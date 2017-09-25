package eu.vmpay.drivestyle;

/**
 * Created by andrew on 9/25/17.
 */

public interface BasePresenter<T>
{
	/**
	 * Binds presenter with a view when resumed. The Presenter will perform initialization here.
	 *
	 * @param view the view associated with this presenter
	 */
	void takeView(T view);

	/**
	 * Drops the reference to the view when destroyed
	 */
	void dropView();
}
