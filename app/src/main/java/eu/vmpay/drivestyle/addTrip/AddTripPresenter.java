package eu.vmpay.drivestyle.addTrip;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import eu.vmpay.drivestyle.data.AccelerometerData;
import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.data.source.TripsRepository;
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
	private TripsRepository tripsRepository;
	@Nullable
	private AddTripContract.View addTripView;

	private boolean accCalibrationFinished = false;
	private HashMap<Long, Double[]> motionDataMap = new HashMap<>(), motionDataMapCopy = new HashMap<>();
	private Handler recyclerHandler = new Handler();

	private int currentStep = 1;

	@Inject
	public AddTripPresenter(AccelerometerSensorContract accelerometerSensor, FusedLocationProviderContract fusedLocationProvider, TripsRepository tripsRepository)
	{
		this.accelerometerSensor = accelerometerSensor;
		this.fusedLocationProvider = fusedLocationProvider;
		this.tripsRepository = tripsRepository;
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
					Log.d(TAG, "recycleMap onAccDataReceived " + System.currentTimeMillis());
					recyclerHandler.postDelayed(recycleMap, 5_000);
					break;
				case 2:
					stopMotionSensor();
					clearOldData();
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
		if(motionDataMapCopy.isEmpty())
		{
			return;
		}

		long startTimestamp = motionDataMapCopy.keySet().iterator().next();
		long stopTimestamp = motionDataMapCopy.keySet().iterator().next();
		long currentTimestamp;
		final Iterator<Long> iterator = motionDataMapCopy.keySet().iterator();
		while(iterator.hasNext())
		{
			currentTimestamp = iterator.next();
			if(stopTimestamp < currentTimestamp)
			{
				stopTimestamp = currentTimestamp;
			}
			if(startTimestamp > currentTimestamp)
			{
				startTimestamp = currentTimestamp;
			}
		}

		Trip trip = new Trip(tripTitle.isEmpty() ? "Trip " + new Date(startTimestamp).toString() : tripTitle, startTimestamp, stopTimestamp, type, scenario);
		Log.d(TAG, trip.toString() + " readings " + motionDataMapCopy.size());
		long tripId = tripsRepository.saveDataModel(trip);

		List<AccelerometerData> accelerometerDataList = new ArrayList<>();
		for(Map.Entry<Long, Double[]> entry : motionDataMapCopy.entrySet())
		{
			AccelerometerData accelerometerData = new AccelerometerData(tripId, entry.getKey(), entry.getValue()[0], entry.getValue()[1], entry.getValue()[2]);
			accelerometerDataList.add(accelerometerData);
		}
		Collections.sort(accelerometerDataList, new Comparator<AccelerometerData>()
		{
			@Override
			public int compare(AccelerometerData o1, AccelerometerData o2)
			{
				return (int) (o1.getTimestamp() - o2.getTimestamp());
			}
		});

		for(AccelerometerData entry : accelerometerDataList)
		{
			Log.d(TAG, entry.toString());
			tripsRepository.saveDataModel(entry);
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
		motionDataMapCopy.putAll(motionDataMap);
		motionDataMap.clear();
		long currentTimeStamp = System.currentTimeMillis();
		Log.d(TAG, "clearOldData " + currentTimeStamp);
		Iterator<Map.Entry<Long, Double[]>> it = motionDataMapCopy.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Long, Double[]> item = it.next();
			if(currentTimeStamp - item.getKey() > 5_000)
			{
				it.remove();
			}
		}
	}
}
