package eu.vmpay.drivestyle.sensors.motion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Andrew on 22/10/2017.
 */
@Singleton
public class AccelerometerSensor implements SensorEventListener, AccelerometerSensorContract
{
	private final String TAG = "AccelerometerSensor";

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private AccDataReceived callback;

	private double[] gravity = new double[3];
	private double[] linear_acceleration = new double[3];

	@Inject
	public AccelerometerSensor(@Nullable Context mContext)
	{
		checkNotNull(mContext);
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		if(event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
		{
			return;
		}

		// In this example, alpha is calculated as t / (t + dT),
		// where t is the low-pass filter's time-constant and
		// dT is the event delivery rate.

		final double alpha = 0.8;

		// Isolate the force of gravity with the low-pass filter.
		gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
		gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

		// Remove the gravity contribution with the high-pass filter.
		linear_acceleration[0] = event.values[0] - gravity[0];
		linear_acceleration[1] = event.values[1] - gravity[1];
		linear_acceleration[2] = event.values[2] - gravity[2];

//		Log.d(TAG, String.format(Locale.US, "event \t%.2f\t%.2f\t%.2f",
//				event.values[0], event.values[1], event.values[2]));
//		Log.d(TAG, String.format(Locale.US, "filtered \t%.2f\t%.2f\t%.2f",
//				linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]));

		if(callback != null)
		{
			double[] acceleration = { event.values[0], event.values[1], event.values[2] };
			callback.onAccDataReceived(acceleration);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
	}


	@Override
	public void startSensor(AccDataReceived IAccDataReceived)
	{
		callback = IAccDataReceived;
		if(mSensorManager != null && mSensor != null)
		{
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
		}
	}

	@Override
	public void stopSensor()
	{
		if(mSensorManager != null)
		{
			mSensorManager.unregisterListener(this);
		}
	}
}
