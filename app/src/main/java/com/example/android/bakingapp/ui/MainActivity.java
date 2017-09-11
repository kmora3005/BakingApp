package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeAdapter;
import com.example.android.bakingapp.provider.RecipeContract;
import com.example.android.bakingapp.provider.RecipeProvider;
import com.example.android.bakingapp.sync.RecipeSyncUtils;
import com.example.android.bakingapp.sync.RecipeWidgetIntentService;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.sync.RecipeWidgetIntentService.EXTRA_RECIPE_ID;
import static com.example.android.bakingapp.sync.RecipeWidgetIntentService.EXTRA_RECIPE_NAME;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final int LOADER_ID_RECIPES = 0;
    private static final String[] RECIPES_PROJECTION = {
            RecipeContract.COLUMN_ID,
            RecipeContract.COLUMN_NAME,
            RecipeContract.COLUMN_SERVINGS,
            RecipeContract.COLUMN_IMAGE
    };
    public static final int COL_NUM_ID = 0;
    public static final int COL_NUM_NAME = 1;
    public static final int COL_NUM_SERVINGS = 2;

    @BindView(R.id.rv_recipes)
    RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);
        int numColumns=numberOfColumns();
        GridLayoutManager layoutManager = new GridLayoutManager(this, numColumns);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(LOADER_ID_RECIPES, null, this);
        RecipeSyncUtils.initialize(this);
    }

    private int numberOfColumns(){
        DisplayMetrics dm = this.getApplicationContext().getResources().getDisplayMetrics();
        float screenWidth = dm.widthPixels / dm.density;
        return screenWidth>=600 ? 3:1;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, RecipeProvider.Recipes.CONTENT_URI,
                RECIPES_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public void onClick(int id, String name) {
        RecipeWidgetIntentService.startActionUpdateIngredientsWidgets(this, id, name);

        Intent intent = new Intent(this, IngredientsAndStepsActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID,id);
        intent.putExtra(EXTRA_RECIPE_NAME,name);
        startActivity(intent);
    }
}
