package com.studySnake.snake;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;


public class Question implements Parcelable, Serializable {
	/**this class is a question that has the query, answers, correct answer, and id. It also has reset answers
	 * for making a new question once the regular answers have been mutilated. a question belongs to a quiz
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String myQuery;
	private ArrayList<String> myAnswers;
	private ArrayList<String> resetAnswers;

	private String myCorrectAnswer;
	private int myId;
	private boolean done;
	public Question(String query, ArrayList<String> answers, ArrayList<String> answers2, String correctAnswer, int id){
		myAnswers = new  ArrayList<String>();
		resetAnswers = new ArrayList<String>();
		myQuery=query;
		myAnswers.addAll(answers);
		resetAnswers.addAll(answers2);
		myCorrectAnswer=correctAnswer;
		myId=id;
		done = false;
	}
	
	public String getCorrectAnswer(){
		return myCorrectAnswer;
	}
	public void setMyAnswers( ArrayList<String> nuAns){
		myAnswers = new ArrayList<String>();
		myAnswers.addAll(nuAns);
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
	public ArrayList<String> getAnswers2(){
		return resetAnswers;
	}
	@Override
	public String toString(){
		String corAns = myCorrectAnswer;
		return "question No: " + myId + "\n query = " + myQuery + "\n answer = " + corAns + "\n your answer = "+myAnswers.toString();
	}
	@Override
	public int hashCode(){
		return myId;
	}
	
	public void setDone(boolean b){
	done = b;
	}
	public boolean getDone(){
		return done;
	}
	public boolean equals(Question other) {
        if (this.hashCode()==other.hashCode()) {
            return true;
        }
        return false;
    }
	public void reset(){
		ArrayList<String> ss =new ArrayList<String>();
		for(String s : resetAnswers){
			ss.add(s);
		}
		myAnswers = ss;

	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(myQuery);
		dest.writeString(myCorrectAnswer);

		dest.writeStringList(myAnswers);
		dest.writeStringList(resetAnswers);

		dest.writeInt(myId);
		dest.writeByte((byte) (done ? 1 : 0)); 
	}
	public static final Parcelable.Creator<Question> CREATOR
    = new Parcelable.Creator<Question>() {
		public Question createFromParcel(Parcel in) {
			return new Question(in);
		}

		public Question[] newArray(int size) {
			return new Question[size];
		}
		
	};
	 private Question(Parcel in) {
		 myQuery = in.readString();
		 myCorrectAnswer = in.readString();
		 myAnswers = new ArrayList<String>();
		 resetAnswers = new ArrayList<String>();

		 in.readStringList(myAnswers);
		 in.readStringList(resetAnswers);
		 myId = in.readInt();
		 done = in.readByte() == 1;
     }
}
