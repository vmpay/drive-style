package eu.vmpay.drivestyle.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import eu.vmpay.drivestyle.data.LocationData;
import eu.vmpay.drivestyle.data.Trip;

/**
 * Created by Andrew on 25/09/2017.
 * Main entry point for accessing trips data.
 */

public interface TripDataSource
{
	//---------------------------------------------------------------TRIPS---------------------------------------------------------------
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

	//---------------------------------------------------------------LOCATIONS---------------------------------------------------------------

	void getLocations(@NonNull String tripId, @NonNull LoadLocationsCallback callback);

	void getLocation(@NonNull String locationDataId, @NonNull GetLocationCallback callback);

	void saveLocation(@NonNull LocationData locationData);

	void deleteAllLocations();

	void deleteLocation(@NonNull long locationDataId);

	interface LoadLocationsCallback
	{

		void onLocationsLoaded(List<LocationData> locationDataList);

		void onDataNotAvailable();
	}

	interface GetLocationCallback
	{

		void onLocationLoaded(LocationData locationData);

		void onDataNotAvailable();
	}
}
