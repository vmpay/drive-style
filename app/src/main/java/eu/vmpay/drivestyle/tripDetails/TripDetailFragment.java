package eu.vmpay.drivestyle.tripDetails;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

	private final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
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
				if(ContextCompat.checkSelfPermission(getActivity(),
						Manifest.permission.WRITE_EXTERNAL_STORAGE)
						!= PackageManager.PERMISSION_GRANTED)
				{
					requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
							PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
				}
				else
				{
					mPresenter.exportCsv();
				}
				return true;
			case R.id.menu_delete:
				mPresenter.deleteTrip();
				return true;
			default:
				return false;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		if(requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
		{
			if(grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				mPresenter.exportCsv();
				// permission was granted, yay! Do the
				// contacts-related task you need to do.

			}
			else
			{
				showExportFailed();
				// permission denied, boo! Disable the
				// functionality that depends on this permission.
			}
		}
		else
		{
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
	public void showLoadingLocationDataError()
	{
		showSnackMessage(R.string.location_data_error);
	}

	private void showSnackMessage(int messageId)
	{
		Snackbar.make(getView(), messageId, Snackbar.LENGTH_SHORT).show();
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

	@Override
	public void goUp()
	{
		getActivity().navigateUpTo(new Intent(getContext(), TripListActivity.class));
	}
}
