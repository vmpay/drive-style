package eu.vmpay.drivestyle.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by Andrew on 20/11/2017.
 */

public class LocationTripViewPersistenceContract extends BasePersistenceContract
{
	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	public LocationTripViewPersistenceContract()
	{
	}

	/* Inner class that defines the table contents */
	public static abstract class LocationTripEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "location_trip_view";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_START_TIME = "start_time";
		public static final String COLUMN_NAME_FINISH_TIME = "finish_time";
		public static final String COLUMN_NAME_MARK = "mark";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_SCENARIO = "scenario";
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
		public static final String COLUMN_NAME_LATITUDE = "latitude";
		public static final String COLUMN_NAME_LONGITUDE = "longitude";
		public static final String COLUMN_NAME_ALTITUDE = "altitude";
		public static final String COLUMN_NAME_SPEED = "speed";

		public static final String CREATE_TABLE =
				"CREATE VIEW " + TABLE_NAME + " AS SELECT " +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + _ID + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_TITLE + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_START_TIME + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_FINISH_TIME + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_MARK + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_TYPE + COMMA_SEP +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + COLUMN_NAME_SCENARIO + COMMA_SEP +

						LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME + DOT_SEP + COLUMN_NAME_TIMESTAMP + COMMA_SEP +
						LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME + DOT_SEP + COLUMN_NAME_LATITUDE + COMMA_SEP +
						LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME + DOT_SEP + COLUMN_NAME_LONGITUDE + COMMA_SEP +
						LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME + DOT_SEP + COLUMN_NAME_ALTITUDE + COMMA_SEP +
						LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME + DOT_SEP + COLUMN_NAME_SPEED +
						" FROM " +
						LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME +
						" LEFT JOIN " +
						TripPersistenceContract.TripEntry.TABLE_NAME +
						" ON " +
						TripPersistenceContract.TripEntry.TABLE_NAME + DOT_SEP + _ID +
						" = " +
						LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME + DOT_SEP +
						LocationDataPersistenceContract.LocationDataEntity.COLUMN_NAME_TRIP_ID;

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
						COLUMN_NAME_LATITUDE,
						COLUMN_NAME_LONGITUDE,
						COLUMN_NAME_ALTITUDE,
						COLUMN_NAME_SPEED
				};
	}
}
