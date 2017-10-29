package eu.vmpay.drivestyle;


import android.app.Application;
import android.support.annotation.VisibleForTesting;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import eu.vmpay.drivestyle.data.source.TripsRepository;
import eu.vmpay.drivestyle.di.AppComponent;
import eu.vmpay.drivestyle.di.DaggerAppComponent;
import eu.vmpay.drivestyle.sensors.location.FusedLocationProviderContract;
import eu.vmpay.drivestyle.sensors.motion.AccelerometerSensorContract;

/**
 * Created by andrew on 9/25/17.
 * We create a custom {@link Application} class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @Singleton Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */

public class DriveStyleApplication extends DaggerApplication
{
	@Inject
	TripsRepository tripsRepository;

	@Inject
	AccelerometerSensorContract accelerometerSensor;

	@Inject
	FusedLocationProviderContract fusedLocationProvider;

	@Override
	protected AndroidInjector<? extends DaggerApplication> applicationInjector()
	{
		AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
		appComponent.inject(this);
		return appComponent;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
//		fusedLocationProvider.connectClient();
	}

	@VisibleForTesting
	public TripsRepository getTripsRepository()
	{
		return tripsRepository;
	}
}
