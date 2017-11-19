package eu.vmpay.drivestyle.data;

import android.content.ContentValues;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.vmpay.drivestyle.data.source.local.LocationDataPersistenceContract;
import eu.vmpay.drivestyle.sensors.location.FusedLocationProvider;

/**
 * Created by andrew on 10/13/17.
 */

public final class LocationData extends BaseModel
{
	@NonNull private final long tripId;
	@NonNull private final long timestamp;
	@NonNull private final double latitude;
	@NonNull private final double longitude;
	@Nullable private final double altitude;
	@Nullable private final double speed;

	public LocationData()
	{
		super(-1, LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME, LocationDataPersistenceContract.LocationDataEntity.COLUMNS);
		this.tripId = -1;
		this.timestamp = -1;
		this.latitude = -1;
		this.longitude = -1;
		this.altitude = -1;
		this.speed = -1;
	}

	/**
	 * Default constructor
	 *
	 * @param mId       gps data record id in the database
	 * @param tripId    trip record id in the database
	 * @param timestamp unix timestamp of the recorded data
	 * @param latitude  recorded latitude from {@link FusedLocationProvider}
	 * @param longitude recorded longitude from {@link FusedLocationProvider}
	 * @param altitude  recorded altitude from {@link FusedLocationProvider}
	 * @param speed     recorded speed from {@link FusedLocationProvider}
	 */
	public LocationData(long mId, @NonNull long tripId, @NonNull long timestamp, @NonNull double latitude, @NonNull double longitude, double altitude, double speed)
	{
		super(mId, LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME, LocationDataPersistenceContract.LocationDataEntity.COLUMNS);
		this.tripId = tripId;
		this.timestamp = timestamp;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.speed = speed;
	}

	/**
	 * Use this constructor if mId is not available
	 *
	 * @param tripId    trip record id in the database
	 * @param timestamp unix timestamp of the recorded data
	 * @param latitude  recorded latitude from {@link FusedLocationProvider}
	 * @param longitude recorded longitude from {@link FusedLocationProvider}
	 * @param altitude  recorded altitude from {@link FusedLocationProvider}
	 * @param speed     recorded speed from {@link FusedLocationProvider}
	 */
	public LocationData(@NonNull long tripId, @NonNull long timestamp, @NonNull double latitude, @NonNull double longitude, double altitude, double speed)
	{
		this(-1, tripId, timestamp, latitude, longitude, altitude, speed);
	}

	/**
	 * Location sensor constructor
	 *
	 * @param tripId    trip record id in the database
	 * @param timestamp unix timestamp of the recorded data
	 * @param location  location data from {@link Location}
	 */
	public LocationData(@NonNull long tripId, @NonNull long timestamp, @NonNull Location location)
	{
		this(-1, tripId, timestamp, location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getSpeed());
	}

	@Nullable
	public long getmId()
	{
		return mId;
	}

	@Nullable
	public String getId()
	{
		return Long.toString(mId);
	}

	@NonNull
	public long getTripId()
	{
		return tripId;
	}

	@NonNull
	public long getTimestamp()
	{
		return timestamp;
	}

	@NonNull
	public double getLatitude()
	{
		return latitude;
	}

	@NonNull
	public double getLongitude()
	{
		return longitude;
	}

	@Nullable
	public double getAltitude()
	{
		return altitude;
	}

	@Nullable
	public double getSpeed()
	{
		return speed;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		LocationData locationData = (LocationData) o;

		if(mId != locationData.mId) return false;
		if(tripId != locationData.tripId) return false;
		if(timestamp != locationData.timestamp) return false;
		if(Double.compare(locationData.latitude, latitude) != 0) return false;
		if(Double.compare(locationData.longitude, longitude) != 0) return false;
		if(Double.compare(locationData.altitude, altitude) != 0) return false;
		return Double.compare(locationData.speed, speed) == 0;

	}

