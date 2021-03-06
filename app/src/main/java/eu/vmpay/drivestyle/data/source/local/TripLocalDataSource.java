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
import eu.vmpay.drivestyle.data.source.TripDataSource;
import eu.vmpay.drivestyle.data.source.TripsRepositoryModule;
import eu.vmpay.drivestyle.di.AppComponent;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

	private <T extends BaseModel> long insertDataModel(@NonNull T dataModel)
	{
		checkNotNull(dataModel);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		return db.insert(dataModel.getTableName(), null, dataModel.getContentValues());
	}

	private <T extends BaseModel> int updateDataModel(@NonNull T dataModel)
	{
		checkNotNull(dataModel);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		return db.update(dataModel.getTableName(), dataModel.getContentValues(), dataModel.getWhereClause(), null);
	}

	private <T extends BaseModel> List<ContentValues> getDataContentValuesList(@NonNull T dataModel)
	{
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

		return modelList;
	}

	private <T extends BaseModel> int deleteDataModel(@NonNull T dataModel)
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		return db.delete(dataModel.getTableName(), dataModel.getWhereClause(), null);
	}

	@Override
	public <T extends BaseModel> Flowable<Long> insertDataModelRx(@NonNull final T dataModel)
	{
		return Flowable.create(new FlowableOnSubscribe<Long>()
		{
			@Override
			public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<Long> e) throws Exception
			{
				Long success = insertDataModel(dataModel);
				e.onNext(success);
				e.onComplete();
			}
		}, BackpressureStrategy.BUFFER)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public <T extends BaseModel> Flowable<Long> insertDataModelListRx(final List<T> dataList)
	{
		return Flowable.create(new FlowableOnSubscribe<Long>()
		{
			@Override
			public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<Long> e) throws Exception
			{
				for(T entry : dataList)
				{
					e.onNext(insertDataModel(entry));
				}
				e.onComplete();
			}
		}, BackpressureStrategy.BUFFER)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public <T extends BaseModel> Flowable<Integer> updateDataModelRx(@NonNull final T dataModel)
	{
		return Flowable.create(new FlowableOnSubscribe<Integer>()
		{
			@Override
			public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<Integer> e) throws Exception
			{
				Integer success = updateDataModel(dataModel);
				e.onNext(success);
				e.onComplete();
			}
		}, BackpressureStrategy.BUFFER)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public <T extends BaseModel> Flowable<ContentValues> getDataModelsRx(@NonNull final T dataModel)
	{
		checkNotNull(dataModel);
		return Flowable.create(new FlowableOnSubscribe<ContentValues>()
		{
			@Override
			public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<ContentValues> e) throws Exception
			{
				List<ContentValues> contentValuesList = getDataContentValuesList(dataModel);
				for(ContentValues entry : contentValuesList)
				{
					e.onNext(entry);
				}
				e.onComplete();
			}
		}, BackpressureStrategy.BUFFER)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public <T extends BaseModel> Flowable<Integer> deleteDataModelRx(@NonNull final T dataModel)
	{
		return Flowable.create(new FlowableOnSubscribe<Integer>()
		{
			@Override
			public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<Integer> e) throws Exception
			{
				Integer success = deleteDataModel(dataModel);
				e.onNext(success);
				e.onComplete();
			}
		}, BackpressureStrategy.BUFFER)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
