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

import eu.vmpay.drivestyle.data.BaseModel;
import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.data.source.TripDataSource;
import eu.vmpay.drivestyle.data.source.TripsRepositoryModule;
import eu.vmpay.drivestyle.di.AppComponent;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Andrew on 26/09/2017.
 * Concrete implementation of a data source as a db.
 * By marking the constructor with {@code @Inject} and the class with {@code @Singleton}, Dagger
 * injects the dependencies required to create an instance of the TripsRespository (if it fails, it
 * emits a compiler error). It uses {@link TripsRepositoryModule} to do so, and the constructed
 * instance is available in {@link AppComponent}.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 */
@Singleton
public class TripLocalDataSource implements TripDataSource
{
	private TripDbHelper mDbHelper;

	/**
	 * By marking the constructor with {@code @Inject}, Dagger will try to inject the dependencies
	 * required to create an instance of the TripLocalDataSource. Because {@link TripDataSource} is an
	 * interface, we must provide to Dagger a way to build those arguments, this is done in
	 * {@link TripsRepositoryModule}.
	 * <p>
	 * When two arguments or more have the same type, we must provide to Dagger a way to
	 * differentiate them. This is done using a qualifier.
	 * <p>
	 * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
	 * with {@code @Nullable} values.
	 */
	@Inject
	public TripLocalDataSource(@NonNull Context context)
	{
		checkNotNull(context);
		mDbHelper = new TripDbHelper(context);
	}

	@Override
	public <T extends BaseModel> long saveDataModel(@NonNull T dataModel)
	{
		checkNotNull(dataModel);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		long success = db.insert(dataModel.getTableName(), null, dataModel.getContentValues());

		db.close();

		return success;
	}

	/**
	 * Note: {@link LoadModelsCallback#onDataNotAvailable()} is fired if the database doesn't exist
	 * or the table is empty.
	 */
	@Override
	public <T extends BaseModel> void getDataModels(@NonNull T dataModel, @NonNull LoadModelsCallback callback)
	{
		checkNotNull(dataModel);
		checkNotNull(callback);

		List<ContentValues> modelList = new ArrayList<>();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor c = db.query(
				dataModel.getTableName(), dataModel.getProjection(), dataModel.getWhereClause(), null, null, null, null);

		if(c != null && c.getCount() > 0)
		{
			while(c.moveToNext())
			{
				ContentValues contentValues = new ContentValues();
				for(int i = 0; i < dataModel.getProjection().length; i++)
				{
					int columnIndex = c.getColumnIndex(dataModel.getProjection()[i]);
					switch(c.getType(columnIndex))
					{
						case Cursor.FIELD_TYPE_STRING:
							String stringValue = c.getString(columnIndex);
							if(stringValue != null)
							{
								contentValues.put(dataModel.getProjection()[i], stringValue);
							}
							break;
						case Cursor.FIELD_TYPE_BLOB:
							byte[] blobValue = c.getBlob(columnIndex);
							if(blobValue != null)
							{
								contentValues.put(dataModel.getProjection()[i], blobValue);
							}
							break;
						case Cursor.FIELD_TYPE_INTEGER:
							Long intValue = c.getLong(columnIndex);
							if(intValue != null)
							{
								contentValues.put(dataModel.getProjection()[i], intValue);
							}
							break;
						case Cursor.FIELD_TYPE_FLOAT:
							Float floatValue = c.getFloat(columnIndex);
							if(floatValue != null)
							{
								contentValues.put(dataModel.getProjection()[i], floatValue);
							}
							break;
						case Cursor.FIELD_TYPE_NULL:
							contentValues.putNull(dataModel.getProjection()[i]);
							break;
					}
				}
				modelList.add(contentValues);
			}
		}

		if(c != null)
		{
			c.close();
		}

		db.close();

		if(modelList.isEmpty())
		{
			// This will be called if the table is new or just empty.
			callback.onDataNotAvailable();
		}
		else
		{
			callback.onModelsLoaded(modelList);
		}
	}

	/**
	 * Note: {@link LoadModelCallback#onDataNotAvailable()} is fired if the {@link Trip} isn't
	 * found.
	 */
	@Override
	public <T extends BaseModel> void getDataModel(@NonNull T dataModel, @NonNull LoadModelCallback callback)
	{
		checkNotNull(dataModel);
		checkNotNull(callback);

		ContentValues contentValues = null;
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor c = db.query(
				dataModel.getTableName(), dataModel.getProjection(), dataModel.getWhereClause(), null, null, null, null);


		if(c != null && c.getCount() > 0)
		{
			c.moveToFirst();
			contentValues = new ContentValues();
			for(int i = 0; i < dataModel.getProjection().length; i++)
			{
				int columnIndex = c.getColumnIndex(dataModel.getProjection()[i]);
				switch(c.getType(columnIndex))
				{
					case Cursor.FIELD_TYPE_STRING:
						String stringValue = c.getString(columnIndex);
						if(stringValue != null)
						{
							contentValues.put(dataModel.getProjection()[i], stringValue);
						}
						break;
					case Cursor.FIELD_TYPE_BLOB:
						byte[] blobValue = c.getBlob(columnIndex);
						if(blobValue != null)
						{
							contentValues.put(dataModel.getProjection()[i], blobValue);
						}
						break;
					case Cursor.FIELD_TYPE_INTEGER:
						Integer intValue = c.getInt(columnIndex);
						if(intValue != null)
						{
							contentValues.put(dataModel.getProjection()[i], intValue);
						}
						break;
					case Cursor.FIELD_TYPE_FLOAT:
						Float floatValue = c.getFloat(columnIndex);
						if(floatValue != null)
						{
							contentValues.put(dataModel.getProjection()[i], floatValue);
						}
						break;
					case Cursor.FIELD_TYPE_NULL:
						contentValues.putNull(dataModel.getProjection()[i]);
						break;
				}
			}
		}

		if(c != null)
		{
			c.close();
		}

		db.close();

		if(contentValues != null)
		{
			callback.onModelsLoaded(contentValues);
		}
		else
		{
			callback.onDataNotAvailable();
		}
	}

	//---------------------------------------------------------------TRIPS---------------------------------------------------------------

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

	//---------------------------------------------------------------LOCATION---------------------------------------------------------------

	@Override
	public void deleteAllLocations()
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		db.delete(LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME, null, null);

		db.close();
	}

	@Override
	public void deleteLocation(@NonNull long locationDataId)
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		String selection = LocationDataPersistenceContract.LocationDataEntity._ID + " LIKE ?";
		String[] selectionArgs = { Long.toString(locationDataId) };

		db.delete(LocationDataPersistenceContract.LocationDataEntity.TABLE_NAME, selection, selectionArgs);

		db.close();
	}

	//---------------------------------------------------------------ACCELEROMETER---------------------------------------------------------------

	@Override
	public void deleteAllAccelerometerDataModels()
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		db.delete(AccelerometerDataPersistenceContract.AccelerometerDataEntity.TABLE_NAME, null, null);

		db.close();
	}

	@Override
	public void deleteAccelerometerDataModel(@NonNull long accelerometerDataId)
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		String selection = AccelerometerDataPersistenceContract.AccelerometerDataEntity._ID + " LIKE ?";
		String[] selectionArgs = { Long.toString(accelerometerDataId) };

		db.delete(AccelerometerDataPersistenceContract.AccelerometerDataEntity.TABLE_NAME, selection, selectionArgs);

		db.close();
	}
}
