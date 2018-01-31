package eu.vmpay.drivestyle.utils.dtw.distance;

/**
 * Created by Andrew on 31/01/2018.
 */

class ManhattanDistance implements DistanceFunction
{
	public ManhattanDistance()
	{
	}

	public double calcDistance(double[] vector1, double[] vector2)
	{
		if(vector1.length != vector2.length)
		{
			throw new InternalError("ERROR:  cannot calculate the distance "
					+ "between vectors of different sizes.");
		}

		double diffSum = 0.0;
		for(int x = 0; x < vector1.length; x++)
		{
			diffSum += Math.abs(vector1[x] - vector2[x]);
		}

		return diffSum;
	}
}
