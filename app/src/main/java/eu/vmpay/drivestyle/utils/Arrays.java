package eu.vmpay.drivestyle.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Andrew on 31/01/2018.
 */

public class Arrays
{
	public static int[] toPrimitiveArray(Integer[] objArr)
	{
		final int[] primArr = new int[objArr.length];
		for(int x = 0; x < objArr.length; x++)
		{
			primArr[x] = objArr[x].intValue();
		}

		return primArr;
	}

	public static int[] toIntArray(Collection c)
	{
		return Arrays.toPrimitiveArray((Integer[]) c.toArray(new Integer[0]));
	}

	public static Collection toCollection(boolean arr[])
	{
		final ArrayList collection = new ArrayList(arr.length);
		for(int x = 0; x < arr.length; x++)
		{
			collection.add(new Boolean(arr[x]));
		}

		return collection;
	}


	public static Collection toCollection(byte arr[])
	{
		final ArrayList collection = new ArrayList(arr.length);
		for(int x = 0; x < arr.length; x++)
		{
			collection.add(new Byte(arr[x]));
		}

		return collection;
	}

	public static Collection toCollection(char arr[])
	{
		final ArrayList collection = new ArrayList(arr.length);
		for(int x = 0; x < arr.length; x++)
		{
			collection.add(new Character(arr[x]));
		}

		return collection;
	}

	public static Collection toCollection(double arr[])
	{
		final ArrayList collection = new ArrayList(arr.length);
		for(int x = 0; x < arr.length; x++)
		{
			collection.add(new Double(arr[x]));
		}

		return collection;
	}


	public static Collection toCollection(float arr[])
	{
		final ArrayList collection = new ArrayList(arr.length);
		for(int x = 0; x < arr.length; x++)
		{
			collection.add(new Float(arr[x]));
		}

		return collection;
	}

	public static Collection toCollection(int arr[])
	{
		final ArrayList collection = new ArrayList(arr.length);
		for(int x = 0; x < arr.length; x++)
		{
			collection.add(new Integer(arr[x]));
		}

		return collection;
	}

	public static Collection toCollection(long arr[])
	{
		final ArrayList collection = new ArrayList(arr.length);
		for(int x = 0; x < arr.length; x++)
		{
			collection.add(new Long(arr[x]));
		}

		return collection;
	}

	public static Collection toCollection(short arr[])
	{
		final ArrayList collection = new ArrayList(arr.length);
		for(int x = 0; x < arr.length; x++)
		{
			collection.add(new Short(arr[x]));
		}

		return collection;
	}

	public static Collection toCollection(String arr[])
	{
		final ArrayList collection = new ArrayList(arr.length);
		for(int x = 0; x < arr.length; x++)
		{
			collection.add(new String(arr[x]));
		}

		return collection;
	}

	public static boolean contains(boolean arr[], boolean val)
	{
		for(int x = 0; x < arr.length; x++)
		{
			if(arr[x] == val)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean contains(byte arr[], byte val)
	{
		for(int x = 0; x < arr.length; x++)
		{
			if(arr[x] == val)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean contains(char arr[], char val)
	{
		for(int x = 0; x < arr.length; x++)
		{
			if(arr[x] == val)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean contains(double arr[], double val)
	{
		for(int x = 0; x < arr.length; x++)
		{
			if(arr[x] == val)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean contains(float arr[], float val)
	{
		for(int x = 0; x < arr.length; x++)
		{
			if(arr[x] == val)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean contains(int arr[], int val)
	{
		for(int x = 0; x < arr.length; x++)
		{
			if(arr[x] == val)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean contains(long arr[], long val)
	{
		for(int x = 0; x < arr.length; x++)
		{
			if(arr[x] == val)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean contains(short arr[], short val)
	{
		for(int x = 0; x < arr.length; x++)
		{
			if(arr[x] == val)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean contains(String arr[], String val)
	{
		for(int x = 0; x < arr.length; x++)
		{
			if(arr[x] == val)
			{
				return true;
			}
		}

		return false;
	}

	public static double[] toDouble(List<Pair<Number, Number>> input)
	{
		if(input == null)
		{
			return new double[0];
		}
		else
		{
			double[] result = new double[input.size()];
			for(int i = 0; i < result.length; i++)
			{
				result[i] = input.get(i).getRight().doubleValue();
			}
			return result;
		}
	}
}
