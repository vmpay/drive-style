package eu.vmpay.drivestyle.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.vmpay.drivestyle.data.AccelerometerData;
import eu.vmpay.drivestyle.data.BaseModel;
import eu.vmpay.drivestyle.data.LocationData;
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
	TripsRepository(@Local TripDataSource tripsLocalDataSource
	)
	{
		mTripsLocalDataSource = tripsLocalDataSource;
	}

	@Override
	public <T extends BaseModel> long saveDataModel(@NonNull T dataModel)
	{
		checkNotNull(dataModel);
		return mTripsLocalDataSource.saveDataModel(dataModel);
	}

	/**
	 * Gets trips from cache, local data source (SQLite) or remote data source, whichever is
	 * available first.
	 * <p>
	 * Note: {@link LoadModelsCallback#onDataNotAvailable()} is fired if all data sources fail to
	 * get the data.
	 */
	@Override
	public <T extends BaseModel> void getDataModels(@NonNull T dataModel, @NonNull LoadModelsCallback callback)
	{
		checkNotNull(dataModel);
		checkNotNull(callback);

		mTripsLocalDataSource.getDataModels(dataModel, callback);
	}

	/**
	 * Gets trips from local data source (sqlite) unless the table is new or empty. In that case it
	 * uses the network data source. This is done to simplify the sample.
	 * <p>
	 * Note: {@link LoadModelCallback#onDataNotAvailable()} is fired if both data sources fail to
	 * get the data.
	 */
	@Override
	public <T extends BaseModel> void getDataModel(@NonNull T dataModel, @NonNull LoadModelCallback callback)
	{
		checkNotNull(dataModel);
		checkNotNull(callback);

		mTripsLocalDataSource.getDataModel(dataModel, callback);
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
				callback.onDataNotAvailable();
			}
		});
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
//				callback.onDataNotAvailable();
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
		mTripsLocalDataSource.deleteTrip(checkNotNull(tripId));

		mCachedTrips.remove(Long.toString(tripId));
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

	//---------------------------------------------------------------TRIPS---------------------------------------------------------------
	@Override
	public void getLocations(@NonNull String tripId, @NonNull final LoadLocationsCallback callback)
	{
		checkNotNull(tripId);
		checkNotNull(callback);

		// Query the local storage if available.
		mTripsLocalDataSource.getLocations(tripId, new LoadLocationsCallback()
		{
			@Override
			public void onLocationsLoaded(List<LocationData> locationDataList)
			{
				callback.onLocationsLoaded(locationDataList);
			}

			@Override
			public void onDataNotAvailable()
			{
				callback.onDataNotAvailable();
			}
		});
	}

	@Override
	public void getLocation(@NonNull String locationDataId, @NonNull final GetLocationCallback callback)
	{
		checkNotNull(locationDataId);
		checkNotNull(callback);

		// Query the local storage if available.
		mTripsLocalDataSource.getLocation(locationDataId, new GetLocationCallback()
		{
			@Override
			public void onLocationLoaded(LocationData locationData)
			{
				callback.onLocationLoaded(locationData);
			}

			@Override
			public void onDataNotAvailable()
			{
				callback.onDataNotAvailable();
			}
		});
	}

	@Override
	public void deleteAllLocations()
	{
		mTripsLocalDataSource.deleteAllLocations();
	}

	@Override
	public void deleteLocation(@NonNull long locationDataId)
	{
		mTripsLocalDataSource.deleteLocation(checkNotNull(locationDataId));
	}


	//---------------------------------------------------------------ACCELEROMETER---------------------------------------------------------------
	@Override
	public void getAccelerometerDataModels(@NonNull String tripId, @NonNull final LoadAccelerometerDataModelsCallback callback)
	{
		checkNotNull(tripId);
		checkNotNull(callback);

		// Query the local storage if available.
		mTripsLocalDataSource.getAccelerometerDataModels(tripId, new LoadAccelerometerDataModelsCallback()
		{
			@Override
			public void onAccelerometerDataModelsLoaded(List<AccelerometerData> accelerometerDataList)
			{
				callback.onAccelerometerDataModelsLoaded(accelerometerDataList);
			}

			@Override
			public void onDataNotAvailable()
			{
				callback.onDataNotAvailable();
			}
		});
	}

	@Override
	public void getAccelerometerDataModel(@NonNull String accelerometerDataId, @NonNull final GetAccelerometerDataModelCallback callback)
	{
		checkNotNull(accelerometerDataId);
		checkNotNull(callback);

		// Query the local storage if available.
		mTripsLocalDataSource.getAccelerometerDataModel(accelerometerDataId, new GetAccelerometerDataModelCallback()
		{
			@Override
			public void onAccelerometerDataModelLoaded(AccelerometerData accelerometerData)
			{
				callback.onAccelerometerDataModelLoaded(accelerometerData);
			}

			@Override
			public void onDataNotAvailable()
			{
				callback.onDataNotAvailable();
			}
		});
	}

	@Override
	public void deleteAllAccelerometerDataModels()
	{
		mTripsLocalDataSource.deleteAllAccelerometerDataModels();
	}

	@Override
	public void deleteAccelerometerDataModel(@NonNull long accelerometerDataId)
	{
		mTripsLocalDataSource.deleteAccelerometerDataModel(checkNotNull(accelerometerDataId));
	}
}
