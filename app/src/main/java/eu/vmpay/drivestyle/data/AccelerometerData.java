package eu.vmpay.drivestyle.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by andrew on 10/13/17.
 */

public final class AccelerometerData
{
	@Nullable
	private final long mId;

	@NonNull
	private final long tripId;

	@NonNull
	private final long timestamp;

	@NonNull
	private final double accX;

	@NonNull
	private final double accY;

	@NonNull
	private final double accZ;

	/**
	 * Default constructor
	 *
	 * @param mId
	 * @param tripId
	 * @param timestamp
	 * @param accX
	 * @param accY
	 * @param accZ
	 */
	public AccelerometerData(long mId, @NonNull long tripId, @NonNull long timestamp, @NonNull double accX, @NonNull double accY, @NonNull double accZ)
	{
		this.mId = mId;
		this.tripId = tripId;
		this.timestamp = timestamp;
		this.accX = accX;
		this.accY = accY;
		this.accZ = accZ;
	}

	/**
	 * Use this constructor if mId is not available
	 *
	 * @param tripId
	 * @param timestamp
	 * @param accX
	 * @param accY
	 * @param accZ
	 */
	public AccelerometerData(@NonNull long tripId, @NonNull long timestamp, @NonNull double accX, @NonNull double accY, @NonNull double accZ)
	{
		this(-1, tripId, timestamp, accX, accY, accZ);
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
	public double getAccX()
	{
		return accX;
	}

	@NonNull
	public double getAccY()
	{
		return accY;
	}

	@NonNull
	public double getAccZ()
	{
		return accZ;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		AccelerometerData that = (AccelerometerData) o;

		if(mId != that.mId) return false;
		if(tripId != that.tripId) return false;
		if(timestamp != that.timestamp) return false;
		if(Double.compare(that.accX, accX) != 0) return false;
		if(Double.compare(that.accY, accY) != 0) return false;
		return Double.compare(that.accZ, accZ) == 0;

	}

	@Override
	public int hashCode()
	{
		int result;
		long temp;
		result = (int) (mId ^ (mId >>> 32));
		result = 31 * result + (int) (tripId ^ (tripId >>> 32));
		result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
		temp = Double.doubleToLongBits(accX);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(accY);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(accZ);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString()
	{
		return "AccelerometerData{" +
				"mId=" + mId +
				", tripId=" + tripId +
				", timestamp=" + timestamp +
				", accX=" + accX +
				", accY=" + accY +
				", accZ=" + accZ +
				'}';
	}
}
