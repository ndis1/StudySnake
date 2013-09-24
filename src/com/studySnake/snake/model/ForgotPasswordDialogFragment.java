package com.studySnake.snake.model;


import javax.inject.Inject;

import com.studySnake.snake.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Simple dialog for entering the email for a forgotten password. 
 * 
 * @author Trey Robinson
 *
 */
public class ForgotPasswordDialogFragment extends DialogFragment implements OnClickListener{ 
	@Inject
	UserManager userM;
	private EditText mEmailEditText;
	
	public ForgotPasswordDialogFragment (){
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_forgot_password, container);
        mEmailEditText = (EditText) view.findViewById(R.id.etEmail);
        view.findViewById(R.id.submitButton).setOnClickListener(this);
        
        
        getDialog().setTitle("Enter Email Below.");

        return view;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitButton:
			submitForgotPassword();
			break;
		default:
			break;
		}
	}
	
	private void submitForgotPassword(){
		
		
		userM.forgotPassword(mEmailEditText.getText().toString());
		this.dismiss();
	}
}