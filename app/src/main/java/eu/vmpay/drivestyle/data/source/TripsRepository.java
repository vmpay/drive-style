package eu.vmpay.drivestyle.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.di.AppComponent;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andrew on 9/26/17.
 * Concrete implementation to load trips from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 * <p/>
 * By marking the constructor with {@code @Inject} and the class with {@code @Singleton}, Dagger
 * injects the dependencies required to create an instance of the TripsRespository (if it fails, it
 * emits a compiler error). It uses {@link TripsRepositoryModule} to do so, and the constructed
 * instance is available in {@link AppComponent}.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 */
@Singleton
public class TripsRepository implements TripDataSource
{
	private final TripDataSource mTripsRemoteDataSource;

	private final TripDataSource mTripsLocalDataSource;

	/**
	 * This variable has package local visibility so it can be accessed from tests.
	 */
	Map<String, Trip> mCachedTrips;

	/**
	 * Marks the cache as invalid, to force an update the next time data is requested. This variable
	 * has package local visibility so it can be accessed from tests.
	 */
	boolean mCacheIsDirty = false;

	/**
	 * By marking the constructor with {@code @Inject}, Dagger will try to inject the dependencies
	 * required to create an instance of the TripsRepository. Because {@link TripDataSource} is an
	 * interface, we must provide to Dagger a way to build those arguments, this is done in
	 * {@link TripsRepositoryModule}.
	 * <p>
	 * When two arguments or more have the same type, we must provide to Dagger a way to
	 * differentiate them. This is done using a qualifier.
	 * <p>
	 * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
	 * with {@code @Nullable} values.
	 */
	@Inject
	TripsRepository(@Local TripDataSource tripsRemoteDataSource,
	                @Local TripDataSource tripsLocalDataSource)
	{
		mTripsRemoteDataSource = tripsRemoteDataSource;
		mTripsLocalDataSource = tripsLocalDataSource;
	}

	/**
	 * Gets trips from cache, local data source (SQLite) or remote data source, whichever is
	 * available first.
	 * <p>
	 * Note: {@link LoadTripsCallback#onDataNotAvailable()} is fired if all data sources fail to
	 * get the data.
	 */
	@Override
	public void getTrips(@NonNull final LoadTripsCallback callback)
	{
		checkNotNull(callback);

		// Respond immediately with cache if available and not dirty
		if(mCachedTrips != null && !mCacheIsDirty)
		{
			callback.onTripsLoaded(new ArrayList<>(mCachedTrips.values()));
			return;
		}

		if(mCacheIsDirty)
		{
			// If the cache is dirty we need to fetch new data from the network.
			getTripsFromRemoteDataSource(callback);
		}
		else
		{
			// Query the local storage if available. If not, query the network.
			mTripsLocalDataSource.getTrips(new LoadTripsCallback()
			{
				@Override
				public void onTripsLoaded(List<Trip> trips)
				{
					refreshCache(trips);
					callback.onTripsLoaded(new ArrayList<>(mCachedTrips.values()));
				}

				@Override
				public void onDataNotAvailable()
				{
					getTripsFromRemoteDataSource(callback);
				}
			});
		}
	}

	@Override
	public void saveTrip(@NonNull Trip trip)
	{
		checkNotNull(trip);
		mTripsRemoteDataSource.saveTrip(trip);
		mTripsLocalDataSource.saveTrip(trip);

		// Do in memory cache update to keep the app UI up to date
		if(mCachedTrips == null)
		{
			mCachedTrips = new LinkedHashMap<>();
		}
		mCachedTrips.put(trip.getId(), trip);
	}

	/**
	 * Gets trips from local data source (sqlite) unless the table is new or empty. In that case it
	 * uses the network data source. This is done to simplify the sample.
	 * <p>
	 * Note: {@link GetTripCallback#onDataNotAvailable()} is fired if both data sources fail to
	 * get the data.
	 */
	@Override
	public void getTrip(@NonNull final String tripId, @NonNull final GetTripCallback callback)
	{
		checkNotNull(tripId);
		checkNotNull(callback);

		Trip cachedTrip = getTripWithId(tripId);

		// Respond immediately with cache if available
		if(cachedTrip != null)
		{
			callback.onTripLoaded(cachedTrip);
			return;
		}

		// Load from server/persisted if needed.

		// Is the trip in the local data source? If not, query the network.
		mTripsLocalDataSource.getTrip(tripId, new GetTripCallback()
		{
			@Override
			public void onTripLoaded(Trip trip)
			{
				// Do in memory cache update to keep the app UI up to date
				if(mCachedTrips == null)
				{
					mCachedTrips = new LinkedHashMap<>();
				}
				mCachedTrips.put(trip.getId(), trip);
				callback.onTripLoaded(trip);
			}

			@Override
			public void onDataNotAvailable()
			{
				mTripsRemoteDataSource.getTrip(tripId, new GetTripCallback()
				{
					@Override
					public void onTripLoaded(Trip trip)
					{
						// Do in memory cache update to keep the app UI up to date
						if(mCachedTrips == null)
						{
							mCachedTrips = new LinkedHashMap<>();
						}
						mCachedTrips.put(trip.getId(), trip);
						callback.onTripLoaded(trip);
					}

					@Override
					public void onDataNotAvailable()
					{
						callback.onDataNotAvailable();
					}
				});
			}
		});
	}

	@Override
	public void refreshTrips()
	{
		mCacheIsDirty = true;
	}

	@Override
	public void deleteAllTrips()
	{
		mTripsRemoteDataSource.deleteAllTrips();
		mTripsLocalDataSource.deleteAllTrips();

		if(mCachedTrips == null)
		{
			mCachedTrips = new LinkedHashMap<>();
		}
		mCachedTrips.clear();
	}

	@Override
	public void deleteTrip(@NonNull long tripId)
	{
		mTripsRemoteDataSource.deleteTrip(checkNotNull(tripId));
		mTripsLocalDataSource.deleteTrip(checkNotNull(tripId));

		mCachedTrips.remove(Long.toString(tripId));
	}

	private void getTripsFromRemoteDataSource(@NonNull final LoadTripsCallback callback)
	{
		mTripsRemoteDataSource.getTrips(new LoadTripsCallback()
		{
			@Override
			public void onTripsLoaded(List<Trip> trips)
			{
				refreshCache(trips);
				refreshLocalDataSource(trips);
				callback.onTripsLoaded(new ArrayList<>(mCachedTrips.values()));
			}

			@Override
			public void onDataNotAvailable()
			{
				callback.onDataNotAvailable();
			}
		});
	}

	private void refreshCache(List<Trip> trips)
	{
		if(mCachedTrips == null)
		{
			mCachedTrips = new LinkedHashMap<>();
		}
		mCachedTrips.clear();
		for(Trip trip : trips)
		{
			mCachedTrips.put(trip.getId(), trip);
		}
		mCacheIsDirty = false;
	}

	private void refreshLocalDataSource(List<Trip> trips)
	{
		mTripsLocalDataSource.deleteAllTrips();
		for(Trip trip : trips)
		{
			mTripsLocalDataSource.saveTrip(trip);
		}
	}

	@Nullable
	private Trip getTripWithId(@NonNull String id)
	{
		checkNotNull(id);
		if(mCachedTrips == null || mCachedTrips.isEmpty())
		{
			return null;
		}
		else
		{
			return mCachedTrips.get(id);
		}
	}
}
