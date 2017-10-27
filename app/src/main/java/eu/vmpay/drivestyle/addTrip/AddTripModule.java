package eu.vmpay.drivestyle.addTrip;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eu.vmpay.drivestyle.di.ActivityScoped;
import eu.vmpay.drivestyle.di.FragmentScoped;

/**
 * Created by andrew on 10/26/17.
 */
@Module
public abstract class AddTripModule
{
	@FragmentScoped
	@ContributesAndroidInjector
	abstract AddTripFragment addTripFragment();

	@ActivityScoped
	@Binds
	abstract AddTripContract.Presenter addTripPresenter(AddTripPresenter presenter);
}
