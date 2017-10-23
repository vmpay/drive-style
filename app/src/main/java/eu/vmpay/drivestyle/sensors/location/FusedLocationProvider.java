package eu.vmpay.drivestyle.sensors.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andrew on 10/23/17.
 */
@Singleton
public class FusedLocationProvider implements FusedLocationProviderContract, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
	private final String TAG = "FusedLocationProvider";

	private Context mContext;
	private FusedLocationProviderClient fusedLocationProviderClient;
	private GoogleApiClient googleApiClient;
	private LocationRequest locationRequest;
	private final int REQUEST_CHECK_SETTINGS = 0x1;
	private Activity activity;
	private boolean isServiceConnected = false;

	@Inject
	public FusedLocationProvider(@Nullable Context mContext)
	{
		checkNotNull(mContext);

		this.mContext = mContext;
		googleApiClient = new GoogleApiClient.Builder(mContext)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		fusedLocationProviderClient = new FusedLocationProviderClient(mContext);
	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		locationRequest = LocationRequest.create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(1000)
				.setFastestInterval(5000)
		;
		isServiceConnected = true;
		requestLocation(activity);
	}

	@Override
	public void onConnectionSuspended(int i)
	{
		Log.i(TAG, "GoogleApiClient connection has been suspended");
		isServiceConnected = false;
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
		Log.i(TAG, "GoogleApiClient connection has failed");
		isServiceConnected = false;
	}

	@Override
	public void connectClient(Activity activity)
	{
		if(googleApiClient != null)
		{
			this.activity = activity;
			googleApiClient.connect();
		}
	}

	@Override
	public void disconnectClient()
	{
		if(googleApiClient != null)
		{
			googleApiClient.disconnect();
		}
	}

	@Override
	public void requestLocation(Activity activity)
	{
		if(!isServiceConnected)
		{
			return;
		}

		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(locationRequest);

		PendingResult<LocationSettingsResult> result =
				LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
						builder.build());

		result.setResultCallback(new ResultCallback<LocationSettingsResult>()
		{
			@Override
			public void onResult(@NonNull LocationSettingsResult locationSettingsResult)
			{
				final Status status = locationSettingsResult.getStatus();
				final LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();
				switch(status.getStatusCode())
				{
					case LocationSettingsStatusCodes.SUCCESS:
						// All location settings are satisfied. The client can
						// initialize location requests here.
						if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
								&& ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
						{
							// TODO: Consider calling
							//    ActivityCompat#requestPermissions
							// here to request the missing permissions, and then overriding
							//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
							//                                          int[] grantResults)
							// to handle the case where the user grants the permission. See the documentation
							// for ActivityCompat#requestPermissions for more details.
							return;
						}
						LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, FusedLocationProvider.this);
						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						// Location settings are not satisfied, but this can be fixed
						// by showing the user a dialog.
						try
						{
							// Show the dialog by calling startResolutionForResult(),
							// and check the result in onActivityResult().
							status.startResolutionForResult(
									FusedLocationProvider.this.activity,
									REQUEST_CHECK_SETTINGS);
						} catch(IntentSender.SendIntentException e)
						{
							// Ignore the error.
						}
						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						// Location settings are not satisfied. However, we have no way
						// to fix the settings so we won't show the dialog.
						break;
				}
			}
		});


//		Task<LocationSettingsResponse> result =
//				LocationServices.getSettingsClient(mContext).checkLocationSettings(builder.build());
//		result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>()
//		{
//			@Override
//			public void onComplete(@NonNull Task<LocationSettingsResponse> task)
//			{
//				try {
//					LocationSettingsResponse response = task.getResult(ApiException.class);
//					// All location settings are satisfied. The client can initialize location
//					// requests here.
//				} catch (ApiException exception) {
//					switch (exception.getStatusCode()) {
//						case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//							// Location settings are not satisfied. But could be fixed by showing the
//							// user a dialog.
//							try {
//								// Cast to a resolvable exception.
//								ResolvableApiException resolvable = (ResolvableApiException) exception;
//								// Show the dialog by calling startResolutionForResult(),
//								// and check the result in onActivityResult().
//								resolvable.startResolutionForResult(
//										mContext,
//										REQUEST_CHECK_SETTINGS);
//							} catch (IntentSender.SendIntentException e) {
//								// Ignore the error.
//							} catch (ClassCastException e) {
//								// Ignore, should be an impossible error.
//							}
//							break;
//						case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//							// Location settings are not satisfied. However, we have no way to fix the
//							// settings so we won't show the dialog.
//							break;
//					}
//				}
//			}
//		});
	}

	@Override
	public void stopLocationRequest()
	{
		LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
	}

	@Override
	public void onLocationChanged(Location location)
	{
		Log.d(TAG, String.format(Locale.US, "Lat %f Long %f Alt %f Speed %f", location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getSpeed()));
	}
}
