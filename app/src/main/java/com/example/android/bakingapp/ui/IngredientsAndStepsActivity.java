package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.IngredientAdapter;
import com.example.android.bakingapp.adapter.StepAdapter;
import com.example.android.bakingapp.provider.IngredientContract;
import com.example.android.bakingapp.provider.RecipeProvider;
import com.example.android.bakingapp.provider.StepContract;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.sync.RecipeWidgetIntentService.EXTRA_RECIPE_ID;
import static com.example.android.bakingapp.sync.RecipeWidgetIntentService.EXTRA_RECIPE_NAME;


public class IngredientsAndStepsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, StepAdapter.StepAdapterOnClickHandler {
    private static final int LOADER_ID_INGREDIENTS = 1;
    private static final int LOADER_ID_STEPS = 2;

    private static final String[] INGREDIENTS_PROJECTION = {
            IngredientContract.COLUMN_ID,
            IngredientContract.COLUMN_QUANTITY,
            IngredientContract.COLUMN_MEASURE,
            IngredientContract.COLUMN_INGREDIENT
    };
    public static final String[] STEPS_PROJECTION = {
            StepContract.COLUMN_ID,
            StepContract.COLUMN_SHORT_DESCRIPTION,
            StepContract.COLUMN_DESCRIPTION,
            StepContract.COLUMN_VIDEO_URL,
            StepContract.COLUMN_THUMBNAIL_URL
    };

    public static final int COL_NUM_QUANTITY = 1;
    public static final int COL_NUM_MEASURE = 2;
    public static final int COL_NUM_INGREDIENT = 3;

    public static final int COL_NUM_SHORT_DESCRIPTION = 1;
    public static final int COL_NUM_DESCRIPTION = 2;
    public static final int COL_NUM_VIDEO_URL = 3;
    public static final int COL_NUM_THUMBNAIL_URL = 4;

    public static final String KEY_EXTRA_ADAPTER_POSITION = "adapter_position_key";
    public static final String KEY_EXTRA_ID_RECIPE = "id_recipe_key";
    public static final String KEY_EXTRA_DESCRIPTION = "description_key";
    public static final String KEY_EXTRA_VIDEO_URL = "video_url_key";
    public static final String KEY_EXTRA_THUMBNAIL_URL = "thumbnail_url_key";

    public static final String KEY_PARCELABLE_INGREDIENTS = "position_ingredients_key";
    public static final String KEY_PARCELABLE_STEPS = "position_steps_key";
    private final String KEY_SCROLL_POSITION = "scroll_position_key";

    private int mRecipeId;

    @BindView(R.id.rv_ingredients)
    RecyclerView mRecyclerViewIngredients;
    private IngredientAdapter mAdapterIngredients;

    @BindView(R.id.rv_steps)
    RecyclerView mRecyclerViewSteps;
    private StepAdapter mAdapterSteps;

    @BindView(R.id.sv_ingredients_and_steps)
    ScrollView mScrollViewIngredientsAndSteps;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_and_steps);
        ButterKnife.bind(this);
        mRecyclerViewIngredients = (RecyclerView) findViewById(R.id.rv_ingredients);
        mRecyclerViewSteps = (RecyclerView) findViewById(R.id.rv_steps);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_RECIPE_ID)) {
            mRecipeId = intent.getExtras().getInt(EXTRA_RECIPE_ID);
        }
        if (intent.hasExtra(EXTRA_RECIPE_NAME)) {
            String recipeName = intent.getExtras().getString(EXTRA_RECIPE_NAME);
            tvTitle.setText(recipeName);
        }


        GridLayoutManager layoutManagerIngredients;
        if (getResources().getBoolean(R.bool.isTablet)) {
            layoutManagerIngredients = new GridLayoutManager(this, 2);
            if (savedInstanceState == null) {
                StepCardFragment fragment = new StepCardFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_fragment_container, fragment)
                        .commit();
            }
        } else {
            layoutManagerIngredients = new GridLayoutManager(this, 1);
        }

        mRecyclerViewIngredients.setHasFixedSize(true);
        mRecyclerViewIngredients.setLayoutManager(layoutManagerIngredients);
        mAdapterIngredients = new IngredientAdapter();
        mRecyclerViewIngredients.setAdapter(mAdapterIngredients);

        mRecyclerViewSteps.setHasFixedSize(true);
        GridLayoutManager layoutManagerSteps = new GridLayoutManager(this, 1);
        layoutManagerSteps.setAutoMeasureEnabled(true);
        mRecyclerViewSteps.setLayoutManager(layoutManagerSteps);
        mAdapterSteps = new StepAdapter(this);
        mRecyclerViewSteps.setAdapter(mAdapterSteps);

        getSupportLoaderManager().initLoader(LOADER_ID_INGREDIENTS, null, this);
        getSupportLoaderManager().initLoader(LOADER_ID_STEPS, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_INGREDIENTS:
                return new CursorLoader(this, RecipeProvider.Ingredients.withId(mRecipeId),
                        INGREDIENTS_PROJECTION, null, null, null);
            case LOADER_ID_STEPS:
                return new CursorLoader(this, RecipeProvider.Steps.withId(mRecipeId),
                        STEPS_PROJECTION, null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        switch (loader.getId()) {
            case LOADER_ID_INGREDIENTS:
                mAdapterIngredients.swapCursor(data);

                break;
            case LOADER_ID_STEPS:
                mAdapterSteps.swapCursor(data);

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_ID_INGREDIENTS:
                mAdapterIngredients.swapCursor(null);
                break;
            case LOADER_ID_STEPS:
                mAdapterSteps.swapCursor(null);
                break;
        }
    }

    @Override
    public void onClick(int position, String description, String videoUrl, String thumbnailUrl) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            StepCardFragment fragment = new StepCardFragment();
            fragment.setDescription(description);
            fragment.setVideoUrl(videoUrl);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_fragment_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(KEY_EXTRA_ADAPTER_POSITION, position);
            intent.putExtra(KEY_EXTRA_ID_RECIPE, mRecipeId);
            intent.putExtra(KEY_EXTRA_DESCRIPTION, description);
            intent.putExtra(KEY_EXTRA_VIDEO_URL, videoUrl);
            intent.putExtra(KEY_EXTRA_THUMBNAIL_URL, thumbnailUrl);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_PARCELABLE_INGREDIENTS, mRecyclerViewIngredients.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(KEY_PARCELABLE_STEPS, mRecyclerViewSteps.getLayoutManager().onSaveInstanceState());

        outState.putIntArray(KEY_SCROLL_POSITION,
                new int[]{ mScrollViewIngredientsAndSteps.getScrollX(), mScrollViewIngredientsAndSteps.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            Parcelable parcelableIngredients = savedInstanceState.getParcelable(KEY_PARCELABLE_INGREDIENTS);
            Parcelable parcelableSteps = savedInstanceState.getParcelable(KEY_PARCELABLE_STEPS);

            mRecyclerViewIngredients.getLayoutManager().onRestoreInstanceState(parcelableIngredients);
            mRecyclerViewSteps.getLayoutManager().onRestoreInstanceState(parcelableSteps);

            final int[] position = savedInstanceState.getIntArray(KEY_SCROLL_POSITION);
            if(position != null)
                mScrollViewIngredientsAndSteps.post(new Runnable() {
                    public void run() {
                        mScrollViewIngredientsAndSteps.scrollTo(position[0], position[1]);
                    }
                });
        }
    }


}
