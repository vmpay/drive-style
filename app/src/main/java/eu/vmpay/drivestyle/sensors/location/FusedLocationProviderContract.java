package eu.vmpay.drivestyle.sensors.location;

import android.app.Activity;

/**
 * Created by andrew on 10/23/17.
 */

public interface FusedLocationProviderContract
{
	void connectClient();

	void disconnectClient();

	void requestLocation(Activity activity);

	void stopLocationRequest();
}