	@Override
	public int hashCode()
	{
		int result;
		long temp;
		result = (int) (mId ^ (mId >>> 32));
		result = 31 * result + (int) (tripId ^ (tripId >>> 32));
		result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
		temp = Double.doubleToLongBits(latitude);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(altitude);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(speed);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString()
	{
		return "LocationData{" +
				"mId=" + mId +
				", tripId=" + tripId +
				", timestamp=" + timestamp +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", altitude=" + altitude +
				", speed=" + speed +
				'}';
	}

	@Override
	public ContentValues getContentValues()
	{
		ContentValues values = new ContentValues();

		values.put(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TRIP_ID, tripId);
		values.put(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TIMESTAMP, timestamp);
		values.put(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_LATITUDE, latitude);
		values.put(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_LONGITUDE, longitude);
		values.put(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_ALTITUDE, altitude);
		values.put(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_SPEED, speed);

		return values;
	}

	@NonNull
	public static List<LocationData> buildFromContentValuesList(List<ContentValues> contentValuesList)
	{
		List<LocationData> locationDataList = new ArrayList<>();
		if(contentValuesList != null)
		{
			for(ContentValues contentValues : contentValuesList)
			{
				locationDataList.add(buildFromContentValues(contentValues));
			}
		}
		return locationDataList;
	}

	public static LocationData buildFromContentValues(ContentValues contentValues)
	{
		long id = contentValues.getAsLong(LocationDataPersistenceContract.LocationDataEntity._ID);
		long mTripId = contentValues.getAsLong(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TRIP_ID);
		long timestamp = contentValues.getAsLong(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TIMESTAMP);
		double latitude = contentValues.getAsDouble(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_LATITUDE);
		double longitude = contentValues.getAsDouble(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_LONGITUDE);
		double altitude = contentValues.getAsDouble(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_ALTITUDE);
		double speed = contentValues.getAsDouble(LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_SPEED);

		return new LocationData(id, mTripId, timestamp, latitude, longitude, altitude, speed);
	}

	public static List<String[]> getExportListFromContentValuesList(List<ContentValues> contentValuesList)
	{
		List<String[]> result = new ArrayList<>();

		String[] header = new String[7];
		header[0] = LocationDataPersistenceContract.LocationDataEntity._ID;
		header[1] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TRIP_ID;
		header[2] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TIMESTAMP;
		header[3] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_LATITUDE;
		header[4] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_LONGITUDE;
		header[5] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_ALTITUDE;
		header[6] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_SPEED;

		result.add(header);

		for(ContentValues entry : contentValuesList)
		{
			result.add(getExportListFromContentValues(entry));
		}

		return result;
	}

	public static List<String[]> getExportListFromModelList(List<LocationData> locationDataList)
	{
		List<String[]> result = new ArrayList<>();

		String[] header = new String[7];
		header[0] = LocationDataPersistenceContract.LocationDataEntity._ID;
		header[1] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TRIP_ID;
		header[2] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TIMESTAMP;
		header[3] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_LATITUDE;
		header[4] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_LONGITUDE;
		header[5] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_ALTITUDE;
		header[6] = LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_SPEED;

		result.add(header);

		for(LocationData entry : locationDataList)
		{
			result.add(exportModel(entry));
		}

		return result;
	}

	public static String[] getExportListFromContentValues(ContentValues contentValues)
	{
		return exportModel(buildFromContentValues(contentValues));
	}

	public static String[] exportModel(LocationData locationData)
	{
		String[] result = new String[7];
		result[0] = Long.toString(locationData.mId);
		result[1] = Long.toString(locationData.tripId);
		result[2] = dateFormat.format(new Date(locationData.timestamp));
		result[3] = Double.toString(locationData.latitude);
		result[4] = Double.toString(locationData.longitude);
		result[5] = Double.toString(locationData.altitude);
		result[6] = Double.toString(locationData.speed);
		return result;
	}
}
