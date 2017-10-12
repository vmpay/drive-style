package eu.vmpay.drivestyle.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Random;

import eu.vmpay.drivestyle.data.source.local.TripLocalDataSource;
import eu.vmpay.drivestyle.tripList.TripListFilterType;

/**
 * Created by andrew on 10/12/17.
 * Concrete implementation of a data source as a db and generation samples of data.
 */

public class FakeTripLocalDataSource extends TripLocalDataSource
{
	private final int FAKE_COUNT = 25;

	public FakeTripLocalDataSource(@NonNull Context context)
	{
		super(context);
		getTrips(new LoadTripsCallback()
		{
			@Override
			public void onTripsLoaded(List<Trip> trips)
			{
			}

			@Override
			public void onDataNotAvailable()
			{
				generateFakeData();
			}
		});

	}

	private void generateFakeData()
	{
		Trip trip;
		Random random = new Random();
		for(int i = 0; i < FAKE_COUNT; i++)
		{
			long timestamp = System.currentTimeMillis();
			trip = new Trip("Trip " + (i + 1), timestamp, timestamp + 5000, random.nextDouble() * 5, "Type", TripListFilterType.BRAKE);
			saveTrip(trip);
		}
	}
}
