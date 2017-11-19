package eu.vmpay.drivestyle.tripDetails;

import com.google.android.gms.maps.OnMapReadyCallback;

import eu.vmpay.drivestyle.BasePresenter;
import eu.vmpay.drivestyle.BaseView;

/**
 * Created by andrew on 9/26/17.
 */

public interface TripDetailContract
{
	interface View extends BaseView<Presenter>
	{
		void showTitle(String title);

		void hideTitle();

		void showDetails(String startTime, String finishTime, Double mark, String type, String scenario);

		boolean isActive();

		void showLoadingDetailsError();

		void showLoadingLocationDataError();

		void showExportSucceeded();

		void showExportFailed();

		void goUp();

		void hideMap();

		void showMap();
	}

	interface Presenter extends BasePresenter<View>, OnMapReadyCallback
	{
		void exportCsv();

		void deleteTrip();

		void editTrip();
	}
}
