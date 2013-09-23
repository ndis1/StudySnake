
package com.studySnake.snake;
import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.ParseUser;

public class ScoreReport extends  FragmentActivity {
	private static final int SERIES_NR = 1;
    private ArrayList<Question> questions;
    private Context context;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    /** The most recently added series. */
    private  boolean fragLive = false;
    private int whichFrag = 1;
    private RelativeLayout back_dim_layout;
    private GraphicalView mChartView;
    private LinearLayout layout3;
    private ResultsFragment searchRadiusQuery;
    private  Intent intentCallFromSnake;
    private String reportString = "starting content";
    private static final int ALL = 5;
    private static final int WRONG = -1;

    private static final int FIRST_TRY = 1;
    private static final int SECOND_TRY = 2;
    private static final int THIRD_TRY = 3;
    private static final int FOURTH_TRY = 4;
	private int whichTry = WRONG;
	private Quiz quiz;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.score_report_layout);
        back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);
        intentCallFromSnake = getIntent();
        quiz =intentCallFromSnake.getParcelableExtra("whichQuiz");
        ArrayList<Question>qw = quiz.getQuestions();
        questions = new ArrayList<Question>();
        for(Question qi : qw){
        	qi.reset();
        	questions.add(qi);
        }
        if (mChartView == null) {
            layout3 = (LinearLayout) findViewById(R.id.chart);
            mRenderer = getBarRenderer();
            mDataset = getBarDataset();
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
                	launchFragment(seriesSelection.getPointIndex());
                }
              }
            });
            layout3.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
          } else {
            mChartView.repaint();
          }
    }
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
	        case R.id.replay_these_answers:
	        	replayWrongAnswers();
	        	return true;
	        case R.id.email_results:
	        	sendEmail();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
 
    @Override
    public void onBackPressed(){
    	//back should do nothing
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        switch(whichTry){
        case WRONG : getMenuInflater().inflate(R.menu.menu_score_rep_layout, menu);
        break;
	        case ALL : getMenuInflater().inflate(R.menu.menu_score_rep_layout, menu);
	        break;
	        case FIRST_TRY : getMenuInflater().inflate(R.menu.menu_replay_these_answers, menu);
	        break;

	        case SECOND_TRY : getMenuInflater().inflate(R.menu.menu_replay_these_answers, menu);
	        break;

	        case THIRD_TRY : getMenuInflater().inflate(R.menu.menu_replay_these_answers, menu);
	        break;

	        case FOURTH_TRY : getMenuInflater().inflate(R.menu.menu_replay_these_answers, menu);
        }      
        return true;
    }
    
    
    @Override
    public void onPause(){
  	  super.onPause();
  	  //if there is a fragment, close it down, but remember that it should be open on resume
  	  if(fragLive){
  		  closeResultsFragment();
  		  fragLive = true;
  	  }
    }
    @Override
    protected void onResumeFragments(){
  	  super.onResumeFragments();
  	  //if a fragment should be launched, launch it
  	  if(fragLive){
  		  launchFragment(whichFrag);
  	  }
    }
    @Override
    protected void onSaveInstanceState(Bundle icicle) {
  	  super.onSaveInstanceState(icicle);
  	  //remember whether there is a fragment alive, and which one
  	  icicle.putBoolean("fragLive", fragLive);
  	  icicle.putInt("whichFrag", whichFrag);
  	}
    @Override
    protected void onRestoreInstanceState(Bundle icicle){
  	  super.onRestoreInstanceState(icicle);
  	  //if there is a fragment alive, set the globals so they 
  	  //can be launched at the right time
  	  if(icicle != null){
  		  fragLive = icicle.getBoolean("fragLive");
  		  whichFrag = icicle.getInt("whichFrag");
  	  }
    }
    
    private void launchFragment(int whichClicked){
    	whichFrag = whichClicked;
    	fragLive = true;
    	Bundle bundle = new Bundle();
	    bundle.putInt("whichList", whichClicked);
	    setReplayMenu( whichClicked+1);
	    searchRadiusQuery = new ResultsFragment();
	    searchRadiusQuery.setArguments(bundle);
	    bundle.putParcelableArrayList("questz", filterQuestions(whichClicked+1));
	    getFragmentManager().beginTransaction().add(R.id.fragment_container, searchRadiusQuery).commit();
    }
    
    public void setReplayMenu(int which){
    	whichTry = which;
    	this.invalidateOptionsMenu();
    }
    public void closeResultsFragment(){
    	
  	  getFragmentManager().beginTransaction().remove(searchRadiusQuery).commit();
  	  whichTry = WRONG;
  	  fragLive = false;
  	  this.invalidateOptionsMenu();
    }
    
   
	
    private void returnToOpening(){
    	Intent intent = new Intent(getApplicationContext(), Opening.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    	this.finish();
    }
    
    //send an email to a provided email address with a report of the user's performance 
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
	        	//use the parse user object to insert the name of the user into the email
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
    
  //formatting for the email report
  	public void formatReportString(){
  		
  		String greeting = "This is your StudySnake report : \n " ;
  		String firstRight = "Questions right on the first try: \n"+ reportFilter(filterQuestions(1)) + "\n";
  		String secondRight = "Questions right on the second try: \n"+ reportFilter(filterQuestions(2))+ "\n";;
  		String thirdRight ="Questions right on the third try: \n"+  reportFilter(filterQuestions(3))+ "\n";;
  		String fourthRight = "Questions right on the fourth try: \n"+ reportFilter(filterQuestions(4));
  		reportString = greeting + firstRight + secondRight + thirdRight + fourthRight;
  	}
  	
  	//replay all answers
    private void replay(){
    	int ct = 0;
	   Intent i = new Intent(context,Snake.class);
       i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       ArrayList<Question> allUniq = filterQuestions(ALL);
       //create a new quiz, using all the questions from the old one, but setting the 
       //answers to be the full answer set, not only the ones that were left at the end of the last quiz
       ArrayList<Question> nuQuestions = new ArrayList<Question>();
    	   for(Question q : allUniq){
    		   String query = q.getQuery();
    		   String nuCorAns = q.getCorrectAnswer();
    		   ArrayList<String> nuAnswers = new ArrayList<String>();
    		   for(String s : q.getAnswers2()){
    			   nuAnswers.add(s);
    		   }
    		   nuQuestions.add(new Question(query,nuAnswers,nuAnswers,nuCorAns,ct));
    		   ct++;
    	   }
       
       Quiz nuQuiz = new Quiz(nuQuestions, quiz.getName());
       for(int ii = 0; ii < ct ; ii++){
    	   nuQuiz.addTryRecord(0, ii);
       }
       i.putExtra("whichQuiz",(Parcelable) nuQuiz);
       startActivity(i);
       this.finish();
    }
    
    //replay only the wrong answers
    private void replayWrongAnswers(){
    	int ct = 0;
 	   Intent i = new Intent(context,Snake.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ArrayList<Question> allUniq = filterQuestions(whichTry);
        //if there are wrong answers, put them in a new quiz and run it
        //if not, display a notification via toast 
        if(allUniq.size()>0){
        	ArrayList<Question> nuQuestions = new ArrayList<Question>();
      	   	for(Question q : allUniq){
      		   String query = q.getQuery();
      		   String nuCorAns = q.getCorrectAnswer();
      		   ArrayList<String> nuAnswers = new ArrayList<String>();
      		   for(String s : q.getAnswers2()){
      			   nuAnswers.add(s);
      		   }
      		   nuQuestions.add(new Question(query,nuAnswers,nuAnswers,nuCorAns,ct));
      		   ct++;
      	   	}
	        Quiz nuQuiz = new Quiz(nuQuestions, quiz.getName());
	        for(int ii = 0; ii < ct ; ii++){
	        	nuQuiz.addTryRecord(0, ii);
	        }
        	i.putExtra("whichQuiz",(Parcelable) nuQuiz);
         	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            this.finish();
        }else{
        	Toast.makeText(
        		    this, 
        		    "No Wrong Answers, Press \"Play again\" to Replay", 
        		    Toast.LENGTH_LONG).show();
        }
    }
	//formatting for the email
	public String reportFilter(ArrayList<Question> in){
		String string_to_return = "";
		for(Question q : in){
			string_to_return = string_to_return + "Question:  \n" + q.getQuery() + "\nAnswer: " + q.getCorrectAnswer() + " ";
		}
		return string_to_return;
	}
	//given all the questions, return only some of them, depending on what we want to do with them
	public ArrayList<Question> filterQuestions(int which){
		ArrayList<Question> questionsToReturn = new ArrayList<Question>();
		if(which == ALL){
			List<Integer> questionToReturnIds = quiz.getTryRecord(0);
			for(Question q : questions){
				if(questionToReturnIds.contains(q.getId()) ){
					questionsToReturn.add(q);
				}
			}
			return questionsToReturn;	
		}
		if(which == WRONG){
			List<Integer> questionToReturnIds = quiz.getTryRecord(1);
			
			for(Question q : questions){
				if((!questionToReturnIds.contains(q.getId())) && q.getDone() ){
					questionsToReturn.add(q);
				}
			}
			return questionsToReturn;	
		}
		List<Integer> questionToReturnIds = quiz.getTryRecord(which);
		for(Question q : questions){
			if(questionToReturnIds.contains(q.getId()) ){
				questionsToReturn.add(q);
			}
		}
		return questionsToReturn;
	}
	
	//achartengine set the height for the bars based on the number of questions 
	//in each category/try
	 private XYMultipleSeriesDataset getBarDataset() {
	        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	        ArrayList<String> legendTitles = new ArrayList<String>();
	        legendTitles.add("First Try");
	        for (int i = 0; i < SERIES_NR; i++) {
	            CategorySeries series = new CategorySeries(legendTitles.get(i));
	            for (int k = 1; k<5; k++) {
	                series.add(filterQuestions(k).size());
	            }
	            dataset.addSeries(series.toXYSeries());
	        }
	        return dataset;
	    }
	
	//achartengine series renderer, doesn't make bars different colors yet
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
  //achart engine multiple series renderer
  public XYMultipleSeriesRenderer getBarRenderer() {
 	 int[] colors = new int[] { Color.parseColor("#0066FF")};        
      XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		myChartSettings(renderer);
		return renderer;
	}//
  //achartengine settings
	private void myChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("How well you answered the questions");
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(5.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(questions.size());
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
  
}
