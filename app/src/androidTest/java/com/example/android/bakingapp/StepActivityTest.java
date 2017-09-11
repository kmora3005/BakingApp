package com.example.android.bakingapp;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.DisplayMetrics;

import com.example.android.bakingapp.ui.StepActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingapp.ui.IngredientsAndStepsActivity.KEY_EXTRA_ADAPTER_POSITION;
import static com.example.android.bakingapp.ui.IngredientsAndStepsActivity.KEY_EXTRA_DESCRIPTION;
import static com.example.android.bakingapp.ui.IngredientsAndStepsActivity.KEY_EXTRA_ID_RECIPE;
import static com.example.android.bakingapp.ui.IngredientsAndStepsActivity.KEY_EXTRA_VIDEO_URL;

/**
 * Project name BakingApp
 * Created by kenneth on 10/09/2017.
 */

public class StepActivityTest {
    private static final String STEP_DESCRIPTION = "Recipe Introduction";

    @Rule
    public final ActivityTestRule<StepActivity> mActivityTestRule = new ActivityTestRule<>(StepActivity.class);

    @Test
    public void loadingActivity_OpenStepActivity() {
        DisplayMetrics dm = mActivityTestRule.getActivity().getApplicationContext().getResources().getDisplayMetrics();
        float screenWidth = dm.widthPixels / dm.density;
        if (screenWidth < 600){
            Intent intent = new Intent();
            intent.putExtra(KEY_EXTRA_ID_RECIPE,0);
            intent.putExtra(KEY_EXTRA_ADAPTER_POSITION,0);
            intent.putExtra(KEY_EXTRA_DESCRIPTION,STEP_DESCRIPTION);
            intent.putExtra(KEY_EXTRA_VIDEO_URL,"");
            mActivityTestRule.launchActivity(intent);
            //
            onView(withId(R.id.tv_description)).check(matches(withText(STEP_DESCRIPTION)));
        }
    }


}
