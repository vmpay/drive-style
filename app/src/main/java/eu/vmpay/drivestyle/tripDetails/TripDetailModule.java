package eu.vmpay.drivestyle.tripDetails;

import dagger.Binds;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import eu.vmpay.drivestyle.di.ActivityScoped;
import eu.vmpay.drivestyle.di.FragmentScoped;

import static eu.vmpay.drivestyle.tripDetails.TripDetailActivity.EXTRA_TRIP_ID;

/**
 * Created by andrew on 9/26/17.
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link TripDetailPresenter}.
 */

public abstract class TripDetailModule
{
	@Provides
	@ActivityScoped
	static String provideTaskId(TripDetailActivity activity)
	{
		return activity.getIntent().getStringExtra(EXTRA_TRIP_ID);
	}

	@FragmentScoped
	@ContributesAndroidInjector
	abstract TripDetailFragment tripDetailFragment();

	@ActivityScoped
	@Binds
	abstract TripDetailContract.Presenter tripDetailPresenter(TripDetailPresenter presenter);
}
