package eu.vmpay.drivestyle.addTrip;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import eu.vmpay.drivestyle.R;
import eu.vmpay.drivestyle.di.ActivityScoped;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
@ActivityScoped
public class AddTripFragment extends Fragment implements AddTripContract.View
{
	@Inject
	AddTripContract.Presenter mPresenter;

	private Unbinder unbinder;

	@BindView(R.id.llFirstStep)
	LinearLayout llFirstStep;
	@BindView(R.id.llSecondStep)
	LinearLayout llSecondStep;
	@BindView(R.id.llThirdStep)
	LinearLayout llThirdStep;
	@BindView(R.id.pbAcc)
	ProgressBar pbAcc;
	@BindView(R.id.pbLocation)
	ProgressBar pbLocation;
	@BindView(R.id.ivAcc)
	ImageView ivAcc;
	@BindView(R.id.ivLocation)
	ImageView ivLocation;
	@BindView(R.id.tvFirst)
	TextView tvFirst;
	@BindView(R.id.tvSecond)
	TextView tvSecond;
	@BindView(R.id.tvThird)
	TextView tvThird;

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
		super.onResume();
		mPresenter.takeView(this);
	}

	@Override
	public void onDestroy()
	{
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

		clearStepper();
		switch(stepIndex)
		{
			case 1:
				llFirstStep.setVisibility(View.VISIBLE);
				tvFirst.setText(R.string.stepper_marked);
				tvFirst.setTextColor(getResources().getColor(R.color.colorAccent));
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
				break;
			default:
				llFirstStep.setVisibility(View.VISIBLE);
				tvFirst.setText(R.string.stepper_marked);
				tvFirst.setTextColor(getResources().getColor(R.color.colorAccent));
				break;
		}
	}

	private void clearStepper()
	{
		llFirstStep.setVisibility(View.GONE);
		llSecondStep.setVisibility(View.GONE);
		llFirstStep.setVisibility(View.GONE);
		tvFirst.setText(R.string.stepper_empty);
		tvSecond.setText(R.string.stepper_empty);
		tvThird.setText(R.string.stepper_empty);
		tvFirst.setTextColor(getResources().getColor(R.color.colorGrey));
		tvSecond.setTextColor(getResources().getColor(R.color.colorGrey));
		tvThird.setTextColor(getResources().getColor(R.color.colorGrey));
	}

	@OnClick({ R.id.fab })
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.fab:
				Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
				break;
			default:
				break;
		}
	}
}
