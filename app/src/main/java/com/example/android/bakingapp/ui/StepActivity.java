package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.StepAdapter;
import com.example.android.bakingapp.provider.RecipeProvider;
import com.example.android.bakingapp.provider.StepContract;

import static com.example.android.bakingapp.ui.IngredientsAndStepsActivity.KEY_EXTRA_DESCRIPTION;
import static com.example.android.bakingapp.ui.IngredientsAndStepsActivity.KEY_EXTRA_ID_RECIPE;
import static com.example.android.bakingapp.ui.IngredientsAndStepsActivity.KEY_EXTRA_ADAPTER_POSITION;
import static com.example.android.bakingapp.ui.IngredientsAndStepsActivity.KEY_EXTRA_VIDEO_URL;

public class StepActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>, StepAdapter.StepAdapterOnClickHandler{
    private static final String TAG = StepActivity.class.getSimpleName();
    private static final int LOADER_ID_STEPS = 3;

    private static final String[] STEPS_PROJECTION = {
            StepContract.COLUMN_ID,
            StepContract.COLUMN_SHORT_DESCRIPTION,
            StepContract.COLUMN_DESCRIPTION,
            StepContract.COLUMN_VIDEO_URL
    };

    private int mStartPosition;
    private int mRecipeId;
    private StepAdapter mAdapterSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent intent = getIntent();
        if (intent.hasExtra(KEY_EXTRA_ID_RECIPE)){
            mRecipeId=intent.getExtras().getInt(KEY_EXTRA_ID_RECIPE);
        }
        if (intent.hasExtra(KEY_EXTRA_ADAPTER_POSITION)){
            mStartPosition=intent.getExtras().getInt(KEY_EXTRA_ADAPTER_POSITION);
        }
        String description="";
        String videoUrl="";
        if (intent.hasExtra(KEY_EXTRA_DESCRIPTION)){
            description=intent.getExtras().getString(KEY_EXTRA_DESCRIPTION);
        }
        if (intent.hasExtra(KEY_EXTRA_VIDEO_URL)){
            videoUrl=intent.getExtras().getString(KEY_EXTRA_VIDEO_URL);
        }

        if(savedInstanceState == null) {
            StepCardFragment fragment = new StepCardFragment();
            fragment.setDescription(description);
            fragment.setVideoUrl(videoUrl);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_fragment_container, fragment)
                    .commit();
        }
        mAdapterSteps = new StepAdapter(this);
        getSupportLoaderManager().initLoader(LOADER_ID_STEPS, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, RecipeProvider.Steps.withId(mRecipeId),
                STEPS_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapterSteps.swapCursor(data);

        mAdapterSteps.setPosition(mStartPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapterSteps.swapCursor(null);
    }

    @Override
    public void onClick(int idStep, String description, String videoUrl) {

    }

    public void onClickNext(View view) {
        mAdapterSteps.nextPosition();
        String description=mAdapterSteps.currentDescription();
        String videoUrl=mAdapterSteps.currentVideoUrl();

        StepCardFragment fragment = new StepCardFragment();
        fragment.setDescription(description);
        fragment.setVideoUrl(videoUrl);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_fragment_container, fragment)
                .commit();
    }
}
