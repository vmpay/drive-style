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
			TripListFilterType filterType;
			switch(random.nextInt() % 3)
			{
				case 0:
					filterType = TripListFilterType.BRAKE;
					break;
				case 1:
					filterType = TripListFilterType.TURN;
					break;
				case 2:
					filterType = TripListFilterType.LANE_CHANGE;
					break;
				default:
					filterType = TripListFilterType.BRAKE;
			}
			trip = new Trip("Trip " + (i + 1), timestamp, timestamp + 5000, random.nextDouble() * 5, "Type", filterType);
			saveTrip(trip);
		}
		getTrips(new LoadTripsCallback()
		{
			@Override
			public void onTripsLoaded(List<Trip> trips)
			{
				for(Trip entry : trips)
				{
					saveLocation(new LocationData(entry.getmId(), entry.getmStartTime(), 52.219968, 21.011818, 0, 0));
					saveLocation(new LocationData(entry.getmId(), entry.getmFinishTime(), 52.220162, 21.012359, 0, 0));
					saveAccelerometerDataModel(new AccelerometerData(entry.getmId(), entry.getmStartTime(), 0, 0, 0));
				}
			}

			@Override
			public void onDataNotAvailable()
			{
			}
		});
	}
}
