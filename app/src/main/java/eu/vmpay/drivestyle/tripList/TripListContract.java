package eu.vmpay.drivestyle.tripList;

import android.support.annotation.NonNull;

import java.util.List;

import eu.vmpay.drivestyle.BasePresenter;
import eu.vmpay.drivestyle.BaseView;
import eu.vmpay.drivestyle.data.Trip;

/**
 * Created by andrew on 9/26/17.
 */

public interface TripListContract
{
	interface View extends BaseView<Presenter>
	{
		void showTrips(List<Trip> trips);

		void showAddTrip();

		void showTripDetailsUi(String tripId);

		void showCompletedTripsCleared();

		void showLoadingTripsError();

		void showNoTrips();

		void showBrakeFilterLabel();

		void showTurnFilterLabel();

		void showLaneChangeFilterLabel();

		void showAllFilterLabel();

		void showNoBrakeTrips();

		void showNoTurnTrips();

		void showNoLaneChangeTrips();

		void showExportSucceeded();

		void showExportFailed();

		boolean isActive();

		void showFilteringPopUpMenu();

		void showInvalidFilename();
	}

	interface Presenter extends BasePresenter<View>
	{
		void result(int requestCode, int resultCode);

		void loadTripList();

		void addNewTrip();

		void openTripDetails(@NonNull Trip requestedTrip);

		void openAddTripDetails();

		TripListFilterType getFiltering();

		void setFiltering(TripListFilterType requestType);

		void takeView(TripListContract.View view);

		void dropView();

		void exportCsv(String filename);
	}
}
