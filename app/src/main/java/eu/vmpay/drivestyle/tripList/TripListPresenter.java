package eu.vmpay.drivestyle.tripList;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import eu.vmpay.drivestyle.data.LocationTripView;
import eu.vmpay.drivestyle.data.MotionTripView;
import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.data.source.local.TripLocalDataSource;
import eu.vmpay.drivestyle.di.ActivityScoped;
import eu.vmpay.drivestyle.utils.ExportUtils;
import io.reactivex.subscribers.DisposableSubscriber;

import static dagger.internal.Preconditions.checkNotNull;
import static eu.vmpay.drivestyle.tripList.TripListFilterType.ALL;

/**
 * Created by andrew on 9/26/17.
 * Listens to user actions from the UI ({@link TripListFragment}), retrieves the data and updates the
 * UI as required.
 * <p/>
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the TasksPresenter (if it fails, it emits a compiler error).  It uses
 * {@link TripListModule} to do so.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 **/
@ActivityScoped
public class TripListPresenter implements TripListContract.Presenter
{
	private static final String TAG = "TripListPresenter";

	private final TripLocalDataSource mTripsRepository;
	@Nullable private TripListContract.View mTripListView;

	private TripListFilterType mCurrentFiltering = ALL;

	/**
	 * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
	 * with {@code @Nullable} values.
	 */
	@Inject
	TripListPresenter(TripLocalDataSource tripsRepository)
	{
		mTripsRepository = tripsRepository;
	}

	@Override
	public void result(int requestCode, int resultCode)
	{
//		If a task was successfully added, show snackbar
//		if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode
//				&& Activity.RESULT_OK == resultCode) {
//			if (mTripListView != null) {
//				mTripListView.showExportSucceeded();
//			}
//		}
	}

	@Override
	public void loadTripList()
	{
		Trip trip = new Trip();
		final List<Trip> tripsToShow = new ArrayList<Trip>();
		mTripsRepository.getDataModelsRx(trip).subscribeWith(new DisposableSubscriber<ContentValues>()
		{
			@Override
			public void onNext(ContentValues contentValues)
			{
				Trip entry = Trip.buildFromContentValues(contentValues);
				switch(mCurrentFiltering)
				{
					case ALL:
						tripsToShow.add(entry);
						break;
					case BRAKE:
					case TURN:
					case LANE_CHANGE:
						if(entry.getmScenario().equals(mCurrentFiltering))
						{
							tripsToShow.add(entry);
						}
						break;
					default:
						tripsToShow.add(entry);
						break;
				}
			}

			@Override
			public void onError(Throwable t)
			{
				if(mTripListView != null && mTripListView.isActive())
				{
					mTripListView.showLoadingTripsError();
				}
			}

			@Override
			public void onComplete()
			{
				if(mTripListView != null && mTripListView.isActive())
				{
					processTrips(tripsToShow);
				}
			}
		});
	}

	private void processTrips(List<Trip> tripList)
	{
		if(tripList.isEmpty())
		{
			// Show a message indicating there are no tasks for that filter type.
			processEmptyTrips();
		}
		else
		{
			// Show the list of tasks
			if(mTripListView != null)
			{
				mTripListView.showTrips(tripList);
			}
			// Set the filter label's text.
			showFilterLabel();
		}
	}

	private void processEmptyTrips()
	{
		if(mTripListView == null) return;
		switch(mCurrentFiltering)
		{
			case ALL:
				mTripListView.showNoTrips();
				break;
			case BRAKE:
				mTripListView.showNoBrakeTrips();
				break;
			case TURN:
				mTripListView.showNoTurnTrips();
				break;
			case LANE_CHANGE:
				mTripListView.showNoLaneChangeTrips();
				break;
			default:
				mTripListView.showNoTrips();
				break;
		}
	}

	private void showFilterLabel()
	{
		switch(mCurrentFiltering)
		{
			case ALL:
				if(mTripListView != null)
				{
					mTripListView.showAllFilterLabel();
				}
				break;
			case BRAKE:
				if(mTripListView != null)
				{
					mTripListView.showBrakeFilterLabel();
				}
				break;
			case TURN:
				if(mTripListView != null)
				{
					mTripListView.showTurnFilterLabel();
				}
				break;
			case LANE_CHANGE:
				if(mTripListView != null)
				{
					mTripListView.showLaneChangeFilterLabel();
				}
				break;
			default:
				if(mTripListView != null)
				{
					mTripListView.showAllFilterLabel();
				}
				break;
		}
	}

