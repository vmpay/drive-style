package eu.vmpay.drivestyle.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by Andrew on 25/09/2017.
 */

public final class TripPersistenceContract extends BasePersistenceContract
{
	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	private TripPersistenceContract()
	{
	}

	/* Inner class that defines the table contents */
	public static abstract class TripEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "trips";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_START_TIME = "start_time";
		public static final String COLUMN_NAME_FINISH_TIME = "finish_time";
		public static final String COLUMN_NAME_MARK = "mark";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_SCENARIO = "scenario";

		public static final String CREATE_TABLE =
				"CREATE TABLE " + TABLE_NAME + " (" +
						_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
						COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
						COLUMN_NAME_START_TIME + TEXT_TYPE + COMMA_SEP +
						COLUMN_NAME_FINISH_TIME + TEXT_TYPE + COMMA_SEP +
						COLUMN_NAME_MARK + REAL_TYPE + COMMA_SEP +
						COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
						COLUMN_NAME_SCENARIO + INTEGER_TYPE +
						" )";

		public static final String[] COLUMNS =
				{
						_ID,
						COLUMN_NAME_TITLE,
						COLUMN_NAME_START_TIME,
						COLUMN_NAME_FINISH_TIME,
						COLUMN_NAME_MARK,
						COLUMN_NAME_TYPE,
						COLUMN_NAME_SCENARIO
				};
	}
}
