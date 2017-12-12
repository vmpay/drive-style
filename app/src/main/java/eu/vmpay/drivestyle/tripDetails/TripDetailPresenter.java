package eu.vmpay.drivestyle.tripDetails;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import eu.vmpay.drivestyle.data.AccelerometerData;
import eu.vmpay.drivestyle.data.LocationData;
import eu.vmpay.drivestyle.data.LocationTripView;
import eu.vmpay.drivestyle.data.MotionTripView;
import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.data.source.local.AccelerometerDataPersistenceContract;
import eu.vmpay.drivestyle.data.source.local.LocationDataPersistenceContract;
import eu.vmpay.drivestyle.data.source.local.LocationTripViewPersistenceContract;
import eu.vmpay.drivestyle.data.source.local.MotionTripViewPersistenceContract;
import eu.vmpay.drivestyle.data.source.local.TripLocalDataSource;
import eu.vmpay.drivestyle.data.source.local.TripPersistenceContract;
import eu.vmpay.drivestyle.utils.ExportUtils;
import eu.vmpay.drivestyle.utils.FilteringUtils;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by andrew on 9/26/17.
 * Listens to user actions from the UI ({@link TripDetailFragment}), retrieves the data and updates
 * the UI as required.
 * <p/>
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the StatisticsPresenter (if it fails, it emits a compiler error). It uses
 * {@link TripDetailModule} to do so.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 */

final class TripDetailPresenter implements TripDetailContract.Presenter
{
	private final String TAG = "TripDetailPresenter";

	private final TripLocalDataSource mTripsRepository;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	@Nullable private String mTripId;
	@Nullable private TripDetailContract.View mTripDetailView;

	private Trip actualTrip;
	private List<LocationData> locationDataList;
	private List<AccelerometerData> accelerometerDataList;

	/**
	 * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
	 * with {@code @Nullable} values.
	 */
	@Inject
	TripDetailPresenter(@Nullable String mTripId,
	                    TripLocalDataSource tripsRepository)
	{
		this.mTripId = mTripId;
		mTripsRepository = tripsRepository;
	}

	private void loadDetails()
	{
		if(isNullOrEmpty(mTripId))
		{
			if(mTripDetailView != null && mTripDetailView.isActive())
			{
				mTripDetailView.showLoadingDetailsError();
			}
			return;
		}

		Trip trip = new Trip();
		trip.setWhereClause(BaseColumns._ID + " LIKE " + mTripId);

		mTripsRepository.getDataModelsRx(trip).subscribeWith(new DisposableSubscriber<ContentValues>()
		{
			@Override
			public void onNext(ContentValues contentValues)
			{
				Trip currentTrip = Trip.buildFromContentValues(contentValues);
				// The view may not be able to handle UI updates anymore
				if(mTripDetailView == null || !mTripDetailView.isActive())
				{
					return;
				}

				actualTrip = currentTrip;
				if(currentTrip == null)
				{
					mTripDetailView.showLoadingDetailsError();
				}
				else
				{
					showTripDetails(currentTrip);

					locationDataList = new ArrayList<LocationData>();
					LocationData locationData = new LocationData();
					locationData.setWhereClause(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TRIP_ID + " LIKE " + currentTrip.getmId());
					mTripsRepository.getDataModelsRx(locationData).subscribeWith(new DisposableSubscriber<ContentValues>()
					{
						@Override
						public void onNext(ContentValues contentValues)
						{
							LocationData entry = LocationData.buildFromContentValues(contentValues);
							Log.d(TAG, entry.toString());
							locationDataList.add(entry);
						}

						@Override
						public void onError(Throwable t)
						{
							t.printStackTrace();
							if(mTripDetailView != null && mTripDetailView.isActive())
							{
								mTripDetailView.showLoadingLocationDataError();
							}
						}

						@Override
						public void onComplete()
						{
							if(mTripDetailView != null && mTripDetailView.isActive() && locationDataList != null && !locationDataList.isEmpty())
							{
								mTripDetailView.showMap();
							}
						}
					});

					accelerometerDataList = new ArrayList<AccelerometerData>();
					AccelerometerData accelerometerData = new AccelerometerData();
					accelerometerData.setWhereClause(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TRIP_ID + " LIKE " + currentTrip.getmId());
					mTripsRepository.getDataModelsRx(accelerometerData).subscribeWith(new DisposableSubscriber<ContentValues>()
					{
						@Override
						public void onNext(ContentValues contentValues)
						{
							AccelerometerData entry = AccelerometerData.buildFromContentValues(contentValues);
							accelerometerDataList.add(entry);
							Log.d(TAG, entry.toString());
						}

						@Override
						public void onError(Throwable t)
						{
							t.printStackTrace();
						}

						@Override
						public void onComplete()
						{
						}
					});
				}
			}

			@Override
			public void onError(Throwable t)
			{
				t.printStackTrace();
				if(mTripDetailView != null && mTripDetailView.isActive())
				{
					mTripDetailView.showLoadingDetailsError();
				}
			}

			@Override
			public void onComplete()
			{
			}
		});
	}