	@Override
	public void addNewTrip()
	{
		if(mTripListView != null)
		{
			mTripListView.showAddTrip();
		}
	}

	@Override
	public void openTripDetails(@NonNull Trip requestedTrip)
	{
		checkNotNull(requestedTrip, "requestedTrip cannot be null!");
		if(mTripListView != null)
		{
			mTripListView.showTripDetailsUi(requestedTrip.getId());
		}
	}

	@Override
	public void openAddTripDetails()
	{
		if(mTripListView != null)
		{
			mTripListView.showAddTrip();
		}
	}

	@Override
	public TripListFilterType getFiltering()
	{
		return mCurrentFiltering;
	}

	/**
	 * Sets the current task filtering type.
	 *
	 * @param requestType Can be {@link TripListFilterType#ALL},
	 *                    {@link TripListFilterType#BRAKE},
	 *                    {@link TripListFilterType#TURN}, or
	 *                    {@link TripListFilterType#LANE_CHANGE}
	 */
	@Override
	public void setFiltering(TripListFilterType requestType)
	{
		mCurrentFiltering = requestType;
	}

	@Override
	public void takeView(TripListContract.View view)
	{
		this.mTripListView = view;
		this.loadTripList();
	}

	@Override
	public void dropView()
	{
		mTripListView = null;
	}

	@Override
	public void exportCsv(final String filename)
	{
		if(!ExportUtils.isFileNameValid(filename))
		{
			if(mTripListView != null && mTripListView.isActive())
			{
				mTripListView.showInvalidFilename();
			}
			return;
		}
		final boolean[] locationExportFinished = { false };
		final boolean[] motionExportFinished = { false };
		final List<ContentValues> locationValuesArrayList = new ArrayList<>();
		final List<ContentValues> motionValuesArrayList = new ArrayList<>();

		LocationTripView locationTripView = new LocationTripView();
		mTripsRepository.getDataModelsRx(locationTripView).subscribeWith(new DisposableSubscriber<ContentValues>()
		{
			@Override
			public void onNext(ContentValues contentValues)
			{
				locationValuesArrayList.add(contentValues);
			}

			@Override
			public void onError(Throwable t)
			{
				if(mTripListView != null && mTripListView.isActive())
				{
					mTripListView.showExportFailed();
				}
			}

			@Override
			public void onComplete()
			{
				List<String[]> exportList = LocationTripView.getExportListFromContentValuesList(locationValuesArrayList);
				try
				{
					ExportUtils.exportToCsv(filename + "_location", exportList);
					locationExportFinished[0] = true;
					if(mTripListView != null && mTripListView.isActive()
							&& motionExportFinished[0] && locationExportFinished[0])
					{
						mTripListView.showExportSucceeded();
					}
				} catch(IOException e)
				{
					e.printStackTrace();
					if(mTripListView != null && mTripListView.isActive())
					{
						mTripListView.showExportFailed();
					}
				}
			}
		});

		MotionTripView motionTripView = new MotionTripView();
		mTripsRepository.getDataModelsRx(motionTripView).subscribeWith(new DisposableSubscriber<ContentValues>()
		{
			@Override
			public void onNext(ContentValues contentValues)
			{
				motionValuesArrayList.add(contentValues);
			}

			@Override
			public void onError(Throwable t)
			{
				if(mTripListView != null && mTripListView.isActive())
				{
					mTripListView.showExportFailed();
				}
			}

			@Override
			public void onComplete()
			{
				List<String[]> exportList = MotionTripView.getExportListFromContentValuesList(motionValuesArrayList);
				try
				{
					ExportUtils.exportToCsv(filename + "_motion", exportList);
					motionExportFinished[0] = true;
					if(mTripListView != null && mTripListView.isActive()
							&& motionExportFinished[0] && locationExportFinished[0])
					{
						mTripListView.showExportSucceeded();
					}
				} catch(IOException e)
				{
					e.printStackTrace();
					if(mTripListView != null && mTripListView.isActive())
					{
						mTripListView.showExportFailed();
					}
				}
			}
		});
	}
}
