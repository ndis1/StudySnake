package com.studySnake.snake.model;

import com.parse.ParseException;


public class AuthenticateUserErrorEvent extends ErrorEvent {

	public AuthenticateUserErrorEvent(ParseException exception) {
		super(exception);
	}

}
