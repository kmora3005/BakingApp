package com.example.android.bakingapp.provider;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.sync.GridWidgetService;
import com.example.android.bakingapp.sync.RecipeWidgetIntentService;
import com.example.android.bakingapp.ui.IngredientsAndStepsActivity;

import static com.example.android.bakingapp.sync.RecipeWidgetIntentService.EXTRA_RECIPE_ID;
import static com.example.android.bakingapp.sync.RecipeWidgetIntentService.EXTRA_RECIPE_NAME;

/**
 * Project name BakingApp
 * Created by kenneth on 03/09/2017.
 */

public class RecipeWidgetProvider extends AppWidgetProvider {
    private static int mIdRecipe;
    private static String mNameRecipe;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int idRecipe,
                                        String nameRecipe, int appWidgetId) {

        RemoteViews rv = getGridRemoteView(context,idRecipe,nameRecipe);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeWidgetIntentService.startActionUpdateIngredientsWidgets(context,mIdRecipe, mNameRecipe);
    }

    public static void updateIngredientsWidgets(Context context, AppWidgetManager appWidgetManager,int idRecipe,
            String nameRecipe, int[] appWidgetIds) {
        mIdRecipe=idRecipe;
        mNameRecipe=nameRecipe;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, idRecipe, nameRecipe, appWidgetId);
        }
    }

    private static RemoteViews getGridRemoteView(Context context, int idRecipe, String nameRecipe) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.tv_recipe_name, " "+nameRecipe);

        Intent intent = new Intent(context, GridWidgetService.class);
        intent.putExtra(EXTRA_RECIPE_ID,idRecipe);
        intent.putExtra(EXTRA_RECIPE_NAME,nameRecipe);
        views.setRemoteAdapter(R.id.gv_widget, intent);

        Intent appIntent = new Intent(context, IngredientsAndStepsActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.gv_widget, appPendingIntent);

        views.setEmptyView(R.id.gv_widget, R.id.rl_empty_view);
        return views;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        RecipeWidgetIntentService.startActionUpdateIngredientsWidgets(context,mIdRecipe,mNameRecipe);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}
