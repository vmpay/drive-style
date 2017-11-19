package eu.vmpay.drivestyle.data;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.vmpay.drivestyle.data.source.local.AccelerometerDataPersistenceContract;

/**
 * Created by andrew on 10/13/17.
 */

public final class AccelerometerData extends BaseModel
{
	@NonNull private final long tripId;
	@NonNull private final long timestamp;
	@NonNull private final double accX;
	@NonNull private final double accY;
	@NonNull private final double accZ;

	public AccelerometerData()
	{
		super(-1, AccelerometerDataPersistenceContract.AccelerometerDataEntity.TABLE_NAME, AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMNS);
		this.tripId = -1;
		this.timestamp = -1;
		this.accX = -1;
		this.accY = -1;
		this.accZ = -1;
	}

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
		super(mId, AccelerometerDataPersistenceContract.AccelerometerDataEntity.TABLE_NAME, AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMNS);
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

	@Override
	public ContentValues getContentValues()
	{
		ContentValues values = new ContentValues();

		values.put(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TRIP_ID, tripId);
		values.put(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TIMESTAMP, timestamp);
		values.put(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_X, accX);
		values.put(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_Y, accY);
		values.put(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_Z, accZ);

		return values;
	}

	@NonNull
	public static List<AccelerometerData> buildFromContentValuesList(List<ContentValues> contentValuesList)
	{
		List<AccelerometerData> accelerometerDataList = new ArrayList<>();
		if(contentValuesList != null)
		{
			for(ContentValues contentValues : contentValuesList)
			{
				accelerometerDataList.add(buildFromContentValues(contentValues));
			}
		}
		return accelerometerDataList;
	}

	public static AccelerometerData buildFromContentValues(ContentValues contentValues)
	{
		long id = contentValues.getAsLong(AccelerometerDataPersistenceContract.AccelerometerDataEntity._ID);
		long mTripId = contentValues.getAsLong(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TRIP_ID);
		long timestamp = contentValues.getAsLong(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TIMESTAMP);
		double accX = contentValues.getAsDouble(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_X);
		double accY = contentValues.getAsDouble(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_Y);
		double accZ = contentValues.getAsDouble(AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_Z);

		return new AccelerometerData(id, mTripId, timestamp, accX, accY, accZ);
	}

	public static List<String[]> getExportListFromContentValuesList(List<ContentValues> contentValuesList)
	{
		List<String[]> result = new ArrayList<>();

		String[] header = new String[6];
		header[0] = AccelerometerDataPersistenceContract.AccelerometerDataEntity._ID;
		header[1] = AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TRIP_ID;
		header[2] = AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TIMESTAMP;
		header[3] = AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_X;
		header[4] = AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_Y;
		header[5] = AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_Z;

		result.add(header);

		for(ContentValues entry : contentValuesList)
		{
			result.add(getExportListFromContentValues(entry));
		}

		return result;
	}


	public static List<String[]> getExportListFromModelList(List<AccelerometerData> accelerometerDataList)
	{
		List<String[]> result = new ArrayList<>();

		String[] header = new String[6];
		header[0] = AccelerometerDataPersistenceContract.AccelerometerDataEntity._ID;
		header[1] = AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TRIP_ID;
		header[2] = AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TIMESTAMP;
		header[3] = AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_X;
		header[4] = AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_Y;
		header[5] = AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_ACC_Z;

		result.add(header);

		for(AccelerometerData entry : accelerometerDataList)
		{
			result.add(exportModel(entry));
		}

		return result;
	}

	public static String[] getExportListFromContentValues(ContentValues contentValues)
	{
		return exportModel(buildFromContentValues(contentValues));
	}

	public static String[] exportModel(AccelerometerData accelerometerData)
	{
		String[] result = new String[6];
		result[0] = Long.toString(accelerometerData.mId);
		result[1] = Long.toString(accelerometerData.tripId);
		result[2] = dateFormat.format(new Date(accelerometerData.timestamp));
		result[3] = Double.toString(accelerometerData.accX);
		result[4] = Double.toString(accelerometerData.accY);
		result[5] = Double.toString(accelerometerData.accZ);
		return result;
	}

}
