package eu.vmpay.drivestyle.data.source.local;

/**
 * Created by andrew on 10/13/17.
 */

public abstract class BasePersistenceContract
{
	protected static final String REAL_TYPE = " INTEGER";
	protected static final String INTEGER_TYPE = " INTEGER";
	protected static final String TEXT_TYPE = " TEXT";
	protected static final String BOOLEAN_TYPE = " INTEGER";
	protected static final String COMMA_SEP = ",";
	protected static final String DOT_SEP = ".";

	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	protected BasePersistenceContract()
	{
	}
}
