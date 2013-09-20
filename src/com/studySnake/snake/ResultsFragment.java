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
	 private ArrayList<String> month;
	 private ArrayList<String> answer;

			 
	public static final int UserMap_ID = 1;
	private int whichList;
	private Toast mToast ;
//
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	    mToast = Toast.makeText( getActivity()  , "" , Toast.LENGTH_SHORT );
		Bundle bun = getArguments();
		month = (ArrayList<String>)bun.getSerializable("queryOnThisDataPt");
		answer = (ArrayList<String>)bun.getSerializable("questionsOnThisDataPt");
		whichList = bun.getInt("whichList");
	    ListAdapter myListAdapter = new ArrayAdapter<String>(
	    getActivity(),
	    android.R.layout.simple_list_item_1, month);
	    setListAdapter(myListAdapter);
	  
	 }
	@Override
	public void onStart(){
		  super.onStart();
		  final LinearLayout l1 = (LinearLayout)getView().findViewById(R.id.back);
		  getListView().setCacheColorHint(Color.RED);

		  l1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
	        	  ((ScoreReport) getActivity()).bdlsvis();
				
			}
			  
		  });
	}


	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
		 if(whichList == 0){
			 return inflater.inflate(R.layout.results_fragment_green, container, false);
		 }else if(whichList == 1){
			  return inflater.inflate(R.layout.results_fragment_blue, container, false);
		 }else if(whichList == 2){
			  return inflater.inflate(R.layout.results_fragment_yellow, container, false);
		 }else {
			  return inflater.inflate(R.layout.results_fragment_red, container, false);
		 }
	 }

	 @Override
	 public void onListItemClick(ListView l, View v, int position, long id) {
		 mToast.setText(answer.get(position));
		 mToast.show();
	 }
}