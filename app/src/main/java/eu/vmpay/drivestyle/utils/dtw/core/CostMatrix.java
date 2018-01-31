package eu.vmpay.drivestyle.utils.dtw.core;

/**
 * Created by Andrew on 31/01/2018.
 */

public interface CostMatrix
{
	void put(int col, int row, double value);

	double get(int col, int row);

	int size();
}
