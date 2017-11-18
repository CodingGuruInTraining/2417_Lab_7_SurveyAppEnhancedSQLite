package com.example.hl4350hb.surveyapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ResultsActivity extends Fragment {

    // Global variable to hold listener.
    private ResultScreenListener mResultScreenListener;

    // Global variables to hold values.
    private static int yesCount;
    private static int noCount;
    private static String option1;
    private static String option2;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ResultScreenListener) {
            mResultScreenListener = (ResultScreenListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ResultScreenListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_results, container, false);

        // Runs method to retrieve passed values.
        retrieveValues();

        // Sets up widgets.
        TextView yesView = (TextView) view.findViewById(R.id.yeses);
        TextView noView = (TextView) view.findViewById(R.id.noes);
        Button resetBtn = (Button) view.findViewById(R.id.reset_button);
        Button contBtn = (Button) view.findViewById(R.id.continue_button);

        // Determines if a New Survey has been created and displays the
        // correct options and results.
        String yesText;
        String noText;
        if (option1 == null || option2 == null) {
            // Default options (Yes and No):
            yesText = "Total Yes's: " + yesCount;
            noText = "Total No's: " + noCount;
        } else {
            yesText = "Total " + option1 + "'s: " + yesCount;
            noText = "Total " + option2 + "'s: " + noCount;
        }
        yesView.setText(yesText);
        noView.setText(noText);

        // Click event listeners.
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResultScreenListener.resetSurvey(true);
            }
        });

        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResultScreenListener.resetSurvey(false);
            }
        });

        return view;
    }


    // Interface for sending back info to main.
    public interface ResultScreenListener {
        void resetSurvey(boolean resetCounts);
    }

    // newInstance method.
    public static ResultsActivity newInstance() {
        return new ResultsActivity();
    }

    // Custom method to retrieve values from attached bundle.
    private void retrieveValues() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            yesCount = bundle.getInt(MainActivity.YES_KEY, 0);
            noCount = bundle.getInt(MainActivity.NO_KEY, 0);
            try {
                option1 = bundle.getString(MainActivity.OPT1_KEY);
                option2 = bundle.getString(MainActivity.OPT2_KEY);
            } catch (NullPointerException err) {
                err.fillInStackTrace();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mResultScreenListener = null;
    }
}
