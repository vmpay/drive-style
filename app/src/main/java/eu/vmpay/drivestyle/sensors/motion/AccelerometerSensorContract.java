package eu.vmpay.drivestyle.sensors.motion;

/**
 * Created by Andrew on 22/10/2017.
 */

public interface AccelerometerSensorContract
{
	void startSensor(AccDataReceived IAccDataReceived);

	void stopSensor();

	interface AccDataReceived
	{
		void onAccDataReceived(double[] acceleration);
	}
}
