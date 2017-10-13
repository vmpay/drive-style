package eu.vmpay.drivestyle.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Andrew on 25/09/2017.
 */

public class TripDbHelper extends SQLiteOpenHelper
{
	public static final int DATABASE_VERSION = 1;

	public static final String DATABASE_NAME = "Trips.db";

//	private static final String SQL_CREATE_ENTRIES =
//			TripPersistenceContract.TripEntry.CREATE_TABLE;

	public TripDbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(TripPersistenceContract.TripEntry.CREATE_TABLE);
		db.execSQL(LocationDataPersistenceContract.LocationDataEntity.CREATE_TABLE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Not required as at version 1
		db.execSQL("DROP TABLE IF EXISTS " + TripPersistenceContract.TripEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Not required as at version 1
	}
}
