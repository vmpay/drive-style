package eu.vmpay.drivestyle.tripDetails;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import eu.vmpay.drivestyle.R;
import eu.vmpay.drivestyle.di.ActivityScoped;
import eu.vmpay.drivestyle.tripList.TripListActivity;

/**
 * A fragment representing a single Track detail screen.
 * This fragment is either contained in a {@link TripListActivity}
 * in two-pane mode (on tablets) or a {@link TripDetailActivity}
 * on handsets.
 */
@ActivityScoped
public class TripDetailFragment extends DaggerFragment implements TripDetailContract.View
{
	@Inject
	String tripId;
	@Inject
	TripDetailContract.Presenter mPresenter;
	@BindView(R.id.trip_detail)
	TextView tripDetail;
	private Unbinder unbinder;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	@Inject
	public TripDetailFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.trip_detail, container, false);
		setHasOptionsMenu(true);
		unbinder = ButterKnife.bind(this, rootView);

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
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return false;
	}

	@OnClick({ R.id.fab })
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.fab:
				Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
				// TODO: launch edit trip activity
				break;
			default:

		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void showTitle(String title)
	{
		Activity activity = this.getActivity();
		Toolbar appBarLayout = activity.findViewById(R.id.detail_toolbar);
		if(appBarLayout != null)
		{
			appBarLayout.setTitle(title);
		}
	}

	@Override
	public void hideTitle()
	{
		Activity activity = this.getActivity();
		Toolbar appBarLayout = activity.findViewById(R.id.detail_toolbar);
		if(appBarLayout != null)
		{
			appBarLayout.setTitle("");
		}
	}

	@Override
	public void showDetails(String startTime, String finishTime, Double mark, String type, String scenario)
	{
		tripDetail.setText(String.format(Locale.US,
				"Mark %.2f\nStart time %s\nFinishTime %s\nType %s\nScenario %s",
				mark, startTime, finishTime, type, scenario));
	}

	@Override
	public boolean isActive()
	{
		return isAdded();
	}

	@Override
	public void showLoadingDetailsError()
	{
		hideTitle();
		tripDetail.setText(getString(R.string.no_data));
	}

	@Override
	public void showSnackMessage(String message)
	{
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}
}
