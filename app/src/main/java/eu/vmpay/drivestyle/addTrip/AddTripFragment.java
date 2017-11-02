package eu.vmpay.drivestyle.addTrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import eu.vmpay.drivestyle.R;
import eu.vmpay.drivestyle.di.ActivityScoped;
import eu.vmpay.drivestyle.tripList.TripListFilterType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
@ActivityScoped
public class AddTripFragment extends Fragment implements AddTripContract.View
{
	private static final String TAG = "AddTripFragment";

	@Inject
	AddTripContract.Presenter mPresenter;

	private Unbinder unbinder;
	private int currentStep = 0;

	@BindView(R.id.llFirstStep) LinearLayout llFirstStep;
	@BindView(R.id.llSecondStep) LinearLayout llSecondStep;
	@BindView(R.id.llThirdStep) LinearLayout llThirdStep;

	@BindView(R.id.pbAcc) ProgressBar pbAcc;
	@BindView(R.id.pbLocation) ProgressBar pbLocation;
	@BindView(R.id.ivAcc) ImageView ivAcc;
	@BindView(R.id.ivLocation) ImageView ivLocation;

	@BindViews({ R.id.tvAccX, R.id.tvAccY, R.id.tvAccZ }) TextView[] tvAcceleration;
	@BindView(R.id.etName) EditText etName;
	@BindViews({ R.id.rbnBrake, R.id.rbnTurn, R.id.rbnLaneChange }) RadioButton[] radioButtons;

	@BindView(R.id.tvFirst) TextView tvFirst;
	@BindView(R.id.tvSecond) TextView tvSecond;
	@BindView(R.id.tvThird) TextView tvThird;
	@BindView(R.id.btnNext) Button btnNext;


	@Inject
	public AddTripFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.add_trip_fragment, container, false);
		unbinder = ButterKnife.bind(this, rootView);

		setHasOptionsMenu(true);

		return rootView;
	}

	@Override
	public void onResume()
	{
		Log.d(TAG, "onResume");
		super.onResume();
		mPresenter.takeView(this);
		mPresenter.startMotionSensor();
	}

	@Override
	public void onPause()
	{
		Log.d(TAG, "onPause");
		mPresenter.stopMotionSensor();
		super.onPause();
	}

	@Override
	public void onDestroy()
	{
		Log.d(TAG, "onDestroy");
		mPresenter.dropView();
		unbinder.unbind();
		super.onDestroy();
	}

	@Override
	public boolean isActive()
	{
		return isAdded();
	}

	@Override
	public void showStep(int stepIndex)
	{
		checkNotNull(stepIndex);

		currentStep = stepIndex;
		clearStepper();
		switch(stepIndex)
		{
			case 1:
				llFirstStep.setVisibility(View.VISIBLE);
				tvFirst.setText(R.string.stepper_marked);
				tvFirst.setTextColor(getResources().getColor(R.color.colorAccent));
				btnNext.setText(getString(R.string.button_next));
				break;
			case 2:
				llSecondStep.setVisibility(View.VISIBLE);
				tvSecond.setText(R.string.stepper_marked);
				tvSecond.setTextColor(getResources().getColor(R.color.colorAccent));
				break;
			case 3:
				llThirdStep.setVisibility(View.VISIBLE);
				tvThird.setText(R.string.stepper_marked);
				tvThird.setTextColor(getResources().getColor(R.color.colorAccent));
				btnNext.setText(getString(R.string.button_done));
				break;
			case 4:
				TripListFilterType scenario = TripListFilterType.BRAKE;
				if(radioButtons[1].isChecked())
				{
					scenario = TripListFilterType.TURN;
				}
				if(radioButtons[2].isChecked())
				{
					scenario = TripListFilterType.LANE_CHANGE;
				}
				mPresenter.saveData(etName.getText().toString(), "OnGo", scenario);
				break;
			default:
				llFirstStep.setVisibility(View.VISIBLE);
				tvFirst.setText(R.string.stepper_marked);
				tvFirst.setTextColor(getResources().getColor(R.color.colorAccent));
				btnNext.setText(getString(R.string.button_next));
				break;
		}
	}

	@Override
	public void motionSensorCalibrated()
	{
		pbAcc.setVisibility(View.GONE);
		ivAcc.setVisibility(View.VISIBLE);
		btnNext.setEnabled(true);
	}

	@Override
	public void showMotionData(double[] acceleration)
	{
		if(currentStep == 2)
		{
			for(int i = 0; i < tvAcceleration.length; i++)
			{
				tvAcceleration[i].setText(String.format(Locale.US, "%.3f", acceleration[i]));
			}
		}
	}

	private void clearStepper()
	{
		llFirstStep.setVisibility(View.GONE);
		llSecondStep.setVisibility(View.GONE);
		llThirdStep.setVisibility(View.GONE);
		tvFirst.setText(R.string.stepper_empty);
		tvSecond.setText(R.string.stepper_empty);
		tvThird.setText(R.string.stepper_empty);
		tvFirst.setTextColor(getResources().getColor(R.color.colorGrey));
		tvSecond.setTextColor(getResources().getColor(R.color.colorGrey));
		tvThird.setTextColor(getResources().getColor(R.color.colorGrey));
	}

	@OnClick({ R.id.btnNext })
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.btnNext:
				mPresenter.proceed();
				break;
			default:
				break;
		}
	}
}
