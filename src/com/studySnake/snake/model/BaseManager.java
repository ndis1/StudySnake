package com.studySnake.snake.model;

import javax.inject.Inject;

import com.squareup.otto.Bus;



/**
 * Base manager handles bus related functions. 
 * 
 * @author Trey Robinson
 *
 */
public class BaseManager {
	@Inject Bus bus;
	protected void postEvent(Object event){
		bus.post(event);
	}
}