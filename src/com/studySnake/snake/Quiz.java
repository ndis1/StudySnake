package com.studySnake.snake;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Quiz implements Serializable,Parcelable {
	/**this class has questions, and the try on which they were gotten right. all questions start on the 
	 * zeroth try, because they have not been gotten right. 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Question> questions;
	private String name;
	private int numberOfQuestions;
	private List<Integer> zerothTry;
	private List<Integer> firstTry;
	private List<Integer> secondTry;
	private List<Integer> thirdTry;
	private List<Integer> fourthTry;

	public Quiz (ArrayList<Question> questionsIn, String quizNameIn) {
		questions = questionsIn;
		name = quizNameIn;
		numberOfQuestions = questions.size();
		zerothTry = new ArrayList<Integer>();
		firstTry = new ArrayList<Integer>();
		secondTry = new ArrayList<Integer>();
		thirdTry = new ArrayList<Integer>();
		fourthTry = new ArrayList<Integer>();

	}
	
	public void setNumberOfQuestions(int i){
		numberOfQuestions = i;
	}
	public int getNumberOfQuestions(){
		return numberOfQuestions;
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
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(questions);
		dest.writeString(name);
		dest.writeInt(numberOfQuestions);
		dest.writeList(zerothTry);

		dest.writeList(firstTry);
		dest.writeList(secondTry);
		dest.writeList(thirdTry);
		dest.writeList(fourthTry);

	}
	public List<Integer> getTryRecord(int whichTry){
		switch(whichTry){
		case 0 : {
			return zerothTry;
		}
		case 1 : {
			return firstTry;
		}
		case 2 : {
			return secondTry;

		}
		case 3 : {
			return thirdTry;
		}
		default: {
			return fourthTry;
		}
		
		}

	}
	public void addTryRecord(int whichTry, int qid){
		switch(whichTry){
		case 0 : {
			zerothTry.add(qid);
			break;
		}
		case 1 : {
			firstTry.add(qid);
			break;
		}
		case 2 : {
			secondTry.add(qid);

			break;
		}
		case 3 : {
			thirdTry.add(qid);

			break;
		}
		case 4 : {
			fourthTry.add(qid);

			break;
		}
		}
	}
	public static final Parcelable.Creator<Quiz> CREATOR
    = new Parcelable.Creator<Quiz>() {
		public Quiz createFromParcel(Parcel in) {
			return new Quiz(in);
		}

		public Quiz[] newArray(int size) {
			return new Quiz[size];
		}
		
	};
	public String getName(){
		return name;
	}
	 private Quiz(Parcel in) {
		 questions = new ArrayList<Question>();
		 in.readTypedList(questions,Question.CREATOR);
		 name = in.readString();
		 numberOfQuestions = in.readInt();
		 zerothTry = new ArrayList<Integer>();
		 in.readList(zerothTry,List.class.getClassLoader());
		 firstTry = new ArrayList<Integer>();
		 in.readList(firstTry,List.class.getClassLoader());
		 secondTry = new ArrayList<Integer>();
		 in.readList(secondTry,List.class.getClassLoader());
		 thirdTry = new ArrayList<Integer>();
		 in.readList(thirdTry,List.class.getClassLoader());
		 fourthTry = new ArrayList<Integer>();
		 in.readList(fourthTry,List.class.getClassLoader());
     }
}
