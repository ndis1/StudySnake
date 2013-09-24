
package com.studySnake.snake;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
//

public class Opening extends ListActivity {
	//here store all the quizzes collected from parse -- this is used to populate the listview
    private ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
    private Context context;
    //key for caching quizzes 
    private String CACHE_KEY = "quiz-cache";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	context = this;
        setContentView(R.layout.opening_layout);
        //if the cache is empty, get the quizzes from the database, otherwise, extract them from the caches
   	   try {
			List<Quiz> cachedEntries = (List<Quiz>) InternalStorage.readObject(this, CACHE_KEY);
			if(cachedEntries == null){
		        getQuizzesFromParse();
			}else if(cachedEntries.size() == 0 ){
		        getQuizzesFromParse();
			}else{
				quizzes.addAll(cachedEntries);
				ColoredArrayAdapter<Quiz> adapter = new ColoredArrayAdapter<Quiz>(context,
				        android.R.layout.simple_list_item_1, quizzes);
		        setListAdapter(adapter);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()){
	    	case R.id.menu_logout:
	        	parseLogout();
	        	return true;
	    	case R.id.menu_refresh_quiz_list:
	        	refreshQuizzesList();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
  
    //log out of parse and kill all activities before the top activity
    public void parseLogout(){
         ArrayList<Quiz> quizzesEmpty = new ArrayList<Quiz>();
         //wipe the cache
    	try {
			InternalStorage.writeObject(this, CACHE_KEY,quizzesEmpty);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	ParseUser.logOut();
    	Intent i = new Intent(context,Login.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        this.finish();
    }
    //get all quizzes from parse(this updates the list if there are new quizzes)
    public void refreshQuizzesList(){
    	ArrayList<Quiz> quizzesEmpty = new ArrayList<Quiz>();
        //wipe the cache
   	try {
			InternalStorage.writeObject(this, CACHE_KEY,quizzesEmpty);
		} catch (IOException e) {
			e.printStackTrace();
		}
   		quizzes.clear();
   		getQuizzesFromParse();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_opening_layout, menu);
        return true;
    }
   
    private void cacheQuizzes(){
         try {
      	   // Save the list of entries to internal storage
      	   InternalStorage.writeObject(this, CACHE_KEY, quizzes);
      	
      	} catch (IOException e) {
      	   Log.wtf("io excep", e.getMessage());
      	}

    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    // Get the quiz that was clicked 
	    Quiz quizClickedByUser = (Quiz) this.getListAdapter().getItem(position);
	    for(Question q : quizClickedByUser.getQuestions()){
		    quizClickedByUser.addTryRecord(0, q.getId());
	    }
	    Intent i = new Intent(context,Snake.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	    i.putExtra("whichQuiz",(Parcelable)  quizClickedByUser);
	    startActivity(i);
	    this.finish();
    }
    @Override
    public void onBackPressed(){
    	//back should do nothing
    }
    //pulls the quizzes from parse and puts them into the quiz class
    private void getQuizzesFromParse(){
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Todo");
        query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(final List<ParseObject> objects, ParseException e) {
				int questionCt = 0;
				HashMap<String,ArrayList<Question>> quizesFound = new HashMap<String,ArrayList<Question>>();
				//for now the questions are all their own objects. this processing is needed to sort the questions into
				//their quizzes. After the webapp is redesigned, this should be able to just get the quizzes from parse
				//and assemble them in a more straightforward way
				for(ParseObject po : objects){
					String quizName = po.getString("title");
					String answer = po.getString("answer").trim();
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
					Question nextQuestion = new Question(query, choices, choices, answer, questionCt);
					questionCt++;
					questions.add(nextQuestion);
					quizesFound.put(quizName, questions);
				}
				for (Map.Entry<String, ArrayList<Question>> entry : quizesFound.entrySet()) {
				    String key = entry.getKey();
				    ArrayList<Question> value = entry.getValue();
				    quizzes.add(new Quiz(value,key));
				}
				ColoredArrayAdapter<Quiz> adapter = new ColoredArrayAdapter<Quiz>(context,
				        android.R.layout.simple_list_item_1, quizzes);
		        setListAdapter(adapter);
		        //after the quizzes are recieved, cache them
		        cacheQuizzes();
			}
        });
    }
}
