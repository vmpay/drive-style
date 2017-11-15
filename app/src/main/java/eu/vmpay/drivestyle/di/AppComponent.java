package eu.vmpay.drivestyle.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;
import eu.vmpay.drivestyle.DriveStyleApplication;
import eu.vmpay.drivestyle.data.source.TripsRepositoryModule;
import eu.vmpay.drivestyle.data.source.local.TripLocalDataSource;
import eu.vmpay.drivestyle.sensors.SensorModule;
import eu.vmpay.drivestyle.sensors.motion.AccelerometerSensor;

/**
 * Created by andrew on 9/26/17.
 * This is a Dagger component. Refer to {@link DriveStyleApplication} for the list of Dagger components
 * used in this application.
 * <p>
 * Even though Dagger allows annotating a {@link Component} as a singleton, the code
 * itself must ensure only one instance of the class is created. This is done in {@link
 * DriveStyleApplication}.
 * //{@link AndroidSupportInjectionModule}
 * // is the module from Dagger.Android that helps with the generation
 * // and location of subcomponents.
 */

@Singleton
@Component(modules = { TripsRepositoryModule.class,
		SensorModule.class,
		ApplicationModule.class,
		ActivityBindingModule.class,
		AndroidSupportInjectionModule.class
})

public interface AppComponent extends AndroidInjector<DaggerApplication>
{

	void inject(DriveStyleApplication application);

	TripLocalDataSource getTripLocalDataSource();

	AccelerometerSensor getAccelerometerSensor();

	@Override
	void inject(DaggerApplication instance);

	// Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
	// never having to instantiate any modules or say which module we are passing the application to.
	// Application will just be provided into our app graph now.
	@Component.Builder
	interface Builder
	{

		@BindsInstance
		AppComponent.Builder application(Application application);

		AppComponent build();
	}
}