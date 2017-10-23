package eu.vmpay.drivestyle.sensors.location;

/**
 * Created by andrew on 10/23/17.
 */

public interface FusedLocationProviderContract
{
	void connectClient();

	void disconnectClient();

	void requestLocation();

	void stopLocationRequest();
}
