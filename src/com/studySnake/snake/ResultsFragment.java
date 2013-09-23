package com.studySnake.snake;

import java.util.ArrayList;

import com.studySnake.*;
import com.studySnake.snake.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ResultsFragment extends ListFragment {
	 private ArrayList<Question> questions;
	 private ArrayList<String> querys = new ArrayList<String>();
	 private ArrayList<String> answers = new ArrayList<String>();
			 
	public static final int UserMap_ID = 1;
	private int whichList;
	//maintain a global toast so that the text can be changed rapidly when the user clicks different 
	//items in the listview
	private Toast mToast ;

	@Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	    mToast = Toast.makeText( getActivity()  , "" , Toast.LENGTH_SHORT );
		Bundle bun = getArguments();
		questions = bun.getParcelableArrayList("questz");
		for(Question q : questions){
			querys.add(q.getQuery());
			answers.add(q.getCorrectAnswer());
		}
		whichList = bun.getInt("whichList");
	    ListAdapter myListAdapter = new ArrayAdapter<String>(
	    getActivity(),
	    android.R.layout.simple_list_item_1, querys);
	    setListAdapter(myListAdapter);
	  
	 }
	@Override
	public void onStart(){
		  super.onStart();
		  final LinearLayout l1 = (LinearLayout)getView().findViewById(R.id.back);
		  //let the fragment know we want the color of the background to be red
		  getListView().setCacheColorHint(Color.RED);
		  l1.setOnClickListener(new View.OnClickListener() {
			  //when the user clicks on the transparent edge(outside the list), close the fragment
			@Override
			public void onClick(View v) {
	        	 ((ScoreReport) getActivity()).closeResultsFragment();
			}
		  });
	}
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
		 View viewForResultsFragment =  inflater.inflate(R.layout.results_fragment, container, false);
		 return viewForResultsFragment;
	 }
	 
	 @Override
	 public void onActivityCreated (Bundle savedInstanceState){
		 super.onActivityCreated(savedInstanceState);
		 Resources res = getResources();
		 View listView = getListView();

		 //launch the list of questions answered on the xth try in the
		 // color for that degree of success(green = best blue 2nd best, yellow worse ,red awful
		 View backgroundView =  getActivity().findViewById(R.id.back);
			 if(whichList == 0){				 
				 listView.setBackgroundColor(res.getColor(R.color.green));
				 backgroundView.setBackgroundColor(res.getColor(R.color.green_background));

			 }else if(whichList == 1){
				 listView.setBackgroundColor(res.getColor(R.color.blue));
				 backgroundView.setBackgroundColor(res.getColor(R.color.blue_background));
			}else if(whichList == 2){
				 listView.setBackgroundColor(res.getColor(R.color.yellow));
				backgroundView.setBackgroundColor(res.getColor(R.color.yellow_background));
			 }else {
				 listView.setBackgroundColor(res.getColor(R.color.red));
				 backgroundView.setBackgroundColor(res.getColor(R.color.red_background));
			 }
	 }
//when list items are clicked show the answer to the question
	 @Override
	 public void onListItemClick(ListView l, View v, int position, long id) {
		 //to make sure the toast doesn't stay too long, change the text of a global toast
		 mToast.setText(answers.get(position));
		 mToast.show();
	 }
}