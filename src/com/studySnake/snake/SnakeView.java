
package com.studySnake.snake;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.studySnake.snake.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SnakeView extends TileView implements OnTouchListener{
	private boolean resetApples;
	private static ArrayList<Question> questions;
	private Quiz quizz;

    private static final String TAG = "SnakeView";
    private int whichRound=0;
    private String que;

    private boolean redOn = false;
    private boolean greenOn = false;

    static final String logTag = "ActivitySwipeDetector";
    static final int MIN_DISTANCEH = 30;
    static final int MIN_DISTANCEV = 20;

    private float downX, downY, upX, upY;
    ArrayList<LtrCoordinate> alreadyAdded = new ArrayList<LtrCoordinate>();

    /**
     * Current mode of application: READY to run, RUNNING, or you have already
     * lost. static final ints are used instead of an enum for performance
     * reasons.
     */
    private int mMode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;
    /**
     * Current direction the snake is headed.
     */
    private int mDirection = NORTH;
    private int mNextDirection = NORTH;
    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;
    private static final int WEST = 4;

    /**
     * Labels for the drawables that will be loaded into the TileView class
     */
    private static final int RED_STAR = 1;
    private static final int YELLOW_STAR = 2;
    private static final int GREEN_STAR = 3;
    private static final int A_IMG = 4;
    private static final int B_IMG = 5;
    private static final int C_IMG = 6;
    private static final int D_IMG = 7;
    private Integer ansCt = 0;

    /**
     * mScore: used to track the number of apples captured mMoveDelay: number of
     * milliseconds between snake movements. This will decrease as apples are
     * captured.
     */
    private long mScore = 0;
    private static long mMoveDelay = 450;
    private int numberOfNextAnswers = 4;
    /**
     * mLastMove: tracks the absolute time when the snake last moved, and is used
     * to determine if a move should be made based on mMoveDelay.
     */
    private long mLastMove;
    
    /**
     * mStatusText: text shows to the user in some run states
     */
    private TextView mStatusText;
    private TextView questionDisp;
    private TextView AanswerDisp;
    private TextView BanswerDisp;
    private TextView CanswerDisp;
    private TextView DanswerDisp;


    /**
     * mSnakeTrail: a list of Coordinates that make up the snake's body
     * mAppleList: the secret location of the juicy apples the snake craves.
     */
    private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>();
    private ArrayList<LtrCoordinate> mAppleList = new ArrayList<LtrCoordinate>();

    /**
     * Everyone needs a little randomness in their life
     */
    private static final Random RNG = new Random();

    /**
     * Create a simple handler that we can use to cause animation to happen.  We
     * set ourselves as a target and we can use the sleep()
     * function to cause an update/invalidate to occur at a later date.
     */
    public RefreshHandler mRedrawHandler = new RefreshHandler(SnakeView.this);

   static class RefreshHandler extends Handler {
	   private final WeakReference<SnakeView> snWeak;

	   boolean paused = false;
	   RefreshHandler(SnakeView con){
		   
		   snWeak= new WeakReference<SnakeView>(con);
	   }
	   
        @Override
        public void handleMessage(Message msg) {
        	SnakeView sn = snWeak.get();
        	if(sn != null){
	        	if(msg.what == 3){
	        		this.removeMessages(0);
	        	}else{
		        	if(msg.what == 7){
		        		paused = true;
		        	}
		        	if(msg.what == 6){
		        		paused = false;
		        	}
		        	if(!paused){
		        		
		        		sn.update();
		        		sn.invalidate();
		        	}else{
		        		this.sleep(mMoveDelay);
		        	}
	        	}
        	}
        }

        public void sleep(long delayMillis) {
        	this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };


    /**
     * Constructs a SnakeView based on inflation from XML
     * 
     * @param context
     * @param attrs
     */
    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSnakeView();
   }

    public SnakeView(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    	initSnakeView();
    }
	
	public boolean onRightToLeftSwipe(){   
	    if (mDirection != EAST) {
	        mNextDirection = WEST;
	    }
	    return true;
	}
	public boolean onLeftToRightSwipe(){   
	    if (mDirection != WEST) {
	        mNextDirection = EAST;
	    }
	    return true;
	}
	public boolean onTopToBottomSwipe(){ 
	
	    if (mDirection != NORTH) {
	        mNextDirection = SOUTH;
	    }
	    return true;
	}
	public boolean onBottomToTopSwipe(){
	    if (mMode == READY | mMode == LOSE) {
	        // At the beginning of the game, or the end of a previous one,
	        // we should start a new game.
	        initNewGame();
	        setMode(RUNNING);
	        update();
	        resetApples=true;
	        return true;
	    }
	
	    if (mMode == PAUSE) {
	        // If the game is merely paused, we should just continue where
	        // we left off.
	        setMode(RUNNING);
	        update();
	        return true;
	    }
	
	    if (mDirection != SOUTH) {
	        mNextDirection = NORTH;
	    }
	    return true;
	
	}
    private void initSnakeView() {

        setFocusable(true);

        Resources r = this.getContext().getResources();
        
        resetTiles(9);
        loadTile(RED_STAR, r.getDrawable(R.drawable.redstar));
        loadTile(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
        loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
        loadTile(A_IMG, r.getDrawable(R.drawable.aim));
        loadTile(B_IMG, r.getDrawable(R.drawable.bim));
        loadTile(C_IMG, r.getDrawable(R.drawable.cim));
        loadTile(D_IMG, r.getDrawable(R.drawable.dim));

    }
    

    private void initNewGame() {
        mSnakeTrail.clear();
        mAppleList.clear();
        resetApples=true;
        Question thisQue = questions.get(whichRound );
         que = thisQue.getQuery();
        
        // For now we're just going to load up a short default eastbound snake
        // that's just turned north

        
        mSnakeTrail.add(new Coordinate(7, 7));
        mSnakeTrail.add(new Coordinate(6, 7));
        mSnakeTrail.add(new Coordinate(5, 7));
        mSnakeTrail.add(new Coordinate(4, 7));
        mSnakeTrail.add(new Coordinate(3, 7));
        mSnakeTrail.add(new Coordinate(2, 7));
        mNextDirection = NORTH;
        
        resetApples=true;
        alreadyAdded.clear();
        mScore = 0;
    
    }


    /**
     * Given a ArrayList of coordinates, we need to flatten them into an array of
     * ints before we can stuff them into a map for flattening and storage.
     * 
     * @param cvec : a ArrayList of Coordinate objects
     * @return : a simple array containing the x/y values of the coordinates
     * as [x1,y1,x2,y2,x3,y3...]
     */
    private int[] coordArrayListToArray(ArrayList<Coordinate> cvec) {
        int count = cvec.size();
        int[] rawArray = new int[count * 2];
        for (int index = 0; index < count; index++) {
            Coordinate c = cvec.get(index);
            rawArray[2 * index] = c.x;
            rawArray[2 * index + 1] = c.y;
        }
        return rawArray;
    }
    private int[] ltrCoordArrayListToArray(ArrayList<LtrCoordinate> cvec) {
        int count = cvec.size();
        int[] rawArray = new int[count * 2];
        for (int index = 0; index < count; index++) {
            Coordinate c = cvec.get(index);
            rawArray[2 * index] = c.x;
            rawArray[2 * index + 1] = c.y;
        }
        return rawArray;
    }

    /**
     * Save game state so that the user does not lose anything
     * if the game process is killed while we are in the 
     * background.
     * 
     * @return a Bundle with this view's state
     */
    public Bundle saveState() {
        Bundle map = new Bundle();

        map.putIntArray("mAppleList", ltrCoordArrayListToArray(mAppleList));
        map.putInt("mDirection", Integer.valueOf(mDirection));
        map.putInt("mNextDirection", Integer.valueOf(mNextDirection));
        map.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
        map.putLong("mScore", Long.valueOf(mScore));
        map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));

        return map;
        
        
    }

    /**
     * Given a flattened array of ordinate pairs, we reconstitute them into a
     * ArrayList of Coordinate objects
     * 
     * @param rawArray : [x1,y1,x2,y2,...]
     * @return a ArrayList of Coordinates
     */
    private ArrayList<Coordinate> coordArrayToArrayList(int[] rawArray) {
        ArrayList<Coordinate> coordArrayList = new ArrayList<Coordinate>();

        int coordCount = rawArray.length;
        for (int index = 0; index < coordCount; index += 2) {
            Coordinate c = new Coordinate(rawArray[index], rawArray[index + 1]);
            coordArrayList.add(c);
        }
        return coordArrayList;
    }
    private ArrayList<LtrCoordinate> ltrCoordArrayToArrayList(int[] rawArray) {
        ArrayList<LtrCoordinate> coordArrayList = new ArrayList<LtrCoordinate>();

        int coordCount = rawArray.length;
        for (int index = 0; index < coordCount; index += 2) {
            LtrCoordinate c = new LtrCoordinate(rawArray[index], rawArray[index + 1], ansCt);
            ansCt = (ansCt + 1) % numberOfNextAnswers;
            coordArrayList.add(c);
        }
        return coordArrayList;
    }

    /**
     * Restore game state if our process is being relaunched
     * 
     * @param icicle a Bundle containing the game state
     */
    public void restoreState(Bundle icicle) {
        setMode(PAUSE);

        mAppleList = ltrCoordArrayToArrayList(icicle.getIntArray("mAppleList"));
        mDirection = icicle.getInt("mDirection");
        mNextDirection = icicle.getInt("mNextDirection");
        mMoveDelay = icicle.getLong("mMoveDelay");
        mScore = icicle.getLong("mScore");
        mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail"));
    }

    /*
     * handles key events in the game. Update the direction our snake is traveling
     * based on the DPAD. Ignore events that would cause the snake to immediately
     * turn back on itself.
     * 
     * (non-Javadoc)
     * 
     * @see android.view.View#onKeyDown(int, android.os.KeyEvent)
     */
    
    /**
     * Sets the TextView that will be used to give information (such as "Game
     * Over" to the user.
     * 
     * @param newView
     */
    public void setTextView(TextView newView) {
        mStatusText = newView;
    }
    public void setQueryDisplay(TextView newView){
        questionDisp = newView;
        questionDisp.setLines(3);

    }
    
    public void setAnswersDisplay(TextView A,TextView B,TextView C,TextView D){
        AanswerDisp = A;
        AanswerDisp.setLines(1);
        BanswerDisp = B;
        BanswerDisp.setLines(1);
        CanswerDisp = C;
        CanswerDisp.setLines(1);
        DanswerDisp = D;
        DanswerDisp.setLines(1);

    }
    
    /* sets the list of questions
     * 
     * 
     */
    public void setQuestions(Quiz q){
    	quizz = q;
    	ArrayList<Question> fromQuiz = q.getQuestions();
    	Collections.shuffle(fromQuiz);
    	questions = fromQuiz;
    	whichRound =0;
    }
    
    
    public Quiz getQuiz(){
    	return quizz;
    }
    
    /**
     * Updates the current mode of the application (RUNNING or PAUSED or the like)
     * as well as sets the visibility of textview for notification
     * 
     * @param newMode
     */
    public void setMode(int newMode) {
        int oldMode = mMode;
        mMode = newMode;

        if (newMode == RUNNING & oldMode != RUNNING) {
            mStatusText.setVisibility(View.INVISIBLE);
            questionDisp.setVisibility(VISIBLE);
            AanswerDisp.setVisibility(VISIBLE);
            BanswerDisp.setVisibility(VISIBLE);
            CanswerDisp.setVisibility(VISIBLE);
            DanswerDisp.setVisibility(VISIBLE);

            update();
            return;
        }

        Resources res = getContext().getResources();
        CharSequence str = "";
        if (newMode == PAUSE) {
            str = res.getText(R.string.mode_pause);
        }
        if (newMode == READY) {
            str = res.getText(R.string.mode_ready);
        }
        if (newMode == LOSE) {
         	resetApples=true;

            
            ((Snake)this.getContext()).moveOnToScoreReport();
            
        }

        mStatusText.setText(str);
        mStatusText.setVisibility(View.VISIBLE);
    }

    /**
     * Selects a random location within the garden that is not currently covered
     * by the snake. Currently _could_ go into an infinite loop if the snake
     * currently fills the garden, but we'll leave discovery of this prize to a
     * truly excellent snake-player.
     * 
     */
    
    private void addRandomApple() {
        LtrCoordinate newCoord = null;
        boolean found = false;
        while (!found) {
            // Choose a new location for our apple
            int newX = 1 + RNG.nextInt(mXTileCount - 5);
            int newY = 1 + RNG.nextInt(mYTileCount - 5);
            newCoord = new LtrCoordinate(newX, newY, ansCt);
            // Make sure it's not already under the snake
            boolean collision = false;
            int snakelength = mSnakeTrail.size();
            for (int index = 0; index < snakelength; index++) {
            	Coordinate trailCoord = mSnakeTrail.get(index);
            	int x = trailCoord.x;
            	int y = trailCoord.y;
                if (trailCoord.equals(newCoord) || (Math.abs(newX-x) < 3 &&  Math.abs(newY-y) <3)) {
                    collision = true;
                }
            }
            for (int index = 0; index < alreadyAdded.size(); index++) {
                if (alreadyAdded.get(index).x==newCoord.x ) {
                    collision = true;
                }
            }
            // if we're here and there's been no collision, then we have
            // a good location for an apple. Otherwise, we'll circle back
            // and try again
            found = !collision;
        }
        alreadyAdded.add(newCoord);

        if (newCoord == null) {
            Log.e(TAG, "Somehow ended up with a null newCoord!");
        }
        mAppleList.add((LtrCoordinate) newCoord);
        ansCt = (ansCt + 1) % numberOfNextAnswers;
    }


    /**
     * Handles the basic update loop, checking to see if we are in the running
     * state, determining if a move should be made, updating the snake's location.
     */
    public int getMode(){
    	return mMode;
    }
    public void update() {
        Log.wtf("UPDATE CALLED", "WWWHAT");

        if (mMode == RUNNING) {
            long now = System.currentTimeMillis();
            //in case update is called an extra time 
            if(whichRound >= questions.size()){
            	whichRound = 0;
            }
            Log.wtf("SNAKEVIEW WHICHR",whichRound+"");
            Question thisQue = questions.get(whichRound);

            if (now - mLastMove > mMoveDelay) {
                clearTiles();
                updateWalls();
             if(redOn){
          	   redCoverOff();
          	   redOn = false;
             }
             if(greenOn){
          	   greenCoverOff();
          	   greenOn = false;
             }
              updateSnake();
              mLastMove = now;//

                ArrayList<String> thisAns = thisQue.getAnswers();
                que = thisQue.getQuery();
                String A, B, C, D;
                A = B = C = D = "";
                for(int sz = 0 ; sz < thisAns.size() ; sz++){
                	switch(sz){
	                	 case 0 :{ A = "A : " + thisAns.get(sz);
	                	 	break;
	                	 }
	                	 case 1 :{ B = "B : " +  thisAns.get(sz);
	                	 break;
	                	 
	                	 }
	                	 case 2 :{ C = "C : " +   thisAns.get(sz);
	                	 break;
	                	 
	                	 }
	                	 case 3 :{ D = "D : " +   thisAns.get(sz);
	                	 break;
	                	 
	                	 }
                	 }
                 }
            	 String qdisp = que;
                 
                  questionDisp.setText(""+qdisp);
                  AanswerDisp.setText(A);
                  BanswerDisp.setText(B);
                  CanswerDisp.setText(C);
                  DanswerDisp.setText(D);

                
            }
            if(resetApples){
                alreadyAdded.clear();
                mAppleList.clear();
                for(int i = 0 ; i < numberOfNextAnswers ; i++){//
                addRandomApple();//
                }
                resetApples=false;
        	}else{
                updateApples();
        	}
            mRedrawHandler.sleep(mMoveDelay);
        }
    }

    /**
     * Draws some walls.
     * 
     */
    private void updateWalls() {
        for (int x = 0; x < mXTileCount; x++) {
            setTile(GREEN_STAR, x, 0);
            setTile(GREEN_STAR, x, mYTileCount - 1);
        }
        for (int y = 1; y < mYTileCount - 1; y++) {
            setTile(GREEN_STAR, 0, y);
            setTile(GREEN_STAR, mXTileCount - 1, y);
        }
    }

    /**
     * Draws some apples.
     * 
     */
    private void updateApples() {
    	
        for (LtrCoordinate c : mAppleList) {
        	
        	if(c.Ltr == 0){
        		setTile(A_IMG, c.x, c.y);
        	}else if(c.Ltr == 1){
        		setTile(B_IMG, c.x, c.y);
        	} else if(c.Ltr == 2){
        		setTile(C_IMG, c.x, c.y);
        	}else if(c.Ltr == 3){
        		setTile(D_IMG, c.x, c.y);
        	}
        }
    }
//
    /**
     * Figure out which way the snake is going, see if he's run into anything (the
     * walls, himself, or an apple). If he's not going to die, we then add to the
     * front and subtract from the rear in order to simulate motion. If we want to
     * grow him, we don't subtract from the rear.
     * 
     */
    private void updateSnake() {
        boolean growSnake = false;
        Coordinate head = mSnakeTrail.get(0);
        Coordinate newHead = new Coordinate(1, 1);
        mDirection = mNextDirection;
        switch (mDirection) {
	        case EAST: {
	            newHead = new Coordinate(head.x + 1, head.y);
	            break;
	        }
	        case WEST: {
	            newHead = new Coordinate(head.x - 1, head.y);
	            break;
	        }
	        case NORTH: {
	            newHead = new Coordinate(head.x, head.y - 1);
	            break;
	        }
	        case SOUTH: {
	            newHead = new Coordinate(head.x, head.y + 1);
	            break;
	        }
        }
        // Collision detection
        // For now we have a 1-square wall around the entire arena
        if ((newHead.x < 1) || (newHead.y < 1) || (newHead.x > mXTileCount - 2)
                || (newHead.y > mYTileCount - 2)) {
        	resetToStartSnake();
            return;
        }
        // Look for collisions with itself
        int snakelength = mSnakeTrail.size();
        for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
            Coordinate c = mSnakeTrail.get(snakeindex);
            if (c.equals(newHead)) {
            	resetToStartSnake();
                return;
            }
        }
        // Look for apples//
        int applecount = mAppleList.size();
      	boolean resetToStartSn = false;
        for (int appleindex = 0; appleindex < applecount; appleindex++) {
            Coordinate c = mAppleList.get(appleindex);
            LtrCoordinate lc = mAppleList.get(appleindex);
            int lcVal = lc.Ltr;
            if (c.equals(newHead)) {
            	resetApples = true;
                Question preQue = questions.get(whichRound);
                ArrayList<String> preAns = preQue.getAnswers();
                String corAnsStr = preQue.getCorrectAnswer();//
                String preAnsLcVal  = preAns.get(lcVal);
                whichRound++;
            	if(preAnsLcVal.trim().equals(corAnsStr.trim())){
            		greenCoverOn();
            		greenOn = true;
                 	mScore= mScore+1;
                    mMoveDelay *= 0.9;
                    growSnake = true;
                    preQue.setDone(true);
                    quizz.addTryRecord(5-numberOfNextAnswers, preQue.getId());
                    if(whichRound==questions.size()){
                       whichRound =0;
                       resetApples=true;
                    
                    ((Snake)this.getContext()).moveOnToScoreReport();
                    }
                 
                 }else{
                	 redCoverOn();
                	 redOn = true;
                	 resetToStartSn = true;
                	 preAns.remove(preAnsLcVal);
                	 quizz.setNumberOfQuestions(quizz.getNumberOfQuestions()+1);
                     Question preQueCopy = new Question(preQue.getQuery(),preQue.getAnswers(), preQue.getAnswers2(),preQue.getCorrectAnswer(),quizz.getNumberOfQuestions());
                     preQueCopy.setMyAnswers(preAns);
                	 questions.add(preQueCopy);
                 }
                	numberOfNextAnswers = questions.get(whichRound).getAnswers().size();
            	 if(whichRound==questions.size()){
                    whichRound =0;
                    ((Snake)this.getContext()).moveOnToScoreReport();

                 }
                
            	 break;
            }
        }
        if(resetToStartSn){
       	 resetToStartSnake();
	    }else{
	        // push a new head onto the ArrayList and pull off the tail
	        mSnakeTrail.add(0, newHead);
	        // except if we want the snake to grow
	        if (!growSnake) {
	            mSnakeTrail.remove(mSnakeTrail.size() - 1);
	        }
	        int index = 0;
	        for (Coordinate c : mSnakeTrail) {
	            if (index == 0) {
	                setTile(YELLOW_STAR, c.x, c.y);
	            } else {
	                setTile(RED_STAR, c.x, c.y);
	            }
	            index++;
	        }
        }
    }
