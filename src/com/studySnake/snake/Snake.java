/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.studySnake.snake;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Pattern;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.studySnake.snake.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Snake: a simple game that everyone can enjoy.
 * 
 * This is an implementation of the classic Game "Snake", in which you control a
 * serpent roaming around the garden looking for apples. Be careful, though,
 * because when you catch one, not only will you become longer, but you'll move
 * faster. Running into yourself or the walls will end the game.
 * 
 */
public class Snake extends Activity {

    private Uri notification = RingtoneManager
            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private SnakeView mSnakeView;
    private MediaPlayer player=null;
    private static String ICICLE_KEY = "snake-view";
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_snake_layout:
	        	onPause();
	        	return true;
	        case R.id.different_quiz:
	        	different_quiz();
	        	return true;
	        case R.id.quit:
	        	quit();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_snake_layout, menu);
        return true;
    }
    
    public void beep(int volume)
    {
        player = MediaPlayer.create(getApplicationContext(), notification);
        player.start();
    }
    public void beepOff(){
    	player.stop();
    	player.release();
    	player = null;
    }//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentCallFromOpening = getIntent();
    	String whichQuiz = intentCallFromOpening.getStringExtra("whichQuiz");
        setContentView(R.layout.snake_layout);
        mSnakeView = (SnakeView) findViewById(R.id.snake);
        ArrayList<Question> ques =(parseQuestionFromString(whichQuiz));
        Collections.shuffle(ques);
        mSnakeView.setQuestions(ques);
        mSnakeView.setWhichQuiz(whichQuiz);
        mSnakeView.setTextView((TextView) findViewById(R.id.text));
        mSnakeView.setTextView2((TextView) findViewById(R.id.mm));
        mSnakeView.setOnTouchListener(mSnakeView);
        if (savedInstanceState == null) {
            // We were just launched -- set up a new gameyz
            mSnakeView.setMode(SnakeView.READY);
        } else {
            // We are being restored
            Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
            if (map != null) {
                mSnakeView.restoreState(map);
            } else {
                mSnakeView.setMode(SnakeView.PAUSE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSnakeView.setMode(SnakeView.PAUSE);
    }

    private void different_quiz(){
    	if(player != null){
    		player.release();
    		player = null;
    	}
    	Intent intent = new Intent(getApplicationContext(), Opening.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    	
    }
    
    private void quit(){
    	if(player != null){
    		player.release();
    		player = null;
    	}
    	mSnakeView.early_end();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
    }
    public ArrayList<Question> parseQuestionFromString(String filename){
    	ArrayList<Question> thisList = new ArrayList<Question>();
		String [] eachQuestion = filename.split(" nxn ");
		for(String aQuestion: eachQuestion){
			if(aQuestion.contains(" , ")){
	           String [] bits=  aQuestion.split(" , ");
	           String query = bits[0];
	           ArrayList<String> answers = new ArrayList<String>();
	           String a1 = bits[1];
	           String a2 = bits[2];
	           String a3 = bits[3];
	           String a4 = bits[4];
	           answers.add(a1);
	           answers.add(a2);
	           answers.add(a3);
	           answers.add(a4);
	           String rightAnswer =bits[5];
	           int id = Integer.parseInt(bits[6]);
	           thisList.add(new Question(query,answers,rightAnswer,id));
			}
		}
		return thisList;
    }
    @Override
    public void onDestroy() {
    	if(player != null){
    		player.release();
    		player = null;
    	}
        super.onDestroy();
    }
}