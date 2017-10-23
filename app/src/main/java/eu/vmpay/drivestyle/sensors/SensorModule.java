package eu.vmpay.drivestyle.sensors;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.vmpay.drivestyle.sensors.location.FusedLocationProvider;
import eu.vmpay.drivestyle.sensors.location.FusedLocationProviderContract;
import eu.vmpay.drivestyle.sensors.motion.AccelerometerSensor;
import eu.vmpay.drivestyle.sensors.motion.AccelerometerSensorContract;

/**
 * Created by Andrew on 22/10/2017.
 */
@Module
public class SensorModule
{
	@Singleton
	@Provides
	AccelerometerSensorContract provideAccelerometerSensor(Application context)
	{
		return new AccelerometerSensor(context);
	}

	@Singleton
	@Provides
	FusedLocationProviderContract provideFusedLocationProvider(Application context)
	{
		return new FusedLocationProvider(context);
	}
}
