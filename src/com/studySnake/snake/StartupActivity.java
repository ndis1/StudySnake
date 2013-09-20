package com.studySnake.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseUser;


public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        Parse.initialize(this, "yj63popR3RaYk7pDxYj7QVeyO7KXgVcMgV09oe7d", "SB2X2Kh7JQv8Mgf0TkKBU0sMm7zu1rabHVwTrUCc"); 
    	Intent intent;
    	if(ParseUser.getCurrentUser() == null){
    	  intent = new Intent(this, Login.class);
    	}else{
    	  intent = new Intent(this, Opening.class);
    	}
    	startActivity(intent);
    	this.finish();
    }
}