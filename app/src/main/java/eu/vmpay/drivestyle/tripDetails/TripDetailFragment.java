package eu.vmpay.drivestyle.tripDetails;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
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
import eu.vmpay.drivestyle.tripList.dummy.DummyContent;

/**
 * A fragment representing a single Track detail screen.
 * This fragment is either contained in a {@link TripListActivity}
 * in two-pane mode (on tablets) or a {@link TripDetailActivity}
 * on handsets.
 */
@ActivityScoped
public class TripDetailFragment extends DaggerFragment implements TripDetailContract.View
{
	@NonNull
	public static final String EXTRA_TRIP_ID = "TRIP_ID";
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	@Inject
	String tripId;
	@Inject
	TripDetailContract.Presenter mPresenter;
	@BindView(R.id.trip_detail)
	TextView tripDetail;
	private Unbinder unbinder;
	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	@Inject
	public TripDetailFragment()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

//		long id = Long.parseLong(tripId);
//		if(id > 0)
//		{
//			mItem = DummyContent.ITEM_MAP.get(tripId);
//
//			Activity activity = this.getActivity();
//			CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
//			if(appBarLayout != null)
//			{
//				appBarLayout.setTitle(mItem.content);
//			}
//		}
//		if(getArguments().containsKey(ARG_ITEM_ID))
//		{
//			// Load the dummy content specified by the fragment
//			// arguments. In a real-world scenario, use a Loader
//			// to load content from a content provider.
//			trip = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
//
//			Activity activity = this.getActivity();
//			CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
//			if(appBarLayout != null)
//			{
//				appBarLayout.setTitle(tripId);
//			}
//		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.trip_detail, container, false);
		setHasOptionsMenu(true);
		unbinder = ButterKnife.bind(this, rootView);

		// Show the dummy content as text in a TextView.
		if(mItem != null)
		{
			((TextView) rootView.findViewById(R.id.trip_detail)).setText(mItem.details);
		}

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
//		switch(item.getItemId())
//		{
//			case R.id.menu_delete:
//				mPresenter.deleteTrip();
//				return true;
//		}
		return false;
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
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void showTitle(String title)
	{
		Activity activity = this.getActivity();
		Toolbar appBarLayout = activity.findViewById(R.id.detail_toolbar);
		if(appBarLayout != null)
		{
			appBarLayout.setTitle(title);
		}
	}

	@Override
	public void hideTitle()
	{
		Activity activity = this.getActivity();
		Toolbar appBarLayout = activity.findViewById(R.id.detail_toolbar);
		if(appBarLayout != null)
		{
			appBarLayout.setTitle("");
		}
	}

	@Override
	public void showDetails(String startTime, String finishTime, Double mark, String type, String scenario)
	{
		tripDetail.setText(String.format(Locale.US,
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
		tripDetail.setText(getString(R.string.no_data));
	}
}
