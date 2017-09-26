package eu.vmpay.drivestyle.tripList;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eu.vmpay.drivestyle.di.ActivityScoped;
import eu.vmpay.drivestyle.di.FragmentScoped;

/**
 * Created by andrew on 9/26/17.
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link TripListPresenter}.
 */
@Module
public abstract class TripListModule
{
	@FragmentScoped
	@ContributesAndroidInjector
	abstract TripListFragment tripListFragment();

	@ActivityScoped
	@Binds
	abstract TripListContract.Presenter tripListPresenter(TripListPresenter presenter);
}
