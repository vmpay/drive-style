package eu.vmpay.drivestyle.addTrip;

import eu.vmpay.drivestyle.BasePresenter;
import eu.vmpay.drivestyle.BaseView;

/**
 * Created by andrew on 10/26/17.
 */

public interface AddTripContract
{
	interface View extends BaseView<Presenter>
	{
		boolean isActive();
	}

	interface Presenter extends BasePresenter<View>
	{
	}
}
