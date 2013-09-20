
package com.studySnake.snake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
//

public class Opening extends ListActivity {
    private ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
    private static String ICICLE_KEY = "snake-view";
    private Context context;
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()){
	    	case R.id.menu_logout:
	        	parseLogout();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    //log out of parse and kill all activities before the top activity
    public void parseLogout(){
    	ParseUser.logOut();
    	Intent i = new Intent(context,Login.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_opening_layout, menu);
        return true;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	context = this;
        setContentView(R.layout.opening_layout);
        getButtons();
        
    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    // Get the item that was clicked
	    Quiz quizClickedByUser = (Quiz) this.getListAdapter().getItem(position);
	    Intent i = new Intent(context,Snake.class);
	    String passer = "";
	    int idCtr = 0;
	    for(Question q : quizClickedByUser.getQuestions()){
	    	ArrayList< String> answers = q.getAnswers();
	    	passer = passer+" nxn "+q.getQuery().trim()+" , "+answers.get(0).trim()+" , "+answers.get(1).trim()+" , "+answers.get(2).trim()+" , "+answers.get(3).trim()+" , "+q.getCorrectAnswer().trim()+" , "+idCtr;
	    	idCtr++;
	    }    
	    i.putExtra("whichQuiz", passer);
	    startActivity(i);
    }
    private void getButtons(){
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Todo");
        query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(final List<ParseObject> objects, ParseException e) {
				int questionCt = 0;
				HashMap<String,ArrayList<Question>> quizesFound = new HashMap<String,ArrayList<Question>>();
				for(ParseObject po : objects){
					String quizName = po.getString("title");
					String answer = po.getString("answer");
					String choicesAsBlob = po.getString("choices");
			        String [] bits=  choicesAsBlob.split(",");
					ArrayList<String> choices = new ArrayList<String>();
			        for(String b : bits){
			        	b.trim();
			        	if(!b.equals("")){
			        		choices.add(b);
			        	}
			        }
					String query = po.getString("content");
					ArrayList<Question> questions;
					if(quizesFound.containsKey(quizName)){
						questions = quizesFound.get(quizName);
					}else{
						questions = new ArrayList<Question>();
					}
					Question nextQuestion = new Question(query, choices, answer, questionCt);
					questions.add(nextQuestion);
					quizesFound.put(quizName, questions);
				}
				for (Map.Entry<String, ArrayList<Question>> entry : quizesFound.entrySet()) {
				    String key = entry.getKey();
				    ArrayList<Question> value = entry.getValue();
				    quizzes.add(new Quiz(value,key));
				}
				ArrayAdapter<Quiz> adapter = new ArrayAdapter<Quiz>(context,
				        android.R.layout.simple_list_item_1, quizzes);
		        setListAdapter(adapter);
			}
        });
    }
}
