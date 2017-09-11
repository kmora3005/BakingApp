package com.example.android.bakingapp.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


/**
 * Project name BakingApp
 * Created by kenneth on 27/08/2017.
 */

public class RecipeSyncIntentService extends IntentService {

    public RecipeSyncIntentService( ) {
        super("RecipeSyncIntentService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        RecipeSyncTask.syncRecipes(this);
    }


}
