package eu.vmpay.drivestyle.utils.dtw.distance;

/**
 * Created by Andrew on 31/01/2018.
 */

public interface DistanceFunction
{
	double calcDistance(double[] vector1, double[] vector2);
}
