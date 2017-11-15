package eu.vmpay.drivestyle.addTrip;

import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.Nullable;

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
import eu.vmpay.drivestyle.data.LocationData;
import eu.vmpay.drivestyle.data.Trip;
import eu.vmpay.drivestyle.data.source.local.TripLocalDataSource;
import eu.vmpay.drivestyle.sensors.location.FusedLocationProviderContract;
import eu.vmpay.drivestyle.sensors.motion.AccelerometerSensorContract;
import eu.vmpay.drivestyle.tripList.TripListFilterType;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by andrew on 10/26/17.
 */
final class AddTripPresenter implements AddTripContract.Presenter, AccelerometerSensorContract.AccDataReceived, FusedLocationProviderContract.LocationData
{
	private final String TAG = "AddTripPresenter";

	private final AccelerometerSensorContract accelerometerSensor;
	private final FusedLocationProviderContract fusedLocationProvider;
	private TripLocalDataSource tripsRepository;
	@Nullable private AddTripContract.View addTripView;

	private boolean accCalibrationFinished = false;
	private HashMap<Long, Double[]> motionDataMap = new HashMap<>(), motionDataMapCopy = new HashMap<>();
	private boolean locationCalibrationFinished = false;
	private HashMap<Long, Location> locationDataMap = new HashMap<>(), locationDataMapCopy = new HashMap<>();
	private Handler recyclerHandler = new Handler();

	private int currentStep = 1;

	@Inject
	public AddTripPresenter(AccelerometerSensorContract accelerometerSensor, FusedLocationProviderContract fusedLocationProvider, TripLocalDataSource tripsRepository)
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
					recyclerHandler.postDelayed(recycleMap, 5_000);
					break;
				case 2:
					stopMotionSensor();
					stopLocationSensor();
					clearOldMotionData();
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
		tripsRepository.saveDataModelRx(trip).subscribeWith(new DisposableSubscriber<Long>()
		{
			@Override
			public void onNext(Long aLong)
			{
				final boolean[] motionSavingFinished = { false };
				final boolean[] locationSavingFinished = { false };
				final long tripId = aLong;

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

				tripsRepository.saveDataModelListRx(accelerometerDataList).subscribeWith(new DisposableSubscriber<Long>()
				{
					@Override
					public void onNext(Long aLong)
					{
					}

					@Override
					public void onError(Throwable t)
					{
					}

					@Override
					public void onComplete()
					{
						List<LocationData> locationDataList = new ArrayList<>();
						for(Map.Entry<Long, Location> entry : locationDataMapCopy.entrySet())
						{
							LocationData locationData = new LocationData(tripId, entry.getKey(), entry.getValue());
							locationDataList.add(locationData);
						}
						Collections.sort(locationDataList, new Comparator<LocationData>()
						{
							@Override
							public int compare(LocationData o1, LocationData o2)
							{
								return (int) (o1.getTimestamp() - o2.getTimestamp());
							}
						});

						tripsRepository.saveDataModelListRx(locationDataList).subscribeWith(new DisposableSubscriber<Long>()
						{
							@Override
							public void onNext(Long aLong)
							{
							}

							@Override
							public void onError(Throwable t)
							{
							}

							@Override
							public void onComplete()
							{
								if(addTripView != null)
								{
									addTripView.closeView();
								}
							}
						});
					}
				});
			}

			@Override
			public void onError(Throwable t)
			{
			}

			@Override
			public void onComplete()
			{
			}
		});
	}

	@Override
	public void startLocationSensor(Activity activity)
	{
		locationDataMap.clear();
		locationCalibrationFinished = false;
		fusedLocationProvider.requestLocation(activity, this);
	}

	@Override
	public void stopLocationSensor()
	{
		accelerometerSensor.stopSensor();
		fusedLocationProvider.stopLocationRequest();
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

	@Override
	public void onLocationDataReceived(@Nullable Location location)
	{
		if(addTripView != null && addTripView.isActive() && location != null)
		{
			if(!locationCalibrationFinished)
			{
				locationCalibrationFinished = true;
				addTripView.locationSensorCalibrated();

			}
			handleLocationData(location);
			addTripView.showLocationData(location);
		}
	}

	private void handleMotionData(double[] acceleration)
	{
		Double[] accelerationArray = new Double[acceleration.length];
		for(int i = 0; i < acceleration.length; i++)
		{
			accelerationArray[i] = acceleration[i];
		}
		motionDataMap.put(System.currentTimeMillis(), accelerationArray);
	}

	private void handleLocationData(Location location)
	{
		locationDataMap.put(System.currentTimeMillis(), location);
	}

	private Runnable recycleMap = new Runnable()
	{
		@Override
		public void run()
		{
			clearOldMotionData();
			clearOldLocationData();
			recyclerHandler.postDelayed(recycleMap, 5_000);
		}
	};

	private void clearOldLocationData()
	{
		locationDataMapCopy.putAll(locationDataMap);
		locationDataMap.clear();
		long currentTimeStamp = System.currentTimeMillis();
		Iterator<Map.Entry<Long, Location>> it = locationDataMapCopy.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Long, Location> item = it.next();
			if(currentTimeStamp - item.getKey() > 10_000)
			{
				it.remove();
			}
		}
	}

	private void clearOldMotionData()
	{
		motionDataMapCopy.putAll(motionDataMap);
		motionDataMap.clear();
		long currentTimeStamp = System.currentTimeMillis();
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
