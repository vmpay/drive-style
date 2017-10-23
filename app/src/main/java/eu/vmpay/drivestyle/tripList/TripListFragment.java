package eu.vmpay.drivestyle.tripList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.tripDetails.TripDetailActivity;
import eu.vmpay.drivestyle.tripList.dummy.DummyContent;

/**
 * Created by andrew on 9/26/17.
 */

public class TripListFragment extends DaggerFragment implements TripListContract.View
{
	@Inject
	TripListContract.Presenter mPresenter;

	//	@BindView(R.id.tvFilteringLabel)
//	TextView tvFilteringLabel;
	@BindView(R.id.track_list)
	RecyclerView recyclerView;
	@BindView(R.id.llTripList)
	LinearLayout llTripList;
	@BindView(R.id.llNoTrips)
	LinearLayout llNoTrips;
	@BindView(R.id.ivNoTrips)
	ImageView ivNoTrips;
	@BindView(R.id.tvNoTrips)
	TextView tvNoTrips;
	@BindView(R.id.tvInstruction)
	TextView tvInstruction;


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
	private boolean sensorActive = false;

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
				Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
				// TODO: 10/11/17 launch add trip intent
				if(sensorActive)
				{
//					mPresenter.unregisterSensor();
					mPresenter.stopLocationRequest();
				}
				else
				{
					Handler handler = new Handler();
					handler.postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
//							mPresenter.unregisterSensor();
							mPresenter.stopLocationRequest();

						}
					}, 10_000);
//					mPresenter.registerSensor();
					mPresenter.requestLocation();
				}
				sensorActive = !sensorActive;
				break;
			default:

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
//			case R.id.menu_clear:
//				mPresenter.clearCompletedTrips();
//				break;
			case R.id.menu_filter:
				showFilteringPopUpMenu();
				break;
			case R.id.menu_refresh:
				mPresenter.loadTripList(true);
				break;
		}
		return true;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.trip_list_fragment_menu, menu);
	}

	@Override
	public void setLoadingIndicator(boolean active)
	{

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
//		Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
//		startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
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
	public void showSuccessfullySavedMessage()
	{
		showMessage(getString(R.string.successfully_saved_trip_message));
	}

	@Override
	public boolean isActive()
	{
		return isAdded();
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
				mPresenter.loadTripList(false);
				return true;
			}
		});

		popup.show();
	}

	public interface TripItemListener
	{
		void onTripClick(Trip clickedTrip);
	}

	public class SimpleItemRecyclerViewAdapter
			extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
	{

		private final List<DummyContent.DummyItem> mValues;
		private TripItemListener mItemListener;

		public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items, TripItemListener itemListener)
		{
			mValues = items;
			mItemListener = itemListener;
		}

		public void replaceData(List<Trip> trips)
		{
			setList(trips);
			notifyDataSetChanged();
		}

		private void setList(List<Trip> trips)
		{
//			mTasks = checkNotNull(trips);
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.track_list_content, parent, false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, int position)
		{
			final DummyContent.DummyItem item = mValues.get(position);
			holder.mItem = item;
			holder.mIdView.setText(mValues.get(position).id);
			holder.mContentView.setText(mValues.get(position).content);


			holder.mView.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View view)
				{
					mItemListener.onTripClick(new Trip(Long.parseLong(item.id), item.content, 0, 0, 0.0, "Type", TripListFilterType.BRAKE));
				}
			});
//			{
//				@Override
//				public void onClick(View v)
//				{
//					if(mTwoPane)
//					{
//						Bundle arguments = new Bundle();
//						arguments.putString(TripDetailFragment.ARG_ITEM_ID, holder.trip.id);
//						TripDetailFragment fragment = new TripDetailFragment();
//						fragment.setArguments(arguments);
//						getSupportFragmentManager().beginTransaction()
//								.replace(R.id.track_detail_container, fragment)
//								.commit();
//					}
//					else
//					{
//						Context context = v.getContext();
//						Intent intent = new Intent(context, TripDetailActivity.class);
//						intent.putExtra(TripDetailFragment.ARG_ITEM_ID, holder.trip.id);
//
//						context.startActivity(intent);
//					}
//				}
//			});
		}

		@Override
		public int getItemCount()
		{
			return mValues.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder
		{
			public final View mView;
			public final TextView mIdView;
			public final TextView mContentView;
			public DummyContent.DummyItem mItem;

			public ViewHolder(View view)
			{
				super(view);
				mView = view;
				mIdView = view.findViewById(R.id.id);
				mContentView = view.findViewById(R.id.content);
			}

			@Override
			public String toString()
			{
				return super.toString() + " '" + mContentView.getText() + "'";
			}
		}
	}
}
