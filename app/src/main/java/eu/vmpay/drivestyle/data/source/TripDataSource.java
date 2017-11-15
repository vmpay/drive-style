package eu.vmpay.drivestyle.data.source;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import java.util.List;

import eu.vmpay.drivestyle.data.BaseModel;

/**
 * Created by Andrew on 25/09/2017.
 * Main entry point for accessing trips data.
 */

public interface TripDataSource
{
	<T extends BaseModel> long saveDataModel(@NonNull T dataModel);

	<T extends BaseModel> void getDataModels(@NonNull T dataModel, @NonNull LoadModelsCallback callback);

	interface LoadModelsCallback
	{
		void onModelsLoaded(List<ContentValues> contentValuesList);

		void onDataNotAvailable();
	}

	<T extends BaseModel> int deleteDataModel(@NonNull T dataModel);
}