/////This class is a basic coordinate 
     class Coordinate {
        public int x;
        public int y;
        public String Ltr;
        
       
        
        public Coordinate(int newX, int newY) {
            x = newX;
            y = newY;
        }

        public boolean equals(Coordinate other) {
            if (x == other.x && y == other.y) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Coordinate: [" + x + "," + y + "]";
        }
    }
     ///slightly expand the basic coodrdinate to also hold a letter
    private class LtrCoordinate extends Coordinate {
        public int Ltr;
        
        public LtrCoordinate(int newX, int newY, int s) {
        	super(newX, newY);
            Ltr = s;
        }
      
        @Override
        public boolean equals(Coordinate other) {
        	try{
            if (x == other.x && y == other.y && Ltr == Integer.valueOf(other.Ltr)) {
                return true;
            }
        	}catch(Exception e){
        		try{
        			if (x == other.x && y == other.y) {
                        return true;
                    }
        		}catch(Exception p){
        			return false;
        		}
        	}
            return false;
        }
        @Override
        public String toString() {
            return "Coordinate: [" + x + "," + y + "] : letter = " + Ltr;
        }
    }


    public void resetToStartSnake(){
    	//clear the old snake
    	mSnakeTrail.clear();
    	//reset the start snake
    	mSnakeTrail.add(new Coordinate(7, 7));
        mSnakeTrail.add(new Coordinate(6, 7));
        mSnakeTrail.add(new Coordinate(5, 7));
        mSnakeTrail.add(new Coordinate(4, 7));
        mSnakeTrail.add(new Coordinate(3, 7));
        mSnakeTrail.add(new Coordinate(2, 7));
        //reset to starting velocity
        mMoveDelay = 450;
        //starting direction in moved to south, unless it was north
        if(mDirection!=NORTH){
        	mNextDirection=SOUTH;
        }
    }
    
    public void redCoverOn(){
    	((Snake)getContext()).findViewById(R.id.bac_dim_lasn).setVisibility(RelativeLayout.VISIBLE);
    }
    public void redCoverOff(){
      	 ((Snake)getContext()).findViewById(R.id.bac_dim_lasn).setVisibility(RelativeLayout.GONE);
    }
    
    public void greenCoverOn(){
      	 ((Snake)getContext()).findViewById(R.id.bac_dim_lasn_green).setVisibility(RelativeLayout.VISIBLE);
    }
    
    public void greenCoverOff(){
        ((Snake)getContext()).findViewById(R.id.bac_dim_lasn_green).setVisibility(RelativeLayout.GONE);
    }
    
	@Override
	public boolean onTouch(View v, MotionEvent event) {
	    	switch(event.getAction()){
	        case MotionEvent.ACTION_DOWN: {
	            downX = event.getX();
	            downY = event.getY();
	            return true;
	        }
	        case MotionEvent.ACTION_UP: {
	            upX = event.getX();
	            upY = event.getY();

	            float deltaX = downX - upX;
	            float deltaY = downY - upY;

	            if(Math.abs(deltaX)>Math.abs(deltaY)){
		            if(Math.abs(deltaX) > MIN_DISTANCEH){
		                // left or right
		                if(deltaX < 0) { return this.onLeftToRightSwipe() ; }
		                if(deltaX > 0) {  return this.onRightToLeftSwipe(); }
		            }
	            }//
	            // swipe vertical?
	            if(Math.abs(deltaY) > MIN_DISTANCEV){
	                // top or down
	                if(deltaY < 0) {  return this.onTopToBottomSwipe(); }
	                if(deltaY > 0) {  return this.onBottomToTopSwipe(); }
	            }
	            else {
	            	return false; // We don't consume the event
	            }
	            return true;
	        }
	    }
	    return false;
	}
}