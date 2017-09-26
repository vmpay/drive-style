package eu.vmpay.drivestyle.tripList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import eu.vmpay.drivestyle.R;
import eu.vmpay.drivestyle.tripDetails.TripDetailActivity;
import eu.vmpay.drivestyle.tripDetails.TripDetailFragment;
import eu.vmpay.drivestyle.tripList.dummy.DummyContent;
import eu.vmpay.drivestyle.utils.ActivityUtils;

/**
 * An activity representing a list of Tracks. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TripDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TripListActivity extends DaggerAppCompatActivity
{
	private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";
	@Inject
	TripListPresenter mTripListPresenter;
	@Inject
	Lazy<TripListFragment> taskFragmentProvider;
	private DrawerLayout mDrawerLayout;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track_list);

//		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
//		toolbar.setTitle(getTitle());

		// Set up the toolbar.
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar ab = getSupportActionBar();
		ab.setHomeAsUpIndicator(R.drawable.ic_menu);
		ab.setDisplayHomeAsUpEnabled(true);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		// Set up the navigation drawer.
		mDrawerLayout = findViewById(R.id.drawer_layout);
		mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
		NavigationView navigationView = findViewById(R.id.nav_view);
		if(navigationView != null)
		{
			setupDrawerContent(navigationView);
		}

		TasksFragment tasksFragment =
				(TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
		if(tasksFragment == null)
		{
			// Get the fragment from dagger
			tasksFragment = taskFragmentProvider.get();
			ActivityUtils.addFragmentToActivity(
					getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
		}


		// Load previously saved state, if available.
		if(savedInstanceState != null)
		{
			TasksFilterType currentFiltering =
					(TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
			mTasksPresenter.setFiltering(currentFiltering);
		}
		/**
		 *
		 */

		View recyclerView = findViewById(R.id.track_list);
		assert recyclerView != null;
		setupRecyclerView((RecyclerView) recyclerView);

		if(findViewById(R.id.track_detail_container) != null)
		{
			// The detail container view will be present only in the
			// large-screen layouts (res/values-w900dp).
			// If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
		}
	}

	private void setupRecyclerView(@NonNull RecyclerView recyclerView)
	{
		recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
	}

	public class SimpleItemRecyclerViewAdapter
			extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
	{

		private final List<DummyContent.DummyItem> mValues;

		public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items)
		{
			mValues = items;
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
			holder.mItem = mValues.get(position);
			holder.mIdView.setText(mValues.get(position).id);
			holder.mContentView.setText(mValues.get(position).content);

			holder.mView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(mTwoPane)
					{
						Bundle arguments = new Bundle();
						arguments.putString(TripDetailFragment.ARG_ITEM_ID, holder.mItem.id);
						TripDetailFragment fragment = new TripDetailFragment();
						fragment.setArguments(arguments);
						getSupportFragmentManager().beginTransaction()
								.replace(R.id.track_detail_container, fragment)
								.commit();
					}
					else
					{
						Context context = v.getContext();
						Intent intent = new Intent(context, TripDetailActivity.class);
						intent.putExtra(TripDetailFragment.ARG_ITEM_ID, holder.mItem.id);

						context.startActivity(intent);
					}
				}
			});
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
