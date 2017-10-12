package eu.vmpay.drivestyle.tripList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import eu.vmpay.drivestyle.R;
import eu.vmpay.drivestyle.data.Trip;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andrew on 10/12/17.
 */

public class TripListRecyclerViewAdapter extends android.support.v7.widget.RecyclerView.Adapter<TripListRecyclerViewAdapter.ViewHolder>
{
	private List<Trip> tripList;
	private TripListFragment.TripItemListener mItemListener;

	public TripListRecyclerViewAdapter(List<Trip> tripList, TripListFragment.TripItemListener mItemListener)
	{
		setList(tripList);
		this.mItemListener = mItemListener;
	}

	public void replaceData(List<Trip> trips)
	{
		setList(trips);
		notifyDataSetChanged();
	}

	private void setList(List<Trip> trips)
	{
		tripList = checkNotNull(trips);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.track_list_item, parent, false);
		return new TripListRecyclerViewAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		final Trip trip = tripList.get(position);
		holder.trip = trip;
		holder.tvName.setText(trip.getmTitle());
		holder.tvMark.setText(String.format(Locale.US, "%.2f", trip.getmMark()));

		holder.mView.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				mItemListener.onTripClick(trip);
			}
		});
	}

	@Override
	public int getItemCount()
	{
		return tripList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		public final View mView;
		public final TextView tvName;
		public final TextView tvMark;
		public Trip trip;

		public ViewHolder(View view)
		{
			super(view);
			mView = view;
			tvName = view.findViewById(R.id.tvName);
			tvMark = view.findViewById(R.id.tvMark);
		}

		@Override
		public String toString()
		{
			return super.toString() + " '" + tvMark.getText() + "'";
		}
	}
}
