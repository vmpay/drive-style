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
	<T extends BaseModel> Flowable<Long> insertDataModelRx(@NonNull T dataModel);

	<T extends BaseModel> Flowable<Long> insertDataModelListRx(List<T> dataList);

	<T extends BaseModel> Flowable<Integer> updateDataModelRx(@NonNull T dataModel);

	<T extends BaseModel> Flowable<ContentValues> getDataModelsRx(@NonNull T dataModel);

	<T extends BaseModel> Flowable<Integer> deleteDataModelRx(@NonNull T dataModel);
}
