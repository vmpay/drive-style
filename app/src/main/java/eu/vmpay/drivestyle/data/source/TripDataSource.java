package eu.vmpay.drivestyle.data.source;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import java.util.List;

import eu.vmpay.drivestyle.data.AccelerometerData;
import eu.vmpay.drivestyle.data.BaseModel;
import eu.vmpay.drivestyle.data.LocationData;
import eu.vmpay.drivestyle.data.Trip;

/**
 * Created by Andrew on 25/09/2017.
 * Main entry point for accessing trips data.
 */

public interface TripDataSource
{
	<T extends BaseModel> long saveDataModel(@NonNull T dataModel);

	<T extends BaseModel> void getDataModels(@NonNull T dataModel, @NonNull LoadModelsCallback callback);

	<T extends BaseModel> void getDataModel(@NonNull T dataModel, @NonNull LoadModelCallback callback);

	interface LoadModelsCallback
	{
		void onModelsLoaded(List<ContentValues> contentValuesList);

		void onDataNotAvailable();
	}

	interface LoadModelCallback
	{
		void onModelsLoaded(ContentValues contentValues);

		void onDataNotAvailable();
	}

	//---------------------------------------------------------------TRIPS---------------------------------------------------------------

	void getTrips(@NonNull LoadTripsCallback callback);

	void getTrip(@NonNull String tripId, @NonNull GetTripCallback callback);

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

	//---------------------------------------------------------------ACCELEROMETER---------------------------------------------------------------

	void getAccelerometerDataModels(@NonNull String tripId, @NonNull LoadAccelerometerDataModelsCallback callback);

	void getAccelerometerDataModel(@NonNull String accelerometerDataId, @NonNull GetAccelerometerDataModelCallback callback);

	void deleteAllAccelerometerDataModels();

	void deleteAccelerometerDataModel(@NonNull long accelerometerDataId);

	interface LoadAccelerometerDataModelsCallback
	{

		void onAccelerometerDataModelsLoaded(List<AccelerometerData> accelerometerDataList);

		void onDataNotAvailable();
	}

	interface GetAccelerometerDataModelCallback
	{

		void onAccelerometerDataModelLoaded(AccelerometerData accelerometerData);

		void onDataNotAvailable();
	}
}
