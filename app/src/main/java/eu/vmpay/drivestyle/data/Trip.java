package eu.vmpay.drivestyle.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import eu.vmpay.drivestyle.tripList.TripListFilterType;


/**
 * Created by Andrew on 25/09/2017.
 */

public final class Trip
{
	@Nullable
	private final long mId;

	@NonNull
	private final String mTitle;

	@NonNull
	private final long mStartTime;

	@NonNull
	private final long mFinishTime;

	@Nullable
	private final Double mMark;

	@NonNull
	private final String mType;

	@NonNull
	private final TripListFilterType mScenario;

	public Trip(long mId, @NonNull String mTitle, @NonNull long mStartTime, @NonNull long mFinishTime, Double mMark, @NonNull String mType, @NonNull TripListFilterType mScenario)
	{
		this.mId = mId;
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
}
