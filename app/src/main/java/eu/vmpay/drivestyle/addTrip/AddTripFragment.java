package eu.vmpay.drivestyle.addTrip;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.vmpay.drivestyle.R;
import eu.vmpay.drivestyle.di.ActivityScoped;

/**
 * A placeholder fragment containing a simple view.
 */
@ActivityScoped
public class AddTripFragment extends Fragment implements AddTripContract.View
{
	@Inject
	AddTripContract.Presenter mPresenter;

	private Unbinder unbinder;

	@Inject
	public AddTripFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_add_trip_activty, container, false);
		unbinder = ButterKnife.bind(this, rootView);

		FloatingActionButton fab = rootView.findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		setHasOptionsMenu(true);

		return rootView;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mPresenter.takeView(this);
	}

	@Override
	public void onDestroy()
	{
		mPresenter.dropView();
		unbinder.unbind();
		super.onDestroy();
	}

	@Override
	public boolean isActive()
	{
		return isAdded();
	}
}
