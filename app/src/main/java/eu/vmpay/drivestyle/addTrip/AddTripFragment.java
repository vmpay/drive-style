package eu.vmpay.drivestyle.addTrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.vmpay.drivestyle.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddTripFragment extends Fragment
{

	public AddTripFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_add_trip_activty, container, false);
	}
}
