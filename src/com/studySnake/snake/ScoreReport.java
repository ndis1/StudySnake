
package com.studySnake.snake;
import java.util.ArrayList;
import java.util.HashMap;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.parse.ParseUser;
import com.studySnake.snake.R;
import com.studySnake.snake.model.UserManager;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Snake: a simple game that everyone can enjoy.
 * 
 * This is an implementation of the classic Game "Snake", in which you control a
 * serpent roaming around the garden looking for apples. Be careful, though,
 * because when you catch one, not only will you become longer, but you'll move
 * faster. Running into yourself or the walls will end the game.
 * 
 */
public class ScoreReport extends Activity {
	private static final int SERIES_NR = 1;
	private HashMap<Integer,ArrayList<String>> playerAnswersHashMap = new HashMap<Integer,ArrayList<String>>();
	private HashMap<Integer,ArrayList<String>> queryHashMap = new HashMap<Integer,ArrayList<String>>();

	private int whichActionBar = 0;

    private ArrayList<String> questions;
    private static String ICICLE_KEY = "snake-view";
    private int score;
    private Context context;
    private ListView questionDisp;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    /** The most recently added series. */
    private XYSeries mCurrentSeries;
    /** The most recently created renderer, customizing the current series. */
    private XYSeriesRenderer mCurrentRenderer;
    /** Button for creating a new series of data. */
    private Button mNewSeries;
    /** Button for adding entered data to the current series. */
    private Button mAdd;
    /** Edit text field for entering the X value of the data to be added. */
    private EditText mX;
    /** Edit text field for entering the Y value of the data to be added. */
    private EditText mY;
    /** The chart view that displays the data. */
    private RelativeLayout back_dim_layout;
    private GraphicalView mChartView;
    private LinearLayout layout3;
    private ResultsFragment searchRadiusQuery;
    private RelativeLayout vgg;
    private  Intent intentCallFromSnake;
    private String reportString = "starting content";
    /**
     * Called w0hen Activity is first created. Turns off the title bar, sets up
     * the content views, and fires up the SnakeView.
     */
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.play_again:
	        	 replay();
	        	 return true;
	        case R.id.return_to_opening:
	        	returnToOpening();
	        	return true;
	        case R.id.replay_wrong_answers:
	        	replayWrongAnswers();
	        	return true;
	        case R.id.replay_these_answers_1:
	        	replayWrongAnswers();
	        	return true;
	        case R.id.replay_these_answers_2:
	        	replayWrongAnswers();
	        	return true;
	        case R.id.replay_these_answers_3:
	        	replayWrongAnswers();
	        	return true;
	        case R.id.replay_these_answers_4:
	        	replayWrongAnswers();
	        	return true;
	        case R.id.email_results:
	        	sendEmail();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    private void returnToOpening(){
    	
    	Intent intent = new Intent(getApplicationContext(), Opening.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    }
    private void sendEmail(){
    	formatReportString();
	    final AlertDialog.Builder emailAlert = new AlertDialog.Builder(this);
	    final EditText input = new EditText(this);
	    emailAlert.setTitle("Email Results");
	    emailAlert.setMessage("Email to send results to:");
	    emailAlert.setView(input);
	    emailAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            String value = input.getText().toString().trim();
	            Intent email = new Intent(Intent.ACTION_SEND);
	        	email.putExtra(Intent.EXTRA_EMAIL, new String[]{value});		  
	        	email.putExtra(Intent.EXTRA_SUBJECT, ParseUser.getCurrentUser().getUsername() + " Study Snake Results");
	        	email.putExtra(Intent.EXTRA_TEXT, reportString);
	        	email.setType("message/rfc822");
	        	startActivity(Intent.createChooser(email, "Choose an Email client :"));
	        	
	        }
	    });

	    emailAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            dialog.cancel();
	        }
	    });
	    emailAlert.show();               
    	
    }
    private void replay(){
    	   Intent i = new Intent(context,Snake.class);
           String quizUsed = intentCallFromSnake.getStringExtra("whichQuiz");
           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           i.putExtra("whichQuiz", quizUsed);
           startActivity(i);
    }
    private void replayWrongAnswers(){
 	   Intent i = new Intent(context,Snake.class);
        String quizUsed = intentCallFromSnake.getStringExtra("whichQuiz");
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.putExtra("whichQuiz", filterQuestions(quizUsed, whichActionBar));
        if(filterQuestions(quizUsed, whichActionBar).contains("nxn")){
        	startActivity(i);
        }else{
        	Toast.makeText(
        		    this, 
        		    "No Wrong Answers, Press \"Play again\" to Replay", 
        		    Toast.LENGTH_LONG).show();
        		 
        }
 }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(whichActionBar == 0){
        getMenuInflater().inflate(R.menu.menu_score_rep_layout, menu);
        }else if(whichActionBar ==1){
            getMenuInflater().inflate(R.menu.menu_replay_these_answers_1, menu);
        }else if(whichActionBar ==2){
            getMenuInflater().inflate(R.menu.menu_replay_these_answers_2, menu);
        }else if(whichActionBar ==3){
            getMenuInflater().inflate(R.menu.menu_replay_these_answers_3, menu);
        }else if(whichActionBar ==4){
            getMenuInflater().inflate(R.menu.menu_replay_these_answers_4, menu);
        }
        
        return true;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	context = this;
        super.onCreate(savedInstanceState);
//
        setContentView(R.layout.score_report_layout);
        back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);
        vgg = (RelativeLayout) findViewById(R.id.vg);
         intentCallFromSnake = getIntent();
        questions = intentCallFromSnake.getExtras().getStringArrayList("questionsAsString");
        playerAnswersHashMap = (HashMap<Integer, ArrayList<String>>) intentCallFromSnake.getSerializableExtra("playerAnswers");
        queryHashMap = (HashMap<Integer, ArrayList<String>>) intentCallFromSnake.getSerializableExtra("queryMap");

        score = intentCallFromSnake.getExtras().getInt("finalScore");
        
       
        if (mChartView == null) {
             layout3 = (LinearLayout) findViewById(R.id.chart);
            mRenderer = getTruitonBarRenderer();
            mDataset = getTruitonBarDataset();
            mChartView = ChartFactory.getBarChartView(this, mDataset, mRenderer, Type.DEFAULT);
            // enable the chart click event
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);
            mChartView.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
            	 
                // handle the click event on the chart
                SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                if (seriesSelection == null) {
                  Toast.makeText(getApplicationContext(), "No chart element", Toast.LENGTH_SHORT).show();
                } else {
                	Bundle bundle = new Bundle();
        		    bundle.putStringArrayList("questionsOnThisDataPt",playerAnswersHashMap.get(4-seriesSelection.getPointIndex()));
        		    bundle.putStringArrayList("queryOnThisDataPt",queryHashMap.get(4-seriesSelection.getPointIndex()));
        		    bundle.putInt("whichList", seriesSelection.getPointIndex());
        		    setReplayMenu( seriesSelection.getPointIndex()+1);
        		    searchRadiusQuery = new ResultsFragment();
        		    searchRadiusQuery.setArguments(bundle);
        		    
        		    getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, searchRadiusQuery).commit();
                }
              }
            });
            layout3.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
          } else {
            mChartView.repaint();
          }
        
    }
    public void setReplayMenu(int which){
    	whichActionBar = which;
    	this.invalidateOptionsMenu();
    }
    public void bdlsvis(){
  	  getFragmentManager().beginTransaction().remove(searchRadiusQuery).commit();
  	  whichActionBar = 0;
  	  this.invalidateOptionsMenu();
    }
    private XYMultipleSeriesDataset getTruitonBarDataset() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        final int nr = 4;
        ArrayList<String> legendTitles = new ArrayList<String>();
        legendTitles.add("First Try");
        for (int i = 0; i < SERIES_NR; i++) {
            CategorySeries series = new CategorySeries(legendTitles.get(i));
            for (int k = 0; k < nr; k++) {
            	if(playerAnswersHashMap.containsKey(nr-k)){
                series.add(playerAnswersHashMap.get(nr-k).size());
            	}else{
            		series.add(0);
            	}
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }
    public XYMultipleSeriesRenderer getTruitonBarRenderer() {
    	 int[] colors = new int[] { Color.parseColor("#0066FF")};        
         XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		myChartSettings(renderer);
		return renderer;
	}//

	private void myChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("How well you answered the questions");
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(5.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(15);
		renderer.addXTextLabel(1, "1");
		renderer.addXTextLabel(2, "2");
		renderer.addXTextLabel(3, "3");
		renderer.addXTextLabel(4, "4");
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setBarSpacing(0.5);
		renderer.setXTitle("Number of tries");
		renderer.setYTitle("How Many Questions");
		renderer.setShowGrid(true);
	    renderer.setGridColor(Color.GRAY);
	    renderer.setZoomEnabled(false, false);
	    renderer.setPanEnabled(false, false);

	    renderer.setXLabels(0); // sets the number of integer labels to appear
	}
	
	public void formatReportString(){
		
		String wholeQuiz =  intentCallFromSnake.getStringExtra("whichQuiz");
		String greeting = "This is your StudySnake report : \n " ;
		String firstRight = "Questions right on the first try: \n"+ reportFilter(filterQuestions(wholeQuiz,1)) + "\n";
		String secondRight = "Questions right on the second try: \n"+ reportFilter(filterQuestions(wholeQuiz,2))+ "\n";;
		String thirdRight ="Questions right on the third try: \n"+  reportFilter(filterQuestions(wholeQuiz,3))+ "\n";;
		String fourthRight = "Questions right on the fourth try: \n"+ reportFilter(filterQuestions(wholeQuiz,4));
		reportString = greeting + firstRight + secondRight + thirdRight + fourthRight;
	}
	public String reportFilter(String in){
		String out = "";
		String [] n = in.split(" nxn ");
		int ct = 0;
		for(String nextLine: n){
			String question = "";
			String rightAnswer = "";
			if(nextLine.contains(" , ")){
	           String [] bits=  nextLine.split(" , ");
	            question = bits[0];
	            rightAnswer =bits[5];
			}
			if(question != ""){
				out = out + " "+ ct + " " +"Question : "+ question +" Answer : "+ rightAnswer +"\n";
			}
			ct ++;
		}
		return out;
	}
	public String filterQuestions(String filename, int which){
    	ArrayList<Question> thisList = new ArrayList<Question>();
    	String nstring = "";
		String [] n = filename.split(" nxn ");
		for(String nextLine: n){
			if(nextLine.contains(" , ")){
	           String [] bits=  nextLine.split(" , ");
	           String question = bits[0];
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
	           //temp fix - answers might be same for multi questions
	           if(which==0){
		           if(playerAnswersHashMap.containsKey(4)){
			           if(!playerAnswersHashMap.get(4).contains(rightAnswer)){
			        	   nstring = nstring +" nxn "+nextLine;
			           }
		           }else{
		        	   nstring = nstring +" nxn "+nextLine;
		           }
	           }else if(which == 1){
	        	   if(playerAnswersHashMap.containsKey(4)){
			           if(playerAnswersHashMap.get(4).contains(rightAnswer)){
			        	   nstring = nstring +" nxn "+nextLine;
			           }
		           }
	           }else if (which == 2){
	        	   if(playerAnswersHashMap.containsKey(3)){
			           if(playerAnswersHashMap.get(3).contains(rightAnswer)){
			        	   nstring = nstring +" nxn "+nextLine;
			           }
		           }
	           }else if (which == 3){
	        	   if(playerAnswersHashMap.containsKey(2)){
			           if(playerAnswersHashMap.get(2).contains(rightAnswer)){
			        	   nstring = nstring +" nxn "+nextLine;
			           }
		           }
	           }else if (which == 4){
	        	   if(playerAnswersHashMap.containsKey(1)){
			           if(playerAnswersHashMap.get(1).contains(rightAnswer)){
			        	   nstring = nstring +" nxn "+nextLine;
			           }
		           }
	           }
			}
		}		
		return nstring;
    }
  protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    int length = colors.length;
	    for (int i = 0; i < length; i++) {
	      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	      r.setColor(colors[i]);
	      renderer.addSeriesRenderer(r);
	    }
	    return renderer;
	  }
}
