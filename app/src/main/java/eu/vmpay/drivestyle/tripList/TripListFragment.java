package eu.vmpay.drivestyle.tripList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Created by andrew on 9/26/17.
 */

public class TripListFragment extends DaggerFragment implements TripListContract.View
{
	@Inject
	TripListContract.Presenter mPresenter;


}
