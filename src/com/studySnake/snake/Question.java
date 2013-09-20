package com.studySnake.snake;

import java.util.ArrayList;
import java.util.LinkedList;


public class Question {
	private String myQuery;
	private ArrayList<String> myAnswers;
	private String myCorrectAnswer;
	private int myId;
	public Question(String query, ArrayList<String> answers, String correctAnswer, int id){
		myQuery=query;
		myAnswers=answers;
		myCorrectAnswer=correctAnswer;
		myId=id;
	}
	
	public String getCorrectAnswer(){
		return myCorrectAnswer;
	}
	public void setMyAnswers( ArrayList<String> nuAns){
		myAnswers = nuAns;
	}
	public int getId(){
		return myId;
	}
	
	public String getQuery(){
		return myQuery;
	}
	public ArrayList<String> getAnswers(){
		return myAnswers;
	}
	@Override
	public String toString(){
		String corAns = myCorrectAnswer;
		return "question No: " + myId + "\n query = " + myQuery + "\n answer = " + corAns + "\n your answer = ";
	}
	@Override
	public int hashCode(){
		return myId;
	}
	@Override
	public boolean equals(Object other) {
        if (this.hashCode()==other.hashCode()) {
            return true;
        }
        return false;
    }
	
}
