package eu.vmpay.drivestyle.tripDetails;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.data.source.TripDataSource;
import eu.vmpay.drivestyle.data.source.TripsRepository;

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

public class TripDetailPresenter implements TripDetailContract.Presenter
{
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
		mTripsRepository.getTrip(mTripId, new TripDataSource.GetTripCallback()
		{
			@Override
			public void onTripLoaded(Trip trip)
			{
				// The view may not be able to handle UI updates anymore
				if(mTripDetailView == null || !mTripDetailView.isActive())
				{
					return;
				}
//				mTripDetailView.setLoadingIndicator(false);
				if(null == trip)
				{
					mTripDetailView.showLoadingDetailsError();
				}
				else
				{
					showTripDetails(trip);
				}
			}

			@Override
			public void onDataNotAvailable()
			{
				// The view may not be able to handle UI updates anymore
				if(!mTripDetailView.isActive())
				{
					return;
				}
				mTripDetailView.showLoadingDetailsError();
			}
		});
	}

	private void showTripDetails(@NonNull Trip trip)
	{
		String title = trip.getmTitle();
		long startTime = trip.getmStartTime();
		long finishTime = trip.getmFinishTime();
		Double mark = trip.getmMark();
		String type = trip.getmType();
		String scenario = trip.getmScenario();

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
}
