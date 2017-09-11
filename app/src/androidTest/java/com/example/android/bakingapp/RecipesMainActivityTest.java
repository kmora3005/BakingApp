package com.example.android.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Project name BakingApp
 * Created by kenneth on 10/09/2017.
 */
@RunWith(AndroidJUnit4.class)
public class RecipesMainActivityTest {
    private static final String RECIPE_NAME = "Brownies";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecyclerViewItem_OpenIngredientsAndStepsActivity() {
        onView(withId(R.id.rv_recipes))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.tv_title)).check(matches(withText(RECIPE_NAME)));
    }
}
