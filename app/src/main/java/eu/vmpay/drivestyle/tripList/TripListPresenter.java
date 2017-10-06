package eu.vmpay.drivestyle.tripList;

import android.support.annotation.NonNull;

import javax.annotation.Nullable;
import javax.inject.Inject;

import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.data.source.TripDataSource;
import eu.vmpay.drivestyle.data.source.TripsRepository;
import eu.vmpay.drivestyle.di.ActivityScoped;

import static com.google.common.base.Preconditions.checkNotNull;

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

	private TripListFilterType mCurrentFiltering = TripListFilterType.ALL;

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

		// The network request might be handled in a different thread so make sure Espresso knows
		// that the app is busy until the response is handled.
//		EspressoIdlingResource.increment(); // App is busy until further notice
//
//		mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback()
//		{
//			@Override
//			public void onTasksLoaded(List<Task> tasks)
//			{
//				List<Task> tasksToShow = new ArrayList<>();
//
//				// This callback may be called twice, once for the cache and once for loading
//				// the data from the server API, so we check before decrementing, otherwise
//				// it throws "Counter has been corrupted!" exception.
//				if(!EspressoIdlingResource.getIdlingResource().isIdleNow())
//				{
//					EspressoIdlingResource.decrement(); // Set app as idle.
//				}
//
//				// We filter the tasks based on the requestType
//				for(Task task : tasks)
//				{
//					switch(mCurrentFiltering)
//					{
//						case ALL_TASKS:
//							tasksToShow.add(task);
//							break;
//						case ACTIVE_TASKS:
//							if(task.isActive())
//							{
//								tasksToShow.add(task);
//							}
//							break;
//						case COMPLETED_TASKS:
//							if(task.isCompleted())
//							{
//								tasksToShow.add(task);
//							}
//							break;
//						default:
//							tasksToShow.add(task);
//							break;
//					}
//				}
//				// The view may not be able to handle UI updates anymore
//				if(mTripListView == null || !mTripListView.isActive())
//				{
//					return;
//				}
//				if(showLoadingUI)
//				{
//					mTripListView.setLoadingIndicator(false);
//				}
//
//				processTasks(tasksToShow);
//			}
//
//			@Override
//			public void onDataNotAvailable()
//			{
//				// The view may not be able to handle UI updates anymore
//				if(!mTripListView.isActive())
//				{
//					return;
//				}
//				mTripListView.showLoadingTasksError();
//			}
//		});
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
