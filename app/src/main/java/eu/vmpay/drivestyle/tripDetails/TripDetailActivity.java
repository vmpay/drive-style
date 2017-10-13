package eu.vmpay.drivestyle.tripDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import eu.vmpay.drivestyle.R;
import eu.vmpay.drivestyle.tripList.TripListActivity;
import eu.vmpay.drivestyle.utils.ActivityUtils;

/**
 * An activity representing a single Track detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TripListActivity}.
 */
public class TripDetailActivity extends DaggerAppCompatActivity
{
	public static final String EXTRA_TRIP_ID = "TRIP_ID";
	@Inject
	TripDetailFragment injectedFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track_detail);
		Toolbar toolbar = findViewById(R.id.detail_toolbar);
		setSupportActionBar(toolbar);

		// Show the Up button in the action bar.
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowHomeEnabled(true);
		}

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if(savedInstanceState == null)
		{
			TripDetailFragment fragment = (TripDetailFragment) getSupportFragmentManager()
					.findFragmentById(R.id.track_detail_container);

			if(fragment == null)
			{
				fragment = injectedFragment;
				ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
						fragment, R.id.track_detail_container);
			}
//			// Create the detail fragment and add it to the activity
//			// using a fragment transaction.
//			Bundle arguments = new Bundle();
//			arguments.putString(TripDetailFragment.ARG_ITEM_ID,
//					getIntent().getStringExtra(TripDetailFragment.ARG_ITEM_ID));
//			TripDetailFragment fragment = new TripDetailFragment();
//			fragment.setArguments(arguments);
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.track_detail_container, fragment)
//					.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if(id == android.R.id.home)
		{
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			navigateUpTo(new Intent(this, TripListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onSupportNavigateUp()
	{
		onBackPressed();
		return true;
	}
}
