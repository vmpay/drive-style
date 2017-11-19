package eu.vmpay.drivestyle.data;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.vmpay.drivestyle.data.source.local.MotionTripViewPersistenceContract;
import eu.vmpay.drivestyle.tripList.TripListFilterType;

/**
 * Created by Andrew on 19/11/2017.
 */

public class MotionTripView extends BaseModel
{

	public MotionTripView()
	{
		super(-1, MotionTripViewPersistenceContract.MotionTripEntry.TABLE_NAME, MotionTripViewPersistenceContract.MotionTripEntry.COLUMNS);

	}

	@Override
	public ContentValues getContentValues()
	{
		return null;
	}

	public static List<String[]> getExportListFromContentValuesList(List<ContentValues> contentValuesList)
	{
		List<String[]> result = new ArrayList<>();

		String[] header = new String[11];
		header[0] = MotionTripViewPersistenceContract.MotionTripEntry._ID;
		header[1] = MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_TITLE;
		header[2] = MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_START_TIME;
		header[3] = MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_FINISH_TIME;
		header[4] = MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_MARK;
		header[5] = MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_TYPE;
		header[6] = MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_SCENARIO;
		header[7] = MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_TIMESTAMP;
		header[8] = MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_ACC_X;
		header[9] = MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_ACC_Y;
		header[10] = MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_ACC_Z;

		result.add(header);

		for(ContentValues entry : contentValuesList)
		{
			result.add(getExportListFromContentValues(entry));
		}

		return result;
	}

	private static String[] getExportListFromContentValues(ContentValues contentValues)
	{
		String[] result = new String[11];

		result[0] = Long.toString(contentValues.getAsLong(MotionTripViewPersistenceContract.MotionTripEntry._ID));
		result[1] = contentValues.getAsString(MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_TITLE);
		result[2] = dateFormat.format(new Date(contentValues.getAsLong(MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_START_TIME)));
		result[3] = dateFormat.format(new Date(contentValues.getAsLong(MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_FINISH_TIME)));
		result[4] = Double.toString(contentValues.getAsDouble(MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_MARK));
		result[5] = contentValues.getAsString(MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_TYPE);
		result[6] = TripListFilterType.values()[contentValues.getAsInteger(MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_SCENARIO)].name();
		result[7] = dateFormat.format(new Date(contentValues.getAsLong(MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_TIMESTAMP)));
		result[8] = Double.toString(contentValues.getAsDouble(MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_ACC_X));
		result[9] = Double.toString(contentValues.getAsDouble(MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_ACC_Y));
		result[10] = Double.toString(contentValues.getAsDouble(MotionTripViewPersistenceContract.MotionTripEntry.COLUMN_NAME_ACC_Z));

		return result;
	}
}
