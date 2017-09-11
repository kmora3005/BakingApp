package com.example.android.bakingapp.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.provider.RecipeProvider;

/**
 * Project name BakingApp
 * Created by kenneth on 27/08/2017.
 */

public class RecipeSyncUtils {
    private static boolean sInitialized;
    synchronized public static void initialize(@NonNull final Context context){
        if (sInitialized)
            return;

        sInitialized = true;

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                Cursor cursor = context.getContentResolver().query(
                        RecipeProvider.Recipes.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                assert cursor != null;
                cursor.close();
            }
        });

        checkForEmpty.start();
    }


    private static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, RecipeSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
