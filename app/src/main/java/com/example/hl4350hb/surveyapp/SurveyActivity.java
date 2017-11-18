package com.example.hl4350hb.surveyapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SurveyActivity extends Fragment {

    // Global variable to hold listener.
    private NewSurveyScreenListener mNewSurveyScreenListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewSurveyScreenListener) {
            mNewSurveyScreenListener = (NewSurveyScreenListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ResultScreenListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_survey, container, false);

        // Defines widgets.
        final EditText newQuestion = (EditText) view.findViewById(R.id.new_question);
        final EditText option1 = (EditText) view.findViewById(R.id.option_1);
        final EditText option2 = (EditText) view.findViewById(R.id.option_2);
        Button createBtn = (Button) view.findViewById(R.id.submit_button);

        // Click event listener.
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grabs values from widgets.
                String question = newQuestion.getText().toString();
                String opt1 = option1.getText().toString();
                String opt2 = option2.getText().toString();

                // Checks that all fields are filled before continuing.
                if (question.equals("") || opt1.equals("") || opt2.equals("")) {
                    // Displays Toast if at least one field is empty.
                    Toast.makeText(container.getContext(), "Please fill in all fields before submitting", Toast.LENGTH_SHORT).show();
                } else {
                    // Wraps string values into an array and passes back to MainActivity.
                    String[] newSurveyStrings = {question, opt1, opt2};
                    mNewSurveyScreenListener.newSurveyCreated(newSurveyStrings);
                }
            }
        });
        return view;
    }

    // Interface for transmitting new survey strings to MainActivity.
    public interface NewSurveyScreenListener {
        void newSurveyCreated(String[] newSurvey);
    }

    // newInstance method.
    public static SurveyActivity newInstance() {
        return new SurveyActivity();
    }
}
