package eu.vmpay.drivestyle.tripDetails;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
	@Inject String tripId;
	@Inject TripDetailContract.Presenter mPresenter;
	@BindView(R.id.tvTripDetail) TextView tvTripDetail;
	@BindView(R.id.tvTripTitle) TextView tvTripTitle;

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
		switch(item.getItemId())
		{
			case R.id.menu_export:
				mPresenter.exportCsv();
				return true;
			default:
				return false;
		}
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
		inflater.inflate(R.menu.menu_trip_detail_fragment, menu);
	}

	@Override
	public void showTitle(String title)
	{
		tvTripTitle.setText(title);
	}

	@Override
	public void hideTitle()
	{
		tvTripTitle.setText("");
	}

	@Override
	public void showDetails(String startTime, String finishTime, Double mark, String type, String scenario)
	{
		tvTripDetail.setText(String.format(Locale.US,
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
		tvTripDetail.setText(getString(R.string.no_data));
	}

	@Override
	public void showSnackMessage(String message)
	{
		Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void showExportSucceeded()
	{
		Snackbar.make(getView(), R.string.export_succeeded, Snackbar.LENGTH_LONG).show();

	}

	@Override
	public void showExportFailed()
	{
		Snackbar.make(getView(), R.string.export_failed, Snackbar.LENGTH_LONG).show();
	}
}
