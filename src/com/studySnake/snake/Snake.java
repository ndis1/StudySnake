

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
    //global used to toggle whether the game is paused
    private boolean paused = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentCallFromOpening = getIntent();
        setContentView(R.layout.snake_layout);
        mSnakeView = (SnakeView) findViewById(R.id.snake);
        quiz =intentCallFromOpening.getParcelableExtra("whichQuiz");
        mSnakeView.setQuestions(quiz);
        mSnakeView.setTextView((TextView) findViewById(R.id.text));
        mSnakeView.setQueryDisplay((TextView) findViewById(R.id.query_display));
        mSnakeView.setAnswersDisplay((TextView) findViewById(R.id.choice_a),(TextView) findViewById(R.id.choice_b)
        		,(TextView) findViewById(R.id.choice_c),(TextView) findViewById(R.id.choice_d));
        mSnakeView.setOnTouchListener(mSnakeView);
        if (savedInstanceState == null) {
            // We were just launched -- set up a new game
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
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.pause_button:{//
	        	pauseGame();
	        	//set the title text of the action bar item here, toggling paused vs unpaused
	        	if(paused){
	        		item.setTitle("unpause");
	        	}else{
		        	item.setTitle("pause");
	        	}
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
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
    }
    
    @Override
    public void onBackPressed(){
    	//back should do nothing
    }
    
    public void pauseGame(){
    	//send the pause message to the snakeview's handler
    	if(paused){
    		Message unpause = new Message();
        	unpause.what = 6;
        	mSnakeView.mRedrawHandler.sendMessage(unpause);
        	paused= false;
    	}else{
        	//send the unpause message to the snakeview's handler
    		Message pause = new Message();
        	pause.what = 7;
        	mSnakeView.mRedrawHandler.sendMessage(pause);
        	paused = true;
    	}
    }
    
    //kill the current game and return to the opening screen
    private void different_quiz(){
    	//send a message to the handler so the action will be handled in the game and not 
    	//mess up the thread
    	Message kill = new Message();
    	kill.what = 3;
    	mSnakeView.mRedrawHandler.sendMessage(kill);
    	Intent intent = new Intent(getApplicationContext(), Opening.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    	this.finish();
    }
    
    //kill the current game and show the score report
    public void moveOnToScoreReport(){
    	Intent i = new Intent(this,ScoreReport.class);
        i.putExtra("whichQuiz", (Parcelable)mSnakeView.getQuiz());
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	//message to the handler to kill messages
    	Message kill = new Message();
    	kill.what = 3;
    	//send a message to the handler so the action will be handled in the game and not 
    	//mess up the thread
    	mSnakeView.mRedrawHandler.sendMessage(kill);
        startActivity(i);
        this.finish();
    }
    
}