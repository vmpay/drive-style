package eu.vmpay.drivestyle;


import android.app.Application;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by andrew on 9/25/17.
 * We create a custom {@link Application} class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @Singleton Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */

public class DriveStyleApplication extends DaggerApplication
{
	@Override
	protected AndroidInjector<? extends DaggerApplication> applicationInjector()
	{
		return null;
	}
}
