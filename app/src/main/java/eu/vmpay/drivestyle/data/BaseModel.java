package eu.vmpay.drivestyle.data;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.Nonnull;

/**
 * Created by Andrew on 03/11/2017.
 */

public abstract class BaseModel
{
	@Nullable protected final long mId;

	@Nonnull protected final String tableName;
	@Nonnull protected final String[] projection;
	@Nullable protected String whereClause;

	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", Locale.US);

	public BaseModel(long mId, @Nonnull String tableName, @Nonnull String[] projection)
	{
		this.mId = mId;
		this.tableName = tableName;
		this.projection = projection;
	}

	public abstract ContentValues getContentValues();

	public String getTableName()
	{
		return tableName;
	}

	@Nonnull
	public String[] getProjection()
	{
		return projection;
	}

	public String getWhereClause()
	{
		return whereClause;
	}

	public void setWhereClause(@Nullable String whereClause)
	{
		this.whereClause = whereClause;
	}

	public void setThisIdWhereClause()
	{
		setWhereClause(BaseColumns._ID + " LIKE " + mId);
	}


}
