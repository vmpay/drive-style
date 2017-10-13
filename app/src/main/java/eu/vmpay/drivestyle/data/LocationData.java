package eu.vmpay.drivestyle.data;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by andrew on 10/13/17.
 */

public final class LocationData
{
	@Nullable
	private final long mId;

	@NonNull
	private final long tripId;

	@NonNull
	private final long timestamp;

	@NonNull
	private final double latitude;

	@NonNull
	private final double longitude;

	@Nullable
	private final double altitude;

	@Nullable
	private final double speed;

	/**
	 * Default constructor
	 *
	 * @param mId       gps data record id in the database
	 * @param tripId    trip record id in the database
	 * @param timestamp unix timestamp of the recorded data
	 * @param latitude  recorded latitude from {@link LocationSensor}
	 * @param longitude recorded longitude from {@link LocationSensor}
	 * @param altitude  recorded altitude from {@link LocationSensor}
	 * @param speed     recorded speed from {@link LocationSensor}
	 */
	public LocationData(long mId, @NonNull long tripId, @NonNull long timestamp, @NonNull double latitude, @NonNull double longitude, double altitude, double speed)
	{
		this.mId = mId;
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
	 * @param latitude  recorded latitude from {@link LocationSensor}
	 * @param longitude recorded longitude from {@link LocationSensor}
	 * @param altitude  recorded altitude from {@link LocationSensor}
	 * @param speed     recorded speed from {@link LocationSensor}
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
}
