package eu.vmpay.drivestyle.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by Andrew on 19/11/2017.
 */

public class MotionTripViewPersistenceContract extends BasePersistenceContract
{
	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	public MotionTripViewPersistenceContract()
	{
	}

	/* Inner class that defines the table contents */
	public static abstract class MotionTripEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "MotionTripView";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_START_TIME = "start_time";
		public static final String COLUMN_NAME_FINISH_TIME = "finish_time";
		public static final String COLUMN_NAME_MARK = "mark";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_SCENARIO = "scenario";
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
		public static final String COLUMN_NAME_ACC_X = "acc_x";
		public static final String COLUMN_NAME_ACC_Y = "acc_y";
		public static final String COLUMN_NAME_ACC_Z = "acc_z";

		public static final String CREATE_TABLE =
				"CREATE TEMP VIEW " + TABLE_NAME + " AS SELECT " +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + _ID + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_TITLE + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_START_TIME + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_FINISH_TIME + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_MARK + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_TYPE + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_SCENARIO + COMMA_SEP +

						AccelerometerDataPersistenceContract.AccelerometerDataEntity.TABLE_NAME + DOT_SEP + COLUMN_NAME_TIMESTAMP + COMMA_SEP +
						AccelerometerDataPersistenceContract.AccelerometerDataEntity.TABLE_NAME + DOT_SEP + COLUMN_NAME_ACC_X + COMMA_SEP +
						AccelerometerDataPersistenceContract.AccelerometerDataEntity.TABLE_NAME + DOT_SEP + COLUMN_NAME_ACC_Y + COMMA_SEP +
						AccelerometerDataPersistenceContract.AccelerometerDataEntity.TABLE_NAME + DOT_SEP + COLUMN_NAME_ACC_Z +
						" FROM " +
						AccelerometerDataPersistenceContract.AccelerometerDataEntity.TABLE_NAME +
						" LEFT JOIN " +
						TripPersistenceContract.TripEntry.TABLE_NAME +
						" ON " +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + _ID +
						" = " +
						AccelerometerDataPersistenceContract.AccelerometerDataEntity.TABLE_NAME + DOT_SEP +
						AccelerometerDataPersistenceContract.AccelerometerDataEntity.COLUMN_NAME_TRIP_ID;

		public static final String[] COLUMNS =
				{
						_ID,
						COLUMN_NAME_TITLE,
						COLUMN_NAME_START_TIME,
						COLUMN_NAME_FINISH_TIME,
						COLUMN_NAME_MARK,
						COLUMN_NAME_TYPE,
						COLUMN_NAME_SCENARIO,
						COLUMN_NAME_TIMESTAMP,
						COLUMN_NAME_ACC_X,
						COLUMN_NAME_ACC_Y,
						COLUMN_NAME_ACC_Z
				};
	}
}
