// This app tracks responses to a survey question.
//
package com.example.hl4350hb.surveyapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainFragment.MainScreenListener,
        ResultsActivity.ResultScreenListener,
        MainFragment.MainScreenListener2,
        SurveyActivity.NewSurveyScreenListener {

    // Initialize static variables.
    Integer yesCount;
    Integer noCount;
    String question;
    String option1;
    String option2;

    // Creates static keys for bundling and intents.
    protected final static String YES_KEY = "yes something";
    protected final static String NO_KEY = "no key goes here";
    protected final static String OPT1_KEY = "first order of business";
    protected final static String OPT2_KEY = "there used to be a third option";
    protected final static String NEW_SURVEY_KEY = "the key to the survey of all surveys";
    private static final String MAIN_FRAG_TAG = "MAIN FRAGMENT";
    private static final String RESULT_FRAG_TAG = "RESULTS FRAGMENT";

    // Static variable to hold main fragment object.
    protected MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checks if bundle exists.
        if (savedInstanceState != null) {
            yesCount = savedInstanceState.getInt(YES_KEY);
            noCount = savedInstanceState.getInt(NO_KEY);
        } else {
            // First time loading steps:
            // Instantiates fragment.
            mMainFragment = MainFragment.newInstance();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            // Adds fragment to activity to be seen.
            ft.add(R.id.main_container, mMainFragment, MAIN_FRAG_TAG);
            ft.commit();
        }
        // Sets values if null.
        if (yesCount == null) {
            yesCount = 0;
        }
        if (noCount == null) {
            noCount = 0;
        }
    }

    // Bundle saver.
    @Override
    protected void onSaveInstanceState(Bundle outBundle) {
        // Saves current counters to be loaded with Activity.
        outBundle.putInt(YES_KEY, yesCount);
        outBundle.putInt(NO_KEY, noCount);
    }


    // Returning function from Main fragment.
    @Override
    public void surveyAnswered(boolean firstAnswer) {
        if (firstAnswer) {
            yesCount++;
        } else {
            noCount++;
        }
        loadResultsFragment();
    }

    // Returning function from Results fragment.
    @Override
    public void resetSurvey(boolean resetCounts) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Reset the scores.
        if (resetCounts) {
            resetCounts();
        }
        // Loads main fragment.
        ft.replace(R.id.main_container, mMainFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    // Returning function from Main fragment (New Survey button).
    @Override
    public void newSurveyTime(boolean newSurvey) {
        if (newSurvey) {
            // Instantiates new Survey fragment object.
            SurveyActivity newSurveyFragment = SurveyActivity.newInstance();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            // Replaces fragments.
            ft.replace(R.id.main_container, newSurveyFragment);
            ft.commit();
        }
    }

    // Returning function from Survey fragment.
    @Override
    public void newSurveyCreated(String[] newSurvey) {
        // Retrieves passed string values and updates global variables
        // with these strings.
        question = newSurvey[0];
        option1 = newSurvey[1];
        option2 = newSurvey[2];
        // Resets the counts since this is now a new survey.
        resetCounts();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Creates new bundle object and attaches new survey strings
        // to the main fragment.
        Bundle bundle = new Bundle();
        bundle.putStringArray(NEW_SURVEY_KEY, newSurvey);
        mMainFragment.setArguments(bundle);

        // Replaces fragments.
        ft.replace(R.id.main_container, mMainFragment);
        ft.commit();
    }



    // Custom method to reset counters.
    private void resetCounts() {
        // Resets counters.
        yesCount = 0;
        noCount = 0;
    }

    // Custom method to load the Results fragment.
    private void loadResultsFragment() {
        // Creates new bundle and adds the current counts
        // and the current option strings.
        Bundle bundle = new Bundle();
        bundle.putInt(YES_KEY, yesCount);
        bundle.putInt(NO_KEY, noCount);
        // Checks if a new survey has been made yet and would have
        // custom options.
        if (option1 != null && option2 != null) {
            bundle.putString(OPT1_KEY, option1);
            bundle.putString(OPT2_KEY, option2);
        }

        // Instantiates new Results fragment object.
        ResultsActivity resultsFragment = ResultsActivity.newInstance();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Attaches bundle to fragment object.
        resultsFragment.setArguments(bundle);

        // Replaces fragments.
        ft.replace(R.id.main_container, resultsFragment);
        ft.addToBackStack(RESULT_FRAG_TAG);
        ft.commit();
    }
}
