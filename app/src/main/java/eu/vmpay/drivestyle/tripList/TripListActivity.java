package eu.vmpay.drivestyle.tripList;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import eu.vmpay.drivestyle.R;
import eu.vmpay.drivestyle.tripDetails.TripDetailActivity;
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

		TripListFragment tripListFragment =
				(TripListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
		if(tripListFragment == null)
		{
			// Get the fragment from dagger
			tripListFragment = taskFragmentProvider.get();
			ActivityUtils.addFragmentToActivity(
					getSupportFragmentManager(), tripListFragment, R.id.contentFrame);
		}


		// Load previously saved state, if available.
		if(savedInstanceState != null)
		{
			TripListFilterType currentFiltering =
					(TripListFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
			mTripListPresenter.setFiltering(currentFiltering);
		}


		/**
		 * Move this stuff to a separate fragment
		 */


	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putSerializable(CURRENT_FILTERING_KEY, mTripListPresenter.getFiltering());

		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				// Open the navigation drawer when the home icon is selected from the toolbar.
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupDrawerContent(NavigationView navigationView)
	{
		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener()
				{
					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem)
					{
						switch(menuItem.getItemId())
						{
							case R.id.list_navigation_menu_item:
								// Do nothing, we're already on that screen
								break;
							case R.id.settings_navigation_menu_item:
//								Intent intent =
//										new Intent(TasksActivity.this, StatisticsActivity.class);
//								startActivity(intent);
								break;
							case R.id.logout_navigation_menu_item:
								//TODO: log out
								break;
							default:
								break;
						}
						// Close the navigation drawer when an item is selected.
						menuItem.setChecked(true);
						mDrawerLayout.closeDrawers();
						return true;
					}
				});
	}
}
