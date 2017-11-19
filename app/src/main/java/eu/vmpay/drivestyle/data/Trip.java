package eu.vmpay.drivestyle.data;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.vmpay.drivestyle.data.source.local.TripPersistenceContract;
import eu.vmpay.drivestyle.tripList.TripListFilterType;


/**
 * Created by Andrew on 25/09/2017.
 */

public final class Trip extends BaseModel
{
	@NonNull private final String mTitle;
	@NonNull private final long mStartTime;
	@NonNull private final long mFinishTime;
	@Nullable private final Double mMark;
	@NonNull private final String mType;
	@NonNull private final TripListFilterType mScenario;

	public Trip()
	{
		super(-1, TripPersistenceContract.TripEntry.TABLE_NAME, TripPersistenceContract.TripEntry.COLUMNS);
		this.mTitle = "";
		this.mStartTime = -1;
		this.mFinishTime = -1;
		this.mMark = -1.0;
		this.mType = "";
		this.mScenario = TripListFilterType.ALL;
	}

	public Trip(long mId, @NonNull String mTitle, @NonNull long mStartTime, @NonNull long mFinishTime, Double mMark, @NonNull String mType, @NonNull TripListFilterType mScenario)
	{
		super(mId, TripPersistenceContract.TripEntry.TABLE_NAME, TripPersistenceContract.TripEntry.COLUMNS);
		this.mTitle = mTitle;
		this.mStartTime = mStartTime;
		this.mFinishTime = mFinishTime;
		this.mMark = mMark;
		this.mType = mType;
		this.mScenario = mScenario;
	}

	/**
	 * Use this constructor to specify the mark
	 *
	 * @param mTitle
	 * @param mStartTime
	 * @param mFinishTime
	 * @param mMark
	 * @param mType
	 * @param mScenario
	 */
	public Trip(@NonNull String mTitle, @NonNull long mStartTime, @NonNull long mFinishTime, Double mMark, @NonNull String mType, @NonNull TripListFilterType mScenario)
	{
		this(-1, mTitle, mStartTime, mFinishTime, mMark, mType, mScenario);
	}

	/**
	 * Use this constructor to create a trip without a mark
	 *
	 * @param mTitle
	 * @param mStartTime
	 * @param mFinishTime
	 * @param mType
	 * @param mScenario
	 */
	public Trip(@NonNull String mTitle, @NonNull long mStartTime, @NonNull long mFinishTime, @NonNull String mType, @NonNull TripListFilterType mScenario)
	{
		this(mTitle, mStartTime, mFinishTime, 0.0, mType, mScenario);
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
	public String getmTitle()
	{
		return mTitle;
	}

	@NonNull
	public long getmStartTime()
	{
		return mStartTime;
	}

	@NonNull
	public long getmFinishTime()
	{
		return mFinishTime;
	}

	@Nullable
	public Double getmMark()
	{
		return mMark;
	}

	@NonNull
	public String getmType()
	{
		return mType;
	}

	@NonNull
	public TripListFilterType getmScenario()
	{
		return mScenario;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Trip trip = (Trip) o;

		if(mId != trip.mId) return false;
		if(mStartTime != trip.mStartTime) return false;
		if(mFinishTime != trip.mFinishTime) return false;
		if(!mTitle.equals(trip.mTitle)) return false;
		if(mMark != null ? !mMark.equals(trip.mMark) : trip.mMark != null) return false;
		if(!mType.equals(trip.mType)) return false;
		return mScenario.equals(trip.mScenario);

	}

	@Override
	public int hashCode()
	{
		int result = (int) (mId ^ (mId >>> 32));
		result = 31 * result + mTitle.hashCode();
		result = 31 * result + (int) (mStartTime ^ (mStartTime >>> 32));
		result = 31 * result + (int) (mFinishTime ^ (mFinishTime >>> 32));
		result = 31 * result + (mMark != null ? mMark.hashCode() : 0);
		result = 31 * result + mType.hashCode();
		result = 31 * result + mScenario.hashCode();
		return result;
	}

	@Override
	public String toString()
	{
		return "Trip{" +
				"mTitle='" + mTitle + '\'' +
				", mStartTime=" + mStartTime +
				", mFinishTime=" + mFinishTime +
				", mMark=" + mMark +
				", mType='" + mType + '\'' +
				", mScenario='" + mScenario + '\'' +
				'}';
	}

	@Override
	public ContentValues getContentValues()
	{
		ContentValues values = new ContentValues();

		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_TITLE, mTitle);
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_START_TIME, mStartTime);
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_FINISH_TIME, mFinishTime);
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_MARK, mMark);
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_TYPE, mType);
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_SCENARIO, mScenario.ordinal());

		return values;
	}

	@NonNull
	public static List<Trip> buildFromContentValuesList(List<ContentValues> contentValuesList)
	{
		List<Trip> tripList = new ArrayList<>();
		if(contentValuesList != null)
		{
			for(ContentValues contentValues : contentValuesList)
			{
				tripList.add(buildFromContentValues(contentValues));
			}
		}
		return tripList;
	}

	public static Trip buildFromContentValues(ContentValues contentValues)
	{
		long id = contentValues.getAsLong(TripPersistenceContract.TripEntry._ID);
		String title = contentValues.getAsString(TripPersistenceContract.TripEntry.COLUMN_NAME_TITLE);
		long startTime = contentValues.getAsLong(TripPersistenceContract.TripEntry.COLUMN_NAME_START_TIME);
		long finishTime = contentValues.getAsLong(TripPersistenceContract.TripEntry.COLUMN_NAME_FINISH_TIME);
		double mark = contentValues.getAsDouble(TripPersistenceContract.TripEntry.COLUMN_NAME_MARK);
		String type = contentValues.getAsString(TripPersistenceContract.TripEntry.COLUMN_NAME_TYPE);
		int scenario = contentValues.getAsInteger(TripPersistenceContract.TripEntry.COLUMN_NAME_SCENARIO);

		return new Trip(id, title, startTime, finishTime, mark, type, TripListFilterType.values()[scenario]);
	}

	public static List<String[]> getExportListFromContentValuesList(List<ContentValues> contentValuesList)
	{
		List<String[]> result = new ArrayList<>();

		String[] header = new String[7];
		header[0] = TripPersistenceContract.TripEntry._ID;
		header[1] = TripPersistenceContract.TripEntry.COLUMN_NAME_TITLE;
		header[2] = TripPersistenceContract.TripEntry.COLUMN_NAME_START_TIME;
		header[3] = TripPersistenceContract.TripEntry.COLUMN_NAME_FINISH_TIME;
		header[4] = TripPersistenceContract.TripEntry.COLUMN_NAME_MARK;
		header[5] = TripPersistenceContract.TripEntry.COLUMN_NAME_TYPE;
		header[6] = TripPersistenceContract.TripEntry.COLUMN_NAME_SCENARIO;

		result.add(header);

		for(ContentValues entry : contentValuesList)
		{
			result.add(getExportListFromContentValues(entry));
		}

		return result;
	}

	public static String[] getExportListFromContentValues(ContentValues contentValues)
	{
		return exportModel(buildFromContentValues(contentValues));
	}

	public static String[] exportModel(Trip trip)
	{
		String[] result = new String[7];
		result[0] = Long.toString(trip.mId);
		result[1] = trip.mTitle;
		result[2] = dateFormat.format(new Date(trip.mStartTime));
		result[3] = dateFormat.format(new Date(trip.mFinishTime));
		result[4] = Double.toString(trip.mMark);
		result[5] = trip.mType;
		result[6] = trip.mScenario.name();
		return result;
	}
}
