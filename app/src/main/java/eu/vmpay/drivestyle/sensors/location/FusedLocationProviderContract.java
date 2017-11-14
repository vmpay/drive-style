package eu.vmpay.drivestyle.sensors.location;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.Nullable;

/**
 * Created by andrew on 10/23/17.
 */

public interface FusedLocationProviderContract
{
	void connectClient();

	void disconnectClient();

	void requestLocation(Activity activity, LocationData locationData);

	void stopLocationRequest();

	interface LocationData
	{
		void onLocationDataReceived(@Nullable Location location);
	}
}
