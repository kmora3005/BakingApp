package com.example.android.bakingapp;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.ui.IngredientsAndStepsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingapp.sync.RecipeWidgetIntentService.EXTRA_RECIPE_ID;
import static com.example.android.bakingapp.sync.RecipeWidgetIntentService.EXTRA_RECIPE_NAME;

/**
 * Project name BakingApp
 * Created by kenneth on 10/09/2017.
 */
@RunWith(AndroidJUnit4.class)
public class IngredientsAndStepsActivityTest {
    private static final String STEP_DESCRIPTION = "Recipe Introduction";
    private static final String QUANTITY = "350.0";

    @Rule
    public final ActivityTestRule<IngredientsAndStepsActivity> mActivityTestRule = new ActivityTestRule<>(IngredientsAndStepsActivity.class);

    @Test
    public void clickRecyclerViewItem_CheckContentAndShowDescription() {
        try {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_RECIPE_ID,0);
            intent.putExtra(EXTRA_RECIPE_NAME,"Brownies");
            mActivityTestRule.launchActivity(intent);

            onView(new RecyclerViewMatcher(R.id.rv_ingredients).atPosition(0))
                    .check(matches(hasDescendant(withText(QUANTITY))));
            onView(new RecyclerViewMatcher(R.id.rv_steps).atPosition(0))
                    .check(matches(hasDescendant(withText(STEP_DESCRIPTION))));
            onView(new RecyclerViewMatcher(R.id.rv_steps).atPosition(0)).perform(click());
            onView(withId(R.id.tv_description)).check(matches(withText(STEP_DESCRIPTION)));
        }
        catch (NoMatchingViewException e){
            e.printStackTrace();
        }
    }
}
