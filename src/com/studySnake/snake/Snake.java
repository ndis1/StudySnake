

package com.studySnake.snake;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Snake extends Activity {

    
    private SnakeView mSnakeView;
    private static String ICICLE_KEY = "snake-view";
    private Quiz quiz;
    private boolean paused = false;
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.pause_button:{//
	        	pauseGame();
	        	return true;
	        }
	        case R.id.different_quiz:{
	        	different_quiz();
	        	return true;
	        }
	        case R.id.quit:{
	        	moveOnToScoreReport();
	        	return true;
	        }
	        default:{
	            return super.onOptionsItemSelected(item);
	        }
	    }
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_snake_layout, menu);
        return true;
    }
    public void pauseGame(){
    	Message pause = new Message();
    	pause.what = 7;
    	Message unpause = new Message();
    	unpause.what = 6;
    	if(paused){
        	mSnakeView.mRedrawHandler.sendMessage(unpause);
        	paused= false;
    	}else{
        	mSnakeView.mRedrawHandler.sendMessage(pause);
        	paused = true;
    	}
    }
    
    @Override
    public void onBackPressed(){
    	//back should do nothing
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentCallFromOpening = getIntent();
        setContentView(R.layout.snake_layout);
        mSnakeView = (SnakeView) findViewById(R.id.snake);
         quiz =intentCallFromOpening.getParcelableExtra("whichQuiz");
        mSnakeView.setQuestions(quiz);
        mSnakeView.setTextView((TextView) findViewById(R.id.text));
        mSnakeView.setTextView2((TextView) findViewById(R.id.query_display));
        mSnakeView.setTextView3((TextView) findViewById(R.id.choice_a),(TextView) findViewById(R.id.choice_b)
        		,(TextView) findViewById(R.id.choice_c),(TextView) findViewById(R.id.choice_d));
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
   

    private void different_quiz(){
    	
    	Message kill = new Message();
    	kill.what = 3;
    	mSnakeView.mRedrawHandler.sendMessage(kill);
    	Intent intent = new Intent(getApplicationContext(), Opening.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    	this.finish();
    }
    
    
    public void moveOnToScoreReport(){
    	
    	Intent i = new Intent(this,ScoreReport.class);
        i.putExtra("whichQuiz", (Parcelable)mSnakeView.getQuiz());
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	Message kill = new Message();
    	kill.what = 3;
    	mSnakeView.mRedrawHandler.sendMessage(kill);


        startActivity(i);
        this.finish();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
    }

   
}