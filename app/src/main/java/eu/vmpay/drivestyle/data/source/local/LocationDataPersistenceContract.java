package eu.vmpay.drivestyle.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by andrew on 10/13/17.
 */

public final class LocationDataPersistenceContract extends BasePersistenceContract
{
	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	private LocationDataPersistenceContract()
	{
	}

	/* Inner class that defines the table contents */
	public static abstract class LocationDataEntity implements BaseColumns
	{
		public static final String TABLE_NAME = "location";
		public static final String COLUMN_NAME_TRIP_ID = "trip_id";
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
		public static final String COLUMN_NAME_LATITUDE = "latitude";
		public static final String COLUMN_NAME_LONGITUDE = "longitude";
		public static final String COLUMN_NAME_ALTITUDE = "altitude";
		public static final String COLUMN_NAME_SPEED = "speed";

		public static final String CREATE_TABLE =
				"CREATE TABLE " + TABLE_NAME + " (" +
						_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
						COLUMN_NAME_TRIP_ID + INTEGER_TYPE + COMMA_SEP +
						COLUMN_NAME_TIMESTAMP + INTEGER_TYPE + COMMA_SEP +
						COLUMN_NAME_LATITUDE + REAL_TYPE + COMMA_SEP +
						COLUMN_NAME_LONGITUDE + REAL_TYPE + COMMA_SEP +
						COLUMN_NAME_ALTITUDE + REAL_TYPE + COMMA_SEP +
						COLUMN_NAME_SPEED + REAL_TYPE +
						" )";

		public static final String[] COLUMNS =
				{
						_ID,
						COLUMN_NAME_TRIP_ID,
						COLUMN_NAME_TIMESTAMP,
						COLUMN_NAME_LATITUDE,
						COLUMN_NAME_LONGITUDE,
						COLUMN_NAME_ALTITUDE,
						COLUMN_NAME_SPEED
				};
	}
}
