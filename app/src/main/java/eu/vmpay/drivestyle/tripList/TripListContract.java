package eu.vmpay.drivestyle.tripList;

import android.support.annotation.NonNull;

import java.util.List;

import eu.vmpay.drivestyle.BasePresenter;
import eu.vmpay.drivestyle.BaseView;
import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.tripDetails.TripDetailContract;

/**
 * Created by andrew on 9/26/17.
 */

public interface TripListContract
{
	interface View extends BaseView<TripDetailContract.Presenter>
	{
		void setLoadingIndicator(boolean active);

		void showTrips(List<Trip> trips);

		void showAddTrip();

		void showTripDetailsUi(String tripId);

		void showTripMarkedComplete();

		void showTripMarkedActive();

		void showCompletedTripsCleared();

		void showLoadingTripsError();

		void showNoTrips();

		void showActiveFilterLabel();

		void showCompletedFilterLabel();

		void showAllFilterLabel();

		void showNoActiveTrips();

		void showNoCompletedTrips();

		void showSuccessfullySavedMessage();

		boolean isActive();

		void showFilteringPopUpMenu();
	}

	interface Presenter extends BasePresenter<View>
	{
		void result(int requestCode, int resultCode);

		void loadTripList(boolean forceUpdate);

		void addNewTrip();

		void openTripDetails(@NonNull Trip requestedTrip);

		void clearCompletedTrips();

		TripListFilterType getFiltering();

		void setFiltering(TripListFilterType requestType);

		void takeView(TripListContract.View view);

		void dropView();
	}
}
