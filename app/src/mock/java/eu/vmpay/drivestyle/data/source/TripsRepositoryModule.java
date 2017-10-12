package eu.vmpay.drivestyle.data.source;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.vmpay.drivestyle.data.FakeTripLocalDataSource;

/**
 * Created by andrew on 10/12/17.
 * This is used by Dagger to inject the required arguments into the {@link TripsRepository}.
 */
@Module
public class TripsRepositoryModule
{
	@Singleton
	@Provides
	@Local
	TripDataSource provideTripsLocalDataSource(Application context)
	{
		return new FakeTripLocalDataSource(context);
	}
}
