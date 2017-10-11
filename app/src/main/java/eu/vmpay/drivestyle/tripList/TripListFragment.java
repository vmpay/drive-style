package eu.vmpay.drivestyle.tripList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
	@BindView(R.id.tvFilteringLabel)
	TextView tvFilteringLabel;
	TripItemListener mItemListener = new TripItemListener()
	{
		@Override
		public void onTripClick(Trip clickedTrip)
		{
			mPresenter.openTripDetails(clickedTrip);
		}
	};
	private Unbinder unbinder;
	private SimpleItemRecyclerViewAdapter mListAdapter;

	@Inject
	public TripListFragment()
	{
		// Requires empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mListAdapter = new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS, mItemListener);
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

		RecyclerView recyclerView = root.findViewById(R.id.track_list);
		assert recyclerView != null;
		recyclerView.setAdapter(mListAdapter);

//		if(root.findViewById(R.id.track_detail_container) != null)
//		{
//			// The detail container view will be present only in the
//			// large-screen layouts (res/values-w900dp).
//			// If this view is present, then the
//			// activity should be in two-pane mode.
//			mTwoPane = true;
//		}

		setHasOptionsMenu(true);

		return root;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.menu_clear:
//				mPresenter.clearCompletedTrips();
				break;
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

//		mTasksView.setVisibility(View.VISIBLE);
//		mNoTasksView.setVisibility(View.GONE);
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
	public void showNoTrips()
	{
//		showNoTasksViews(
//				getResources().getString(R.string.no_tasks_all),
//				R.drawable.ic_assignment_turned_in_24dp,
//				false
//		);
	}

	@Override
	public void showBrakeFilterLabel()
	{
		tvFilteringLabel.setText(getResources().getString(R.string.label_brake));
	}

	@Override
	public void showTurnFilterLabel()
	{
		tvFilteringLabel.setText(getResources().getString(R.string.label_turn));
	}

	@Override
	public void showLineChangeFilterLabel()
	{
		tvFilteringLabel.setText(getResources().getString(R.string.label_lane_change));
	}

	@Override
	public void showAllFilterLabel()
	{
		tvFilteringLabel.setText(getResources().getString(R.string.label_all));
	}

	@Override
	public void showNoActiveTrips()
	{
//		showNoTasksViews(
//				getResources().getString(R.string.no_tasks_active),
//				R.drawable.ic_check_circle_24dp,
//				false
//		);
	}

	@Override
	public void showNoCompletedTrips()
	{
//		showNoTasksViews(
//				getResources().getString(R.string.no_tasks_completed),
//				R.drawable.ic_verified_user_24dp,
//				false
//		);
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
//		mTasksView.setVisibility(View.GONE);
//		mNoTasksView.setVisibility(View.VISIBLE);
//
//		mNoTaskMainView.setText(mainText);
//		//noinspection deprecation
//		mNoTaskIcon.setImageDrawable(getResources().getDrawable(iconRes));
//		mNoTaskAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
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
					mItemListener.onTripClick(new Trip(item.content, 0, 0, item.details, item.id));
				}
			});
//			{
//				@Override
//				public void onClick(View v)
//				{
//					if(mTwoPane)
//					{
//						Bundle arguments = new Bundle();
//						arguments.putString(TripDetailFragment.ARG_ITEM_ID, holder.mItem.id);
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
//						intent.putExtra(TripDetailFragment.ARG_ITEM_ID, holder.mItem.id);
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
