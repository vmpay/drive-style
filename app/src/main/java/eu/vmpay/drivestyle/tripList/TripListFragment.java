package eu.vmpay.drivestyle.tripList;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import eu.vmpay.drivestyle.R;
import eu.vmpay.drivestyle.addTrip.AddTripActivity;
import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.tripDetails.TripDetailActivity;

/**
 * Created by andrew on 9/26/17.
 */

public class TripListFragment extends DaggerFragment implements TripListContract.View
{
	private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;

	@Inject TripListContract.Presenter mPresenter;

	@BindView(R.id.track_list) RecyclerView recyclerView;
	@BindView(R.id.llTripList) LinearLayout llTripList;
	@BindView(R.id.llNoTrips) LinearLayout llNoTrips;
	@BindView(R.id.ivNoTrips) ImageView ivNoTrips;
	@BindView(R.id.tvNoTrips) TextView tvNoTrips;
	@BindView(R.id.tvInstruction) TextView tvInstruction;

	TripItemListener mItemListener = new TripItemListener()
	{
		@Override
		public void onTripClick(Trip clickedTrip)
		{
			mPresenter.openTripDetails(clickedTrip);
		}
	};
	private Unbinder unbinder;
	private TripListRecyclerViewAdapter mListAdapter;

	@Inject
	public TripListFragment()
	{
		// Requires empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mListAdapter = new TripListRecyclerViewAdapter(new ArrayList<Trip>(0), mItemListener);
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
		super.onDestroy();
		unbinder.unbind();
		mPresenter.dropView();  //prevent leaking activity in
		// case presenter is orchestrating a long running task
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		mPresenter.result(requestCode, resultCode);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.track_list, container, false);
		unbinder = ButterKnife.bind(this, root);

		assert recyclerView != null;
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(mListAdapter);
//		recyclerView.setHasFixedSize(true);

		setHasOptionsMenu(true);

