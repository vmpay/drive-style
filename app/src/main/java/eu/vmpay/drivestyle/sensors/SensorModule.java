package eu.vmpay.drivestyle.sensors;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.vmpay.drivestyle.sensors.motion.AccelerometerSensor;

/**
 * Created by Andrew on 22/10/2017.
 */
@Module
public class SensorModule
{
	@Singleton
	@Provides
	AccelerometerSensor provideAccelerometerSensor(Application context)
	{
		return new AccelerometerSensor(context);
	}
}