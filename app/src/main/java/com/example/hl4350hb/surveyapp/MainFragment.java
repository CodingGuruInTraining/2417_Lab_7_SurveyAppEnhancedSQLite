package com.example.hl4350hb.surveyapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



public class MainFragment extends Fragment {

    // Global variables to hold listeners.
    private MainScreenListener mMainScreenListener;
    private MainScreenListener2 mMainScreenListener2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainScreenListener) {
            // Stores reference to main activity.
            mMainScreenListener = (MainScreenListener) context;
            mMainScreenListener2 = (MainScreenListener2) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MainScreenListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Sets up widgets.
        TextView surveyQuestion = (TextView) view.findViewById(R.id.survey_questions);
        Button mYesButton = (Button) view.findViewById(R.id.yes_button);
        Button mNoButton = (Button) view.findViewById(R.id.no_button);
        Button mNewButton = (Button) view.findViewById(R.id.create_button);

        // Retrieves bundle if one exists.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // Retrieve passed string values and set widgets' text to these values.
            String[] surveyStrings = bundle.getStringArray(MainActivity.NEW_SURVEY_KEY);
            surveyQuestion.setText(surveyStrings[0]);
            mYesButton.setText(surveyStrings[1]);
            mNoButton.setText(surveyStrings[2]);
        }

        // Yes button event listener.
        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainScreenListener.surveyAnswered(true);
            }
        });

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainScreenListener.surveyAnswered(false);
            }
        });

        mNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainScreenListener2.newSurveyTime(true);
            }
        });

        return view;
    }

    // Interface for transmitting survey results back to MainActivity.
    public interface MainScreenListener {
        void surveyAnswered(boolean firstAnswer);
    }

    // Interface for transmitting New Survey prompt to MainActivity.
    public interface MainScreenListener2 {
        void newSurveyTime(boolean newSurvey);
    }

    // newInstance method.
    public static MainFragment newInstance() {
        return new MainFragment();
    }
}
