package eu.vmpay.drivestyle.addTrip;

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

public class AddTripActivity extends DaggerAppCompatActivity
{
	public static final int REQUEST_ADD_TRIP = 1;

	@Inject
	AddTripFragment injectedFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_trip_activty);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowHomeEnabled(true);
		}

		if(savedInstanceState == null)
		{
			AddTripFragment fragment = (AddTripFragment) getSupportFragmentManager()
					.findFragmentById(R.id.add_trip_container);

			if(fragment == null)
			{
				fragment = injectedFragment;
				ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
						fragment, R.id.add_trip_container);
			}
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
}
