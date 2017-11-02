package eu.vmpay.drivestyle.addTrip;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import eu.vmpay.drivestyle.data.AccelerometerData;
import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.sensors.location.FusedLocationProviderContract;
import eu.vmpay.drivestyle.sensors.motion.AccelerometerSensorContract;
import eu.vmpay.drivestyle.tripList.TripListFilterType;

/**
 * Created by andrew on 10/26/17.
 */

final class AddTripPresenter implements AddTripContract.Presenter, AccelerometerSensorContract.AccDataReceived
{
	private final String TAG = "AddTripPresenter";

	private final AccelerometerSensorContract accelerometerSensor;
	private final FusedLocationProviderContract fusedLocationProvider;
	@Nullable
	private AddTripContract.View addTripView;

	private boolean accCalibrationFinished = false;
	private HashMap<Long, Double[]> motionDataMap = new HashMap<>();
	private Handler recyclerHandler = new Handler();

	private int currentStep = 1;

	@Inject
	public AddTripPresenter(AccelerometerSensorContract accelerometerSensor, FusedLocationProviderContract fusedLocationProvider)
	{
		this.accelerometerSensor = accelerometerSensor;
		this.fusedLocationProvider = fusedLocationProvider;
	}

	@Override
	public void takeView(AddTripContract.View view)
	{
		addTripView = view;
	}

	@Override
	public void dropView()
	{
		addTripView = null;
	}

	@Override
	public void proceed()
	{
		if(addTripView != null && addTripView.isActive())
		{
			switch(currentStep)
			{
				case 1:
					currentStep++;
					addTripView.showStep(currentStep);
					break;
				case 2:
					stopMotionSensor();
//					clearOldData();
					currentStep++;
					addTripView.showStep(currentStep);
					break;
				case 3:
					// TODO: 11/2/17 finish activity
					currentStep++;
					addTripView.showStep(currentStep);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void startMotionSensor()
	{
		motionDataMap.clear();
		accCalibrationFinished = false;
		accelerometerSensor.startSensor(this);
	}

	@Override
	public void stopMotionSensor()
	{
		recyclerHandler.removeCallbacks(recycleMap);
		accelerometerSensor.stopSensor();
	}

	@Override
	public void saveData(String tripTitle, String type, TripListFilterType scenario)
	{
		if(motionDataMap.isEmpty())
		{
			return;
		}

		long startTimestamp = motionDataMap.keySet().iterator().next();
		long stopTimestamp = motionDataMap.keySet().iterator().next();
		final Iterator<Long> iterator = motionDataMap.keySet().iterator();
		while(iterator.hasNext())
		{
			stopTimestamp = iterator.next();
		}

		Trip trip = new Trip(tripTitle.isEmpty() ? "Trip " + new Date(startTimestamp).toString() : tripTitle, startTimestamp, stopTimestamp, type, scenario);
		Log.d(TAG, trip.toString());

		for(Map.Entry<Long, Double[]> entry : motionDataMap.entrySet())
		{
			AccelerometerData accelerometerData = new AccelerometerData(-1, entry.getKey(), entry.getValue()[0], entry.getValue()[1], entry.getValue()[2]);
			Log.d(TAG, accelerometerData.toString());
		}
	}

	@Override
	public void onAccDataReceived(double[] acceleration)
	{
		if(addTripView != null && addTripView.isActive())
		{
			if(!accCalibrationFinished)
			{
				accCalibrationFinished = true;
				addTripView.motionSensorCalibrated();
//				recyclerHandler.postDelayed(recycleMap, 5_000);
			}
			if(currentStep == 2)
			{
				handleMotionData(acceleration);
				addTripView.showMotionData(acceleration);
			}
		}
	}

	private void handleMotionData(double[] acceleration)
	{
		Double[] accelerationArray = new Double[acceleration.length];
		for(int i = 0; i < acceleration.length; i++)
		{
			accelerationArray[i] = acceleration[i];
		}

		long currentTimeStamp = System.currentTimeMillis();
		Iterator<Long> iterator = motionDataMap.keySet().iterator();
		if(iterator.hasNext())
		{
			Long oldTimestamp = iterator.next();
			if(currentTimeStamp - oldTimestamp > 5_000)
			{
				motionDataMap.remove(oldTimestamp);
			}
		}
		motionDataMap.put(currentTimeStamp, accelerationArray);
	}

	private Runnable recycleMap = new Runnable()
	{
		@Override
		public void run()
		{
			clearOldData();
			recyclerHandler.postDelayed(recycleMap, 5_000);
		}
	};

	private void clearOldData()
	{
		long currentTimeStamp = System.currentTimeMillis();
		for(Long timestamp : motionDataMap.keySet())
		{
			if(currentTimeStamp - timestamp > 5_000)
			{
				motionDataMap.remove(timestamp);
			}
		}
	}
}