		return root;
	}

	@OnClick({ R.id.fab })
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.fab:
				mPresenter.openAddTripDetails();
				break;
			default:

		}
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
					showExportDialog();
				}
				break;
			case R.id.menu_filter:
				showFilteringPopUpMenu();
				break;
		}
		return true;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.menu_trip_list_fragment, menu);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		if(requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
		{
			if(grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				showExportDialog();
				// permission was granted, yay! Do the
				// contacts-related task you need to do.

			}
			else
			{
				showMessage(R.string.export_failed);
				// permission denied, boo! Disable the
				// functionality that depends on this permission.
			}
		}
		else
		{
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	private void showExportDialog()
	{
		TextInputLayout textInputLayout = new TextInputLayout(getActivity());
		final EditText input = new EditText(getActivity());
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setMaxLines(1);
		input.setHint(R.string.filename);
		textInputLayout.addView(input);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.export)
				.setMessage(R.string.message_export_dialog)
				.setCancelable(true)
				.setNegativeButton(R.string.cancel, null)
				.setPositiveButton(R.string.export, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						mPresenter.exportCsv(input.getText().toString());
					}
				})
				.setView(textInputLayout);
		builder.show();
	}

	@Override
	public void showTrips(List<Trip> trips)
	{
		mListAdapter.replaceData(trips);

		llTripList.setVisibility(View.VISIBLE);
		llNoTrips.setVisibility(View.GONE);
	}

	@Override
	public void showAddTrip()
	{
		Intent intent = new Intent(getContext(), AddTripActivity.class);
		startActivityForResult(intent, AddTripActivity.REQUEST_ADD_TRIP);
	}

	@Override
	public void showTripDetailsUi(String tripId)
	{
		//Shown in it's own Activity, since it makes more sense that way
		// and it gives us the flexibility to show some Intent stubbing.
		Intent intent = new Intent(getContext(), TripDetailActivity.class);
		intent.putExtra(TripDetailActivity.EXTRA_TRIP_ID, tripId);
		startActivity(intent);
	}

	@Override
	public void showCompletedTripsCleared()
	{
		showMessage(getString(R.string.trips_cleared));
	}

	@Override
	public void showLoadingTripsError()
	{
		showMessage(getString(R.string.loading_trips_error));
	}

	@Override
	public void showBrakeFilterLabel()
	{
		Activity activity = this.getActivity();
		Toolbar appBarLayout = activity.findViewById(R.id.toolbar);
		if(appBarLayout != null)
		{
			appBarLayout.setTitle(getResources().getString(R.string.label_brake));
		}
	}

	@Override
	public void showTurnFilterLabel()
	{
		Activity activity = this.getActivity();
		Toolbar appBarLayout = activity.findViewById(R.id.toolbar);
		if(appBarLayout != null)
		{
			appBarLayout.setTitle(getResources().getString(R.string.label_turn));
		}
	}

	@Override
	public void showLaneChangeFilterLabel()
	{
		Activity activity = this.getActivity();
		Toolbar appBarLayout = activity.findViewById(R.id.toolbar);
		if(appBarLayout != null)
		{
			appBarLayout.setTitle(getResources().getString(R.string.label_lane_change));
		}
	}

	@Override
	public void showAllFilterLabel()
	{
		Activity activity = this.getActivity();
		Toolbar appBarLayout = activity.findViewById(R.id.toolbar);
		if(appBarLayout != null)
		{
			appBarLayout.setTitle(getResources().getString(R.string.label_all));
		}
	}

	@Override
	public void showNoTrips()
	{
		showNoTasksViews(
				getResources().getString(R.string.no_trips_all),
				R.drawable.ic_do_not_disturb_black_24dp,
				true
		);
	}

	@Override
	public void showNoBrakeTrips()
	{
		showNoTasksViews(
				getResources().getString(R.string.no_trips_brake),
				R.drawable.ic_do_not_disturb_black_24dp,
				false
		);
	}

	@Override
	public void showNoTurnTrips()
	{
		showNoTasksViews(
				getResources().getString(R.string.no_trips_turn),
				R.drawable.ic_do_not_disturb_black_24dp,
				false
		);
	}

	@Override
	public void showNoLaneChangeTrips()
	{
		showNoTasksViews(
				getResources().getString(R.string.no_trips_lane_change),
				R.drawable.ic_do_not_disturb_black_24dp,
				false
		);
	}

	@Override
	public void showExportSucceeded()
	{
		showMessage(getString(R.string.export_succeeded));
	}

	@Override
	public void showExportFailed()
	{
		showMessage(getString(R.string.export_failed));
	}

	@Override
	public boolean isActive()
	{
		return isAdded();
	}

	private void showMessage(int messageResId)
	{
		Snackbar.make(getView(), messageResId, Snackbar.LENGTH_LONG).show();
	}

	private void showMessage(String message)
	{
		Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
	}

	private void showNoTasksViews(String mainText, int iconRes, boolean showAddView)
	{
		llTripList.setVisibility(View.GONE);
		llNoTrips.setVisibility(View.VISIBLE);

		tvNoTrips.setText(mainText);
		//noinspection deprecation
		ivNoTrips.setImageResource(iconRes);
		tvInstruction.setVisibility(showAddView ? View.VISIBLE : View.GONE);
	}

	@Override
	public void showFilteringPopUpMenu()
	{
		PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
		popup.getMenuInflater().inflate(R.menu.filter_trips, popup.getMenu());

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
		{
			public boolean onMenuItemClick(MenuItem item)
			{
				switch(item.getItemId())
				{
					case R.id.brake:
						mPresenter.setFiltering(TripListFilterType.BRAKE);
						break;
					case R.id.turn:
						mPresenter.setFiltering(TripListFilterType.TURN);
						break;
					case R.id.lane_change:
						mPresenter.setFiltering(TripListFilterType.LANE_CHANGE);
						break;
					default:
						mPresenter.setFiltering(TripListFilterType.ALL);
						break;
				}
				mPresenter.loadTripList();
				return true;
			}
		});

		popup.show();
	}

	@Override
	public void showInvalidFilename()
	{
		showMessage(R.string.invalid_filename);
	}

	public interface TripItemListener
	{
		void onTripClick(Trip clickedTrip);
	}
}
