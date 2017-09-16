package com.example.android.bakingapp.sync;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.provider.RecipeWidgetProvider;

import static com.example.android.bakingapp.ui.MainActivity.KEY_PREF_ID_RECIPE;
import static com.example.android.bakingapp.ui.MainActivity.KEY_PREF_NAME_RECIPE;
import static com.example.android.bakingapp.ui.MainActivity.PREFERENCES_RECIPE;

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
                handleActionUpdateIngredientsWidgets();
            }
        }
    }

    public static void startActionUpdateIngredientsWidgets(Context context) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGETS);
        context.startService(intent);
    }

    private void handleActionUpdateIngredientsWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gv_widget);
        SharedPreferences prefs = this.getSharedPreferences(PREFERENCES_RECIPE,Context.MODE_MULTI_PROCESS);
        int idRecipe=prefs.getInt(KEY_PREF_ID_RECIPE, 0);
        String nameRecipe=prefs.getString(KEY_PREF_NAME_RECIPE, "...");
        RecipeWidgetProvider.updateIngredientsWidgets(this, appWidgetManager, idRecipe, nameRecipe,appWidgetIds);
    }
}