	private boolean isNullOrEmpty(String string)
	{
		return !(string != null && !string.isEmpty());
	}

	private void showTripDetails(@NonNull Trip trip)
	{
		String title = trip.getmTitle();
		long startTime = trip.getmStartTime();
		long finishTime = trip.getmFinishTime();
		Double mark = trip.getmMark();
		String type = trip.getmType();
		String scenario = trip.getmScenario().name();

		if(isNullOrEmpty(title))
		{
			if(mTripDetailView != null)
			{
				mTripDetailView.hideTitle();
			}
		}
		else
		{
			if(mTripDetailView != null)
			{
				mTripDetailView.showTitle(title);
			}
		}

		if(mark == null)
		{
			if(mTripDetailView != null)
			{
				mark = -1.0;
			}
		}

		String mStartTime = dateFormat.format(new Date(startTime));
		String mFinishTime = dateFormat.format(new Date(finishTime));

		if(mTripDetailView != null)
		{
			mTripDetailView.showDetails(mStartTime, mFinishTime, mark, type, scenario);
		}
	}

	@Override
	public void takeView(TripDetailContract.View view)
	{
		mTripDetailView = view;
		loadDetails();
	}

	@Override
	public void dropView()
	{
		mTripDetailView = null;
	}

	@Override
	public void exportCsv()
	{
		if(actualTrip != null)
		{
			final boolean[] exportFailed = { false };

			MotionTripView motionTripView = new MotionTripView();
			motionTripView.setWhereClause(MotionTripViewPersistenceContract.MotionTripEntry._ID + " LIKE " + mTripId);
			final List<ContentValues> motionTripViewList = new ArrayList<>();
			mTripsRepository.getDataModelsRx(motionTripView).subscribeWith(new DisposableSubscriber<ContentValues>()
			{
				@Override
				public void onNext(ContentValues contentValues)
				{
					motionTripViewList.add(contentValues);
				}

				@Override
				public void onError(Throwable t)
				{
					exportFailed[0] = true;
				}

				@Override
				public void onComplete()
				{
					try
					{
						ExportUtils.exportToCsv(actualTrip.getmTitle() + "_motion",
								MotionTripView.getExportListFromContentValuesList(motionTripViewList));
					} catch(IOException e)
					{
						exportFailed[0] = true;
						e.printStackTrace();
					}
				}
			});

			LocationTripView locationTripView = new LocationTripView();
			locationTripView.setWhereClause(LocationTripViewPersistenceContract.LocationTripEntry._ID + " LIKE " + mTripId);
			final List<ContentValues> locationTripViewList = new ArrayList<>();
			mTripsRepository.getDataModelsRx(locationTripView).subscribeWith(new DisposableSubscriber<ContentValues>()
			{
				@Override
				public void onNext(ContentValues contentValues)
				{
					locationTripViewList.add(contentValues);
				}

				@Override
				public void onError(Throwable t)
				{
					exportFailed[0] = true;
				}

				@Override
				public void onComplete()
				{
					try
					{
						ExportUtils.exportToCsv(actualTrip.getmTitle() + "_location",
								LocationTripView.getExportListFromContentValuesList(locationTripViewList));
					} catch(IOException e)
					{
						exportFailed[0] = true;
						e.printStackTrace();
					}
				}
			});

			if(mTripDetailView != null)
			{
				if(exportFailed[0])
				{
					mTripDetailView.showExportFailed();
				}
				else
				{
					mTripDetailView.showExportSucceeded();
				}
			}
		}
	}

	@Override
	public void deleteTrip()
	{
		Trip trip = new Trip();
		trip.setWhereClause(TripPersistenceContract.TripEntry._ID + " LIKE " + mTripId);
		mTripsRepository.deleteDataModelRx(trip).subscribeWith(new DisposableSubscriber<Integer>()
		{
			@Override
			public void onNext(Integer integer)
			{
				Log.d(TAG, "Deleting tripId = " + mTripId + " result = " + integer);
				if(integer > 0 && mTripDetailView != null)
				{
					mTripDetailView.goUp();
				}
			}

			@Override
			public void onError(Throwable t)
			{
			}

			@Override
			public void onComplete()
			{
			}
		});
		LocationData locationData = new LocationData();
		locationData.setWhereClause(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TRIP_ID + " LIKE " + mTripId);
		mTripsRepository.deleteDataModelRx(locationData).subscribe();
		AccelerometerData accelerometerData = new AccelerometerData();
		accelerometerData.setWhereClause(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TRIP_ID + " LIKE " + mTripId);
		mTripsRepository.deleteDataModelRx(accelerometerData).subscribe();
	}

	@Override
	public void editTrip()
	{
		List<Double> originalDoubleValues = new ArrayList<>();
		List<Pair<Number, Number>> originalValues = new ArrayList<>();

		for(int i = 0; i < accelerometerDataList.size(); i++)
		{
			originalValues.add(Pair.<Number, Number>of(i, accelerometerDataList.get(i).getAccZ()));
			originalDoubleValues.add(accelerometerDataList.get(i).getAccZ());
		}

		List<Pair<Number, Number>> result = FilteringUtils.calculateFilter(originalValues, 30, false);
		List<String[]> exportList = new ArrayList<>();
		for(int i = 0; i < result.size(); i++)
		{
			Log.d(TAG, String.format(Locale.US, "%d.\t%s\t%s",
					i,
					result.get(i).getLeft().toString(),
					result.get(i).getRight().toString()
			));
			String[] export = new String[1];
			export[0] = result.get(i).getRight().toString();
			exportList.add(export);
		}

		List<Double> resultDouble = FilteringUtils.movMedian(originalDoubleValues, 30);
		List<String[]> exportDoubleList = new ArrayList<>();
		for(int i = 0; i < resultDouble.size(); i++)
		{
			String[] tmp = new String[1];
			tmp[0] = resultDouble.get(i).toString();
			exportDoubleList.add(tmp);
		}

		try
		{
			ExportUtils.exportToCsv(actualTrip.getmTitle() + "_filtered", exportList);
			ExportUtils.exportToCsv(actualTrip.getmTitle() + "_filtered_median", exportDoubleList);
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap)
	{
		if(locationDataList == null || locationDataList.isEmpty())
		{
			if(mTripDetailView != null && mTripDetailView.isActive())
			{
				mTripDetailView.hideMap();
			}
			return;
		}

		List<LatLng> latLngList = new ArrayList<>();
		for(LocationData entry : locationDataList)
		{
			latLngList.add(new LatLng(entry.getLatitude(), entry.getLongitude()));
		}

		Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
				.clickable(true)
				.addAll(latLngList));

		// Position the map's camera near Alice Springs in the center of Australia,
		// and set the zoom factor so most of Australia shows on the screen.
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationDataList.get(0).getLatitude(), locationDataList.get(0).getLongitude()), 18));
	}
}
