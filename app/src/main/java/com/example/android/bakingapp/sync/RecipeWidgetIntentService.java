package com.example.android.bakingapp.sync;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.provider.RecipeWidgetProvider;

/**
 * Project name BakingApp
 * Created by kenneth on 07/09/2017.
 */

public class RecipeWidgetIntentService extends IntentService {
    private static final String ACTION_UPDATE_INGREDIENTS_WIDGETS = "com.example.android.bakingapp.action.update_ingredients_widgets";
    public static final String EXTRA_RECIPE_ID = "com.example.android.bakingapp.extra.recipe_id";
    public static final String EXTRA_RECIPE_NAME = "com.example.android.bakingapp.extra.recipe_name";

    private static final int INVALID_RECIPE_ID = -1;

    public RecipeWidgetIntentService( ) {
        super("RecipeWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENTS_WIDGETS.equals(action)) {
                final int idRecipe = intent.getIntExtra(EXTRA_RECIPE_ID, INVALID_RECIPE_ID);
                final String nameRecipe = intent.getStringExtra(EXTRA_RECIPE_NAME);
                handleActionUpdateIngredientsWidgets(idRecipe, nameRecipe);
            }
        }
    }

    public static void startActionUpdateIngredientsWidgets(Context context, int idRecipe, String nameRecipe) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGETS);
        intent.putExtra(EXTRA_RECIPE_ID, idRecipe);
        intent.putExtra(EXTRA_RECIPE_NAME, nameRecipe);
        context.startService(intent);
    }

    private void handleActionUpdateIngredientsWidgets(int idRecipe, String nameRecipe) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gv_widget);
        RecipeWidgetProvider.updateIngredientsWidgets(this, appWidgetManager, idRecipe, nameRecipe,appWidgetIds);
    }
}
