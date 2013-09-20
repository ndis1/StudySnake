package com.studySnake.snake.model;

import com.parse.ParseUser;


public class AuthenticateUserSuccessEvent {

	public ParseUser user;
	public AuthenticateUserSuccessEvent(ParseUser user) {
		super();
		
		this.user = user;
	}

}
