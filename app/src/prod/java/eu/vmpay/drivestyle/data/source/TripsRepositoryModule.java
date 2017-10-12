package eu.vmpay.drivestyle.data.source;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.vmpay.drivestyle.data.source.local.TripLocalDataSource;

/**
 * Created by andrew on 9/26/17.
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
		return new TripLocalDataSource(context);
	}

//	@Singleton
//	@Provides
//	@Remote
//	TripDataSource provideTripsRemoteDataSource()
//	{
//		return new TasksRemoteDataSource();
//	}
}
