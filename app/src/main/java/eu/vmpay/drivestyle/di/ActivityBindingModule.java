package eu.vmpay.drivestyle.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eu.vmpay.drivestyle.addTrip.AddTripActivity;
import eu.vmpay.drivestyle.addTrip.AddTripModule;
import eu.vmpay.drivestyle.tripDetails.TripDetailActivity;
import eu.vmpay.drivestyle.tripDetails.TripDetailModule;
import eu.vmpay.drivestyle.tripList.TripListActivity;
import eu.vmpay.drivestyle.tripList.TripListModule;

/**
 * Created by andrew on 9/26/17.
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create subcomponents for us.
 */
@Module
public abstract class ActivityBindingModule
{
	@ActivityScoped
	@ContributesAndroidInjector(modules = TripListModule.class)
	abstract TripListActivity tripListActivity();

	@ActivityScoped
	@ContributesAndroidInjector(modules = TripDetailModule.class)
	abstract TripDetailActivity tripDetailActivity();

	@ActivityScoped
	@ContributesAndroidInjector(modules = AddTripModule.class)
	abstract AddTripActivity addTripActivity();
}
