package eu.vmpay.drivestyle.tripList;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.data.source.TripDataSource;
import eu.vmpay.drivestyle.data.source.TripsRepository;
import eu.vmpay.drivestyle.di.ActivityScoped;

import static com.google.common.base.Preconditions.checkNotNull;
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
	private final TripsRepository mTripsRepository;
	@Nullable
	private TripListContract.View mTripListView;

	private TripListFilterType mCurrentFiltering = ALL;

	private boolean mFirstLoad = true;

	/**
	 * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
	 * with {@code @Nullable} values.
	 */
	@Inject
	TripListPresenter(TripsRepository tripsRepository)
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
//				mTripListView.showSuccessfullySavedMessage();
//			}
//		}
	}

	@Override
	public void loadTripList(boolean forceUpdate)
	{
		// Simplification for sample: a network reload will be forced on first load.
		loadTripList(forceUpdate || mFirstLoad, true);
		mFirstLoad = false;
	}

	/**
	 * @param forceUpdate   Pass in true to refresh the data in the {@link TripDataSource}
	 * @param showLoadingUI Pass in true to display a loading icon in the UI
	 */
	private void loadTripList(boolean forceUpdate, final boolean showLoadingUI)
	{
		if(showLoadingUI)
		{
			if(mTripListView != null)
			{
				mTripListView.setLoadingIndicator(true);
			}
		}
		if(forceUpdate)
		{
			mTripsRepository.refreshTrips();
		}

		mTripsRepository.getTrips(new TripDataSource.LoadTripsCallback()
		{
			@Override
			public void onTripsLoaded(List<Trip> trips)
			{
				List<Trip> tripsToShow = new ArrayList<Trip>();

				// We filter the tasks based on the requestType
				for(Trip trip : trips)
				{
					switch(mCurrentFiltering)
					{
						case ALL:
							tripsToShow.add(trip);
							break;
						case BRAKE:
						case TURN:
						case LANE_CHANGE:
							if(trip.getmScenario().equals(mCurrentFiltering))
							{
								tripsToShow.add(trip);
							}
							break;
						default:
							tripsToShow.add(trip);
							break;
					}
				}

				// The view may not be able to handle UI updates anymore
				if(mTripListView == null || !mTripListView.isActive())
				{
					return;
				}
				if(showLoadingUI)
				{
					mTripListView.setLoadingIndicator(false);
				}

				processTrips(tripsToShow);
			}

			@Override
			public void onDataNotAvailable()
			{
				// The view may not be able to handle UI updates anymore
				if(!mTripListView.isActive())
				{
					return;
				}
				mTripListView.showLoadingTripsError();
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
		this.loadTripList(false);
	}

	@Override
	public void dropView()
	{
		mTripListView = null;
	}
}
