package eu.vmpay.drivestyle.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.data.source.TripDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Andrew on 26/09/2017.
 * Concrete implementation of a data source as a db.
 */
@Singleton
public class TripLocalDataSource implements TripDataSource
{
	private TripDbHelper mDbHelper;

	@Inject
	public TripLocalDataSource(@NonNull Context context)
	{
		checkNotNull(context);
		mDbHelper = new TripDbHelper(context);
	}

	/**
	 * Note: {@link LoadTripsCallback#onDataNotAvailable()} is fired if the database doesn't exist
	 * or the table is empty.
	 */
	@Override
	public void getTrips(@NonNull LoadTripsCallback callback)
	{
		List<Trip> trips = new ArrayList<Trip>();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		String[] projection = TripPersistenceContract.TripEntry.COLUMNS;

		Cursor c = db.query(
				TripPersistenceContract.TripEntry.TABLE_NAME, projection, null, null, null, null, null);

		if(c != null && c.getCount() > 0)
		{
			while(c.moveToNext())
			{
				c.moveToFirst();
				long id = c.getLong(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry._ID));
				String title = c.getString(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_TITLE));
				long startTime = c.getLong(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_START_TIME));
				long finishTime = c.getLong(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_FINISH_TIME));
				double mark = c.getDouble(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_MARK));
				String type = c.getString(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_TYPE));
				String scenario = c.getString(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_SCENARIO));

				Trip trip = new Trip(id, title, startTime, finishTime, mark, type, scenario);
				trips.add(trip);
			}
		}
		if(c != null)
		{
			c.close();
		}

		db.close();

		if(trips.isEmpty())
		{
			// This will be called if the table is new or just empty.
			callback.onDataNotAvailable();
		}
		else
		{
			callback.onTripsLoaded(trips);
		}
	}

	/**
	 * Note: {@link GetTripCallback#onDataNotAvailable()} is fired if the {@link Trip} isn't
	 * found.
	 */
	@Override
	public void getTrip(@NonNull String tripId, @NonNull GetTripCallback callback)
	{
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		String[] projection = TripPersistenceContract.TripEntry.COLUMNS;

		String selection = TripPersistenceContract.TripEntry._ID + " LIKE ?";
		String[] selectionArgs = { tripId };

		Cursor c = db.query(
				TripPersistenceContract.TripEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

		Trip trip = null;

		if(c != null && c.getCount() > 0)
		{
			c.moveToFirst();
			long id = c.getLong(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry._ID));
			String title = c.getString(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_TITLE));
			long startTime = c.getLong(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_START_TIME));
			long finishTime = c.getLong(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_FINISH_TIME));
			double mark = c.getDouble(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_MARK));
			String type = c.getString(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_TYPE));
			String scenario = c.getString(c.getColumnIndexOrThrow(TripPersistenceContract.TripEntry.COLUMN_NAME_SCENARIO));

			trip = new Trip(id, title, startTime, finishTime, mark, type, scenario);
		}
		if(c != null)
		{
			c.close();
		}

		db.close();

		if(trip != null)
		{
			callback.onTripLoaded(trip);
		}
		else
		{
			callback.onDataNotAvailable();
		}
	}

	@Override
	public void saveTrip(@NonNull Trip trip)
	{
		checkNotNull(trip);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_TITLE, trip.getmTitle());
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_START_TIME, trip.getmStartTime());
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_FINISH_TIME, trip.getmFinishTime());
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_MARK, trip.getmMark());
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_TYPE, trip.getmType());
		values.put(TripPersistenceContract.TripEntry.COLUMN_NAME_SCENARIO, trip.getmScenario());

		db.insert(TripPersistenceContract.TripEntry.TABLE_NAME, null, values);

		db.close();
	}

	@Override
	public void refreshTrips()
	{
		// Not required because the {@link TasksRepository} handles the logic of refreshing the
		// tasks from all the available data sources.
	}

	@Override
	public void deleteAllTrips()
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		db.delete(TripPersistenceContract.TripEntry.TABLE_NAME, null, null);

		db.close();
	}

	@Override
	public void deleteTrip(@NonNull long tripId)
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		String selection = TripPersistenceContract.TripEntry._ID + " LIKE ?";
		String[] selectionArgs = { Long.toString(tripId) };

		db.delete(TripPersistenceContract.TripEntry.TABLE_NAME, selection, selectionArgs);

		db.close();
	}
}
