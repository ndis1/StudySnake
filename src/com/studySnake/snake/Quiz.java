package com.studySnake.snake;

import java.util.ArrayList;

public class Quiz {
	private ArrayList<Question> questions;
	private String name;
	public Quiz (ArrayList<Question> questionsIn, String quizNameIn){
		questions = questionsIn;
		name = quizNameIn;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public boolean equalsQ(Quiz other) {
        if (this.name==other.name) {
            return true;
        }
        return false;
    }
	public ArrayList<Question> getQuestions(){
		return questions;
	}
}
