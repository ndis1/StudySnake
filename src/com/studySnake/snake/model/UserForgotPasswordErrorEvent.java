package com.studySnake.snake.model;

import com.parse.ParseException;

/**
 * Event for forgot password errors. 
 * @author Trey Robinson
 *
 */
public class UserForgotPasswordErrorEvent extends ErrorEvent {

	public UserForgotPasswordErrorEvent(ParseException exception) {
		super(exception);
	}

}
