package eu.vmpay.drivestyle.data;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.vmpay.drivestyle.data.source.local.LocationTripViewPersistenceContract;
import eu.vmpay.drivestyle.tripList.TripListFilterType;

/**
 * Created by Andrew on 20/11/2017.
 */

public class LocationTripView extends BaseModel
{
	public LocationTripView()
	{
		super(-1, LocationTripViewPersistenceContract.LocationTripEntry.TABLE_NAME, LocationTripViewPersistenceContract.LocationTripEntry.COLUMNS);
	}

	@Override
	public ContentValues getContentValues()
	{
		return null;
	}

	public static List<String[]> getExportListFromContentValuesList(List<ContentValues> contentValuesList)
	{
		List<String[]> result = new ArrayList<>();

		String[] header = new String[12];
		header[0] = LocationTripViewPersistenceContract.LocationTripEntry._ID;
		header[1] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_TITLE;
		header[2] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_START_TIME;
		header[3] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_FINISH_TIME;
		header[4] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_MARK;
		header[5] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_TYPE;
		header[6] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_SCENARIO;
		header[7] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_TIMESTAMP;
		header[8] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_LATITUDE;
		header[9] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_LONGITUDE;
		header[10] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_ALTITUDE;
		header[11] = LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_SPEED;

		result.add(header);

		for(ContentValues entry : contentValuesList)
		{
			result.add(getExportListFromContentValues(entry));
		}

		return result;
	}

	private static String[] getExportListFromContentValues(ContentValues contentValues)
	{
		String[] result = new String[12];

		result[0] = Long.toString(contentValues.getAsLong(LocationTripViewPersistenceContract.LocationTripEntry._ID));
		result[1] = contentValues.getAsString(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_TITLE);
		result[2] = dateFormat.format(new Date(contentValues.getAsLong(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_START_TIME)));
		result[3] = dateFormat.format(new Date(contentValues.getAsLong(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_FINISH_TIME)));
		result[4] = Double.toString(contentValues.getAsDouble(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_MARK));
		result[5] = contentValues.getAsString(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_TYPE);
		result[6] = TripListFilterType.values()[contentValues.getAsInteger(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_SCENARIO)].name();
		result[7] = dateFormat.format(new Date(contentValues.getAsLong(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_TIMESTAMP)));
		result[8] = Double.toString(contentValues.getAsDouble(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_LATITUDE));
		result[9] = Double.toString(contentValues.getAsDouble(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_LONGITUDE));
		result[10] = Double.toString(contentValues.getAsDouble(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_ALTITUDE));
		result[11] = Double.toString(contentValues.getAsDouble(LocationTripViewPersistenceContract.LocationTripEntry.COLUMN_NAME_SPEED));

		return result;
	}
}
