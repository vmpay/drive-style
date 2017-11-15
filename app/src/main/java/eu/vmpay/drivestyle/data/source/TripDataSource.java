package eu.vmpay.drivestyle.data.source;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import java.util.List;

import eu.vmpay.drivestyle.data.BaseModel;
import io.reactivex.Flowable;

/**
 * Created by Andrew on 25/09/2017.
 * Main entry point for accessing trips data.
 */

public interface TripDataSource
{
	interface LoadModelsCallback
	{
		void onModelsLoaded(List<ContentValues> contentValuesList);

		void onDataNotAvailable();
	}

	<T extends BaseModel> void getDataModels(@NonNull T dataModel, @NonNull LoadModelsCallback callback);

	<T extends BaseModel> Flowable<Long> saveDataModelRx(@NonNull T dataModel);

	<T extends BaseModel> Flowable<Long> saveDataModelListRx(List<T> dataList);

	<T extends BaseModel> Flowable<ContentValues> getDataModelsRx(@NonNull T dataModel);

	<T extends BaseModel> Flowable<Integer> deleteDataModelRx(@NonNull T dataModel);
}
