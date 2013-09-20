
package com.studySnake.snake;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;//
import com.parse.ParseUser;
import com.studySnake.snake.R;
import com.studySnake.snake.model.UserManager;

public class Opening extends Activity {
    
    private static String ICICLE_KEY = "snake-view";
    private Context context;
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()){
	    
	    //
	        case R.id.menu_logout:
	        	parseLogout();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Todo");
        query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(final List<ParseObject> objects, ParseException e) {
				ArrayList<String> quizesFound = new ArrayList<String>();
				ArrayList<Button> dynButtons=	new ArrayList<Button>();
				final ParseObject p1 = objects.get(0);
				dynButtons.add(new Button(getBaseContext()));
				dynButtons.get(0).setId(33);
				dynButtons.get(0).setBackgroundResource(R.drawable.greenstar);
				dynButtons.get(0).setText(p1.getString("title"));
				quizesFound.add(p1.getString("title"));
				dynButtons.get(0).setMinimumHeight(150);
				dynButtons.get(0).setMinimumWidth(150);
		        RelativeLayout rl = (RelativeLayout) findViewById(R.id.Button22);
		        rl.addView(dynButtons.get(0)); 
		        dynButtons.get(0).setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		                Intent i = new Intent(context,Snake.class);
		                String passer = "";
		                for(ParseObject ppo : objects){
		                	if(ppo.getString("title").equals(p1.getString("title"))){
		                		passer = passer+" nxn "+ppo.getString("content")+" , "+ppo.getString("choices")+" , "+ppo.getString("answer")+" , "+1;
		                	}
		                }
		                i.putExtra("whichQuiz", passer);
		                startActivity(i);
		            }
		        });
		        int btnCt = 1;
		        ArrayList<  RelativeLayout.LayoutParams> listLp = new   ArrayList<RelativeLayout.LayoutParams>();
				for(final ParseObject po : objects){
					if(!po.equals(p1)){
						String playerName = po.getString("title");
						if(!quizesFound.contains(playerName)){
							btnCt++;
							quizesFound.add(playerName);
							dynButtons.add(new Button(getBaseContext()));
							dynButtons.get(btnCt-1).setId(33+btnCt-1);
							dynButtons.get(btnCt-1).setBackgroundResource(R.drawable.greenstar);
							dynButtons.get(btnCt-1).setText(po.getString("title"));
							dynButtons.get(btnCt-1).setMinimumHeight(150);
							dynButtons.get(btnCt-1).setMinimumWidth(150);
					        listLp.add(new RelativeLayout.LayoutParams(
					        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
					        listLp.get(btnCt-2).addRule(RelativeLayout.BELOW, 33+btnCt-2);			        
					        rl.addView(dynButtons.get(btnCt-1),listLp.get(btnCt-2)); 
					        dynButtons.get(btnCt-1).setOnClickListener(new View.OnClickListener() {
					            public void onClick(View v) {
					            	Intent i = new Intent(context,Snake.class);
					                String passer = "";
					                for(ParseObject ppo : objects){
					                	if(ppo.getString("title").equals(po.getString("title"))){
					                		passer = passer+" nxn "+ppo.getString("content")+" , "+ppo.getString("choices")+" , "+ppo.getString("answer")+" , "+1;
					                	}
					                }
					                i.putExtra("whichQuiz", passer);
					                startActivity(i);
					            }
					        });
						}
					}
				}
			}
        });
    }
}
