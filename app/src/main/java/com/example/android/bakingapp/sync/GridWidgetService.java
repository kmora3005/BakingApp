package com.example.android.bakingapp.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.provider.IngredientContract;
import com.example.android.bakingapp.provider.RecipeProvider;

import static com.example.android.bakingapp.sync.RecipeWidgetIntentService.EXTRA_RECIPE_ID;
import static com.example.android.bakingapp.sync.RecipeWidgetIntentService.EXTRA_RECIPE_NAME;


/**
 * Project name BakingApp
 * Created by kenneth on 03/09/2017.
 */

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int idRecipe = intent.getIntExtra(EXTRA_RECIPE_ID,0);
        String nameRecipe = intent.getStringExtra(EXTRA_RECIPE_NAME);
        return new GridRemoteViewsFactory(this.getApplicationContext(), idRecipe, nameRecipe);
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private Cursor mCursor;
    private final int mIdRecipe;
    private final String mNameRecipe;

    public GridRemoteViewsFactory(Context applicationContext, int idRecipe, String nameRecipe) {
        mContext = applicationContext;
        mIdRecipe = idRecipe;
        mNameRecipe = nameRecipe;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                RecipeProvider.Ingredients.withId(mIdRecipe),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(i);

        int quantityIndex = mCursor.getColumnIndex(IngredientContract.COLUMN_QUANTITY);
        int measureIndex = mCursor.getColumnIndex(IngredientContract.COLUMN_MEASURE);
        int ingredientIndex = mCursor.getColumnIndex(IngredientContract.COLUMN_INGREDIENT);


        float quantity = mCursor.getFloat(quantityIndex);
        String measure = mCursor.getString(measureIndex);
        String ingredient = mCursor.getString(ingredientIndex);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredient_widget);
        views.setTextViewText(R.id.tv_quantity_widget, String.valueOf(quantity));
        views.setTextViewText(R.id.tv_measure_widget, measure);
        views.setTextViewText(R.id.tv_ingredient_widget, ingredient);

        Bundle extras = new Bundle();
        extras.putInt(EXTRA_RECIPE_ID, mIdRecipe);
        extras.putString(EXTRA_RECIPE_NAME, mNameRecipe);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.tv_ingredient_widget, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}