package eu.vmpay.drivestyle.tripDetails;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import eu.vmpay.drivestyle.data.AccelerometerData;
import eu.vmpay.drivestyle.data.LocationData;
import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.data.source.TripDataSource;
import eu.vmpay.drivestyle.data.source.TripsRepository;
import eu.vmpay.drivestyle.data.source.local.AccelerometerDataPersistenceContract;
import eu.vmpay.drivestyle.data.source.local.LocationDataPersistenceContract;

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

	private final TripsRepository mTripsRepository;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	@Nullable
	private String mTripId;
	@Nullable
	private TripDetailContract.View mTripDetailView;

	/**
	 * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
	 * with {@code @Nullable} values.
	 */
	@Inject
	TripDetailPresenter(@Nullable String mTripId,
	                    TripsRepository tripsRepository)
	{
		this.mTripId = mTripId;
		mTripsRepository = tripsRepository;
	}

	private void loadDetails()
	{
		if(Strings.isNullOrEmpty(mTripId))
		{
			if(mTripDetailView != null)
			{
				mTripDetailView.showLoadingDetailsError();
			}
			return;
		}

//		if (mTripDetailView != null) {
//			mTripDetailView.setLoadingIndicator(true);
//		}

		Trip trip = new Trip();
		trip.setWhereClause(BaseColumns._ID + " LIKE " + mTripId);

		mTripsRepository.getDataModel(trip, new TripDataSource.LoadModelCallback()
		{
			@Override
			public void onModelsLoaded(ContentValues contentValues)
			{
				Trip trip = Trip.buildFromContentValues(contentValues);
				// The view may not be able to handle UI updates anymore
				if(mTripDetailView == null || !mTripDetailView.isActive())
				{
					return;
				}


				if(null == trip)
				{
					mTripDetailView.showLoadingDetailsError();
				}
				else
				{
					showTripDetails(trip);
				}

				if(trip != null)
				{
					LocationData locationData = new LocationData();
					locationData.setWhereClause(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TRIP_ID + " LIKE " + trip.getmId());
					mTripsRepository.getDataModels(locationData, new TripDataSource.LoadModelsCallback()
					{
						@Override
						public void onModelsLoaded(List<ContentValues> contentValuesList)
						{
							List<LocationData> locationDataList = LocationData.buildFromContentValuesList(contentValuesList);
							for(LocationData entry : locationDataList)
							{
								Log.d(TAG, entry.toString());
							}
							// TODO: 10/13/17 add map
						}

						@Override
						public void onDataNotAvailable()
						{
						}
					});

					AccelerometerData accelerometerData = new AccelerometerData();
					accelerometerData.setWhereClause(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TRIP_ID + " LIKE " + trip.getmId());
					mTripsRepository.getDataModels(accelerometerData, new TripDataSource.LoadModelsCallback()
					{
						@Override
						public void onModelsLoaded(List<ContentValues> contentValuesList)
						{
							List<AccelerometerData> accelerometerDataList = AccelerometerData.buildFromContentValuesList(contentValuesList);
							for(AccelerometerData entry : accelerometerDataList)
							{
								Log.d(TAG, entry.toString());
							}
						}

						@Override
						public void onDataNotAvailable()
						{
						}
					});


//					mTripsRepository.getLocations(trip.getId(), new TripDataSource.LoadLocationsCallback()
//					{
//						@Override
//						public void onLocationsLoaded(List<LocationData> locationDataList)
//						{
//							for(LocationData entry : locationDataList)
//							{
//								Log.d("TAG", entry.toString());
//							}
//							// TODO: 10/13/17 add map
//						}
//
//						@Override
//						public void onDataNotAvailable()
//						{
//						}
//					});
//					mTripsRepository.getAccelerometerDataModels(trip.getId(), new TripDataSource.LoadAccelerometerDataModelsCallback()
//					{
//						@Override
//						public void onAccelerometerDataModelsLoaded(List<AccelerometerData> accelerometerDataList)
//						{
//							for(AccelerometerData entry : accelerometerDataList)
//							{
//								Log.d("TAG", entry.toString());
//							}
//						}
//
//						@Override
//						public void onDataNotAvailable()
//						{
//						}
//					});
				}
			}

			@Override
			public void onDataNotAvailable()
			{
				if(!mTripDetailView.isActive())
				{
					return;
				}
				mTripDetailView.showLoadingDetailsError();
			}
		});

//		mTripsRepository.getTrip(mTripId, new TripDataSource.GetTripCallback()
//		{
//			@Override
//			public void onTripLoaded(Trip trip)
//			{
//				// The view may not be able to handle UI updates anymore
//				if(mTripDetailView == null || !mTripDetailView.isActive())
//				{
//					return;
//				}
////				mTripDetailView.setLoadingIndicator(false);
//				if(null == trip)
//				{
//					mTripDetailView.showLoadingDetailsError();
//				}
//				else
//				{
//					showTripDetails(trip);
//				}
//				if(trip != null)
//				{
//					mTripsRepository.getLocations(trip.getId(), new TripDataSource.LoadLocationsCallback()
//					{
//						@Override
//						public void onLocationsLoaded(List<LocationData> locationDataList)
//						{
//							for(LocationData entry : locationDataList)
//							{
//								Log.d("TAG", entry.toString());
//							}
//							// TODO: 10/13/17 add map
//						}
//
//						@Override
//						public void onDataNotAvailable()
//						{
//						}
//					});
//					mTripsRepository.getAccelerometerDataModels(trip.getId(), new TripDataSource.LoadAccelerometerDataModelsCallback()
//					{
//						@Override
//						public void onAccelerometerDataModelsLoaded(List<AccelerometerData> accelerometerDataList)
//						{
//							for(AccelerometerData entry : accelerometerDataList)
//							{
//								Log.d("TAG", entry.toString());
//							}
//						}
//
//						@Override
//						public void onDataNotAvailable()
//						{
//						}
//					});
//				}
//			}
//
//			@Override
//			public void onDataNotAvailable()
//			{
//				// The view may not be able to handle UI updates anymore
//				if(!mTripDetailView.isActive())
//				{
//					return;
//				}
//				mTripDetailView.showLoadingDetailsError();
//			}
//		});

	}

	private void showTripDetails(@NonNull Trip trip)
	{
		String title = trip.getmTitle();
		long startTime = trip.getmStartTime();
		long finishTime = trip.getmFinishTime();
		Double mark = trip.getmMark();
		String type = trip.getmType();
		String scenario = trip.getmScenario().name();

		if(Strings.isNullOrEmpty(title))
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
	public void deleteTrip()
	{

	}
}
