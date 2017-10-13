package eu.vmpay.drivestyle.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by andrew on 10/13/17.
 */

public final class AccelerometerDataPersistenceContract extends BasePersistenceContract
{
	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	private AccelerometerDataPersistenceContract()
	{
	}

	/* Inner class that defines the table contents */
	public static abstract class AccelerometerDataEntity implements BaseColumns
	{
		public static final String TABLE_NAME = "accelerometer";
		public static final String COLUMN_NAME_TRIP_ID = "trip_id";
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
		public static final String COLUMN_NAME_ACC_X = "acc_x";
		public static final String COLUMN_NAME_ACC_Y = "acc_y";
		public static final String COLUMN_NAME_ACC_Z = "acc_z";

		public static final String CREATE_TABLE =
				"CREATE TABLE " + TABLE_NAME + " (" +
						_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
						COLUMN_NAME_TRIP_ID + INTEGER_TYPE + COMMA_SEP +
						COLUMN_NAME_TIMESTAMP + INTEGER_TYPE + COMMA_SEP +
						COLUMN_NAME_ACC_X + REAL_TYPE + COMMA_SEP +
						COLUMN_NAME_ACC_Y + REAL_TYPE + COMMA_SEP +
						COLUMN_NAME_ACC_Z + REAL_TYPE +
						" )";

		public static final String[] COLUMNS =
				{
						_ID,
						COLUMN_NAME_TRIP_ID,
						COLUMN_NAME_TIMESTAMP,
						COLUMN_NAME_ACC_X,
						COLUMN_NAME_ACC_Y,
						COLUMN_NAME_ACC_Z
				};
	}
}
