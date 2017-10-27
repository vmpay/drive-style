package eu.vmpay.drivestyle.addTrip;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import eu.vmpay.drivestyle.sensors.location.FusedLocationProviderContract;
import eu.vmpay.drivestyle.sensors.motion.AccelerometerSensorContract;

/**
 * Created by andrew on 10/26/17.
 */

final class AddTripPresenter implements AddTripContract.Presenter
{
	private final AccelerometerSensorContract accelerometerSensor;
	private final FusedLocationProviderContract fusedLocationProvider;
	@Nullable
	private AddTripContract.View addTripView;

	@Inject
	public AddTripPresenter(AccelerometerSensorContract accelerometerSensor, FusedLocationProviderContract fusedLocationProvider)
	{
		this.accelerometerSensor = accelerometerSensor;
		this.fusedLocationProvider = fusedLocationProvider;
	}

	@Override
	public void takeView(AddTripContract.View view)
	{
		addTripView = view;
	}

	@Override
	public void dropView()
	{
		addTripView = null;
	}
}