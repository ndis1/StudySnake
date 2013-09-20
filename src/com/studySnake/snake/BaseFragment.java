package com.studySnake.snake;


import javax.inject.Inject;


import com.squareup.otto.Bus;
import com.studySnake.snake.model.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Fragment class that resolves basic service bus requirement (Register, deregister, Event Posting)
 * 
 * @author Trey Robinson
 *
 */
public class BaseFragment extends Fragment {
	@Inject
    Bus bus;
	
	@Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        ((BaseApplication) getActivity()
                .getApplication())
                .inject(this);
    }
	
	
	@Override
	public void onResume() {
		super.onResume();
		bus.register(this);
		
	}
	
	@Override
	public void onPause() {
		/**fragment must be removed from the service bus in onPause or an error will occur
		  when the bus attempts to dispatch an event to the paused activity. **/ 
		bus.unregister(this);
		super.onPause();
	}
	
	/**
	 * Post the event to the service bus
	 * @param event
	 * 		The event to dispatch on the service bus
	 */
	protected void postEvent(Object event) {
		bus.post(event);
	}
}