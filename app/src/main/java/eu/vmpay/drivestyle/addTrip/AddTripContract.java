package eu.vmpay.drivestyle.addTrip;

import eu.vmpay.drivestyle.BasePresenter;
import eu.vmpay.drivestyle.BaseView;
import eu.vmpay.drivestyle.tripList.TripListFilterType;

/**
 * Created by andrew on 10/26/17.
 */

public interface AddTripContract
{
	interface View extends BaseView<Presenter>
	{
		boolean isActive();

		void showStep(int stepIndex);

		void motionSensorCalibrated();

		void showMotionData(double[] acceleration);
	}

	interface Presenter extends BasePresenter<View>
	{
		void proceed();

		void startMotionSensor();

		void stopMotionSensor();

		void saveData(String tripTitle, String type, TripListFilterType scenario);
	}
}
