package eu.vmpay.drivestyle.utils;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.LoginManager;
import net.hockeyapp.android.Tracking;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.metrics.MetricsManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import eu.vmpay.drivestyle.R;

import static net.hockeyapp.android.utils.Util.getAppSecret;

/**
 * Created by andrew on 11/15/17.
 */

public class BugTrackingUtils
{
	private static Activity currentActivity;
	private static CrashManagerListener listener = new CrashManagerListener()
	{
		@Override
		public void onCrashesSent()
		{
			currentActivity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{

					Toast.makeText(currentActivity, currentActivity.getResources().getText(R.string.crashes_sent), Toast.LENGTH_LONG).show();
				}
			});
		}

		public boolean shouldAutoUploadCrashes()
		{
			return true;
		}

		public void onCrashesNotSent()
		{
			currentActivity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					Toast.makeText(currentActivity, currentActivity.getResources().getText(R.string.crashes_not_sent), Toast.LENGTH_LONG).show();
				}
			});
		}

		@Override
		public boolean includeDeviceData()
		{
			return true;
		}

		@Override
		public boolean includeDeviceIdentifier()
		{
			return true;
		}

		@Override
		public String getDescription()
		{
			String description = "";

			try
			{
				Process process = Runtime.getRuntime().exec("logcat -d drivestyle");
				BufferedReader bufferedReader =
						new BufferedReader(new InputStreamReader(process.getInputStream()));

				StringBuilder log = new StringBuilder();
				String line;
				while((line = bufferedReader.readLine()) != null)
				{
					log.append(line);
					log.append(System.getProperty("line.separator"));
				}
				bufferedReader.close();

				description = log.toString();
			} catch(IOException e)
			{
				e.printStackTrace();
			}

			return description;
		}
	};

	public static void registerMetricsManager(Application application)
	{
		MetricsManager.register(application);
	}

	public static void checkForCrashes(Application application, Activity activity)
	{
		currentActivity = activity;
//		CrashManager.register(application, listener);
	}

	public static void checkForUpdates(Activity activity)
	{
		//TODO: Remove this for GooglePlay builds!
		UpdateManager.register(activity);
	}

	public static void trackUsage(Activity activity, boolean work)
	{
		if(work)
		{
			Tracking.startUsage(activity);
		}
		else
		{
			Tracking.stopUsage(activity);
		}
	}

	public static void unregisterUpdateManager()
	{
		UpdateManager.unregister();
	}

	/**
	 * Adds custom event for tracking.
	 *
	 * @param event - event name. Accepted characters for tracking events are: [a-zA-Z0-9_. -].
	 *              If you use other than the accepted characters, your events will not show up
	 *              in the HockeyApp web portal.
	 */
	public static void trackEvent(String event)
	{
		MetricsManager.trackEvent(event);
	}

	public static void registerLoginManager(Activity activity)
	{
		//TODO: Remove this for GooglePlay builds!
		LoginManager.register(activity, getAppSecret(activity), LoginManager.LOGIN_MODE_ANONYMOUS);
		LoginManager.verifyLogin(activity, activity.getIntent());
	}

	public static void checkOldPackage(final Activity activity)
	{
		if(isAppInstalled("com.diploma.vmpay.driving_style", activity))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(R.string.dialog_uninstall_old_package)
					.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
							Uri packageURI = Uri.parse("package:com.diploma.vmpay.driving_style");
							Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI);
							activity.startActivity(uninstallIntent);
						}
					})
					.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{
						}
					});
			builder.show();
		}
	}

	private static boolean isAppInstalled(String uri, Activity activity)
	{
		PackageManager pm = activity.getPackageManager();
		try
		{
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			return true;
		} catch(PackageManager.NameNotFoundException e)
		{
		}

		return false;
	}
}
