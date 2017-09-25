package eu.vmpay.drivestyle.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import eu.vmpay.drivestyle.data.Trip;

/**
 * Created by Andrew on 25/09/2017.
 * Main entry point for accessing trips data.
 */

public interface TripDataSource
{
	void getTrips(@NonNull LoadTripsCallback callback);

	void getTrip(@NonNull String tripId, @NonNull GetTripCallback callback);

	void saveTrip(@NonNull Trip trip);

	void refreshTrips();

	void deleteAllTrips();

	void deleteTrip(@NonNull long tripId);

	interface LoadTripsCallback
	{

		void onTripsLoaded(List<Trip> trips);

		void onDataNotAvailable();
	}

	interface GetTripCallback
	{

		void onTripLoaded(Trip trip);

		void onDataNotAvailable();
	}
}
