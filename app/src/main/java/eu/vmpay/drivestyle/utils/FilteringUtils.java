package eu.vmpay.drivestyle.utils;

import android.support.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by Andrew on 11/12/2017.
 */

public class FilteringUtils
{
	/**
	 * Moving median value. movMedian(a,k) for a vector a and positive integer scalar k
	 * computes a centered moving median by sliding a window of length k along
	 * a. Each element of Y is the local median of the corresponding values of
	 * a inside the window, with Y the same size as a. When k is even, the
	 * window is centered about the current and previous elements of a. The
	 * sliding window is truncated at the endpoints where there are fewer than
	 * k elements from a to fill the window.
	 *
	 * @param a input vector
	 * @param k positive integer scalar
	 * @return
	 */
	@Nullable
	public static double[] movMedian(@Nonnull double[] a, int k)
	{
		if(k < 1)
		{
			return null;
		}
		double[] m = new double[a.length];

		return m;
	}

	/**
	 * Calculate filter values for the given data
	 * movmedian
	 *
	 * @param originalValues
	 * @param slidingWindow
	 * @param weighted
	 * @return
	 */
	public static List<Pair<Number, Number>> calculateFilter(List<Pair<Number, Number>> originalValues, int slidingWindow, boolean weighted)
	{
		// do a few checks first
		if(originalValues == null || originalValues.isEmpty())
		{
			throw new RuntimeException("No measurement values provided");
		}

		final int originalSize = originalValues.size();
		if(slidingWindow > originalSize)
		{
			throw new RuntimeException("Sliding window cannot exceed the number of measurements provided");
		}

		List<Pair<Number, Number>> calculatedValues = new ArrayList<>();
		final int loops = originalSize;
		for(int i = 0; i < loops; i++)
		{
			//iterate through the list and get the windows
			int startIndex = i - slidingWindow + 1 > 0 ? i - slidingWindow + 1 : 0;
			List<Pair<Number, Number>> window = originalValues.subList(startIndex, i + 1);
			List<Number> xValues = getXValues(window, weighted);
			List<Number> yValues = getYValues(window, weighted);

			//Calculate medians for each window and store the values
			Number x = calculateMedian(xValues);
			Number y = calculateMedian(yValues);

			//Store the calculated value
			Pair<Number, Number> value = Pair.of(x, y);
			calculatedValues.add(value);
		}
		return calculatedValues;
	}

	/**
	 * Calculates the distance for the given values
	 *
	 * @param originalValues
	 * @param estimatedValues
	 * @return
	 */
	public static Number calculateEuclideanDistance(List<Pair<Number, Number>> originalValues, List<Pair<Number, Number>> estimatedValues)
	{
		List<Number> distances = new ArrayList<>();
		int loops = estimatedValues.size();

		// find the distances for all points
		for(int i = 0; i < loops; i++)
		{
			double xOrig = originalValues.get(i).getKey().doubleValue();
			double xEst = estimatedValues.get(i).getKey().doubleValue();
			double yOrig = originalValues.get(i).getValue().doubleValue();
			double yEst = estimatedValues.get(i).getValue().doubleValue();
			double distance = Math.sqrt(Math.pow((xEst - xOrig), 2) + Math.pow((yEst - yOrig), 2));
			distances.add(distance);
		}

		//calculate the mean distance
		Number meanDistance = calculateMean(distances);
		return meanDistance;
	}

	/**
	 * Returns a list of x axis values for the given window
	 *
	 * @param window
	 * @param weighted
	 * @return
	 */
	private static List<Number> getXValues(List<Pair<Number, Number>> window, boolean weighted)
	{
		List<Number> values = new ArrayList<>();
		for(int i = 0; i < window.size(); i++)
		{
			final Number value = window.get(i).getKey();
			values.add(value);
			// Add the value as many times as the weight value dictates
			if(weighted)
			{
				for(int j = 0; j < i; j++)
				{
					values.add(value);
				}
			}
		}
		return values;
	}

	/**
	 * Returns a list of y axis values for the given window
	 *
	 * @param window
	 * @param weighted
	 * @return
	 */
	private static List<Number> getYValues(List<Pair<Number, Number>> window, boolean weighted)
	{
		List<Number> values = new ArrayList<>();
		for(int i = 0; i < window.size(); i++)
		{
			final Number value = window.get(i).getValue();
			values.add(value);
			// Add the value as many times as the weight value dictates
			if(weighted)
			{
				for(int j = 0; j < i; j++)
				{
					values.add(value);
				}
			}
		}
		return values;
	}

	/**
	 * Calculates the median for the given unsorted list of numeric values
	 *
	 * @param values
	 * @return
	 */
	private static Number calculateMedian(List<Number> values)
	{
		//Sort the collection
		Collections.sort(values, new Comparator<Number>()
		{
			@Override
			public int compare(Number o1, Number o2)
			{
				Double d1 = (o1 == null) ? Double.POSITIVE_INFINITY : o1.doubleValue();
				Double d2 = (o2 == null) ? Double.POSITIVE_INFINITY : o2.doubleValue();
				return d1.compareTo(d2);
			}
		});

		final int size = values.size();
		if(size % 2 == 1)
		{
			//Odd
			return values.get((int) Math.ceil(size / 2));
		}
		else
		{
			//Even
			return (values.get(size / 2 - 1).doubleValue() + values.get(size / 2).doubleValue()) / 2;
		}
	}

	/**
	 * Calculates the mean(average) of the given list of values
	 *
	 * @param values
	 * @return
	 */
	private static Number calculateMean(List<Number> values)
	{
		double sum = 0;
		for(Number value : values)
		{
			sum += value.doubleValue();
		}
		return sum / values.size();
	}
}
