package com.example.android.bakingapp.object;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.android.bakingapp.network.GsonRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Project name BakingApp
 * Created by kenneth on 26/08/2017.
 */

public class JsonResponseRecipes {
    private static final String TAG = JsonResponseRecipes.class.getSimpleName();
    private final static String PATH_JSON_RECIPE="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static String readUrl() throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(JsonResponseRecipes.PATH_JSON_RECIPE);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public void getRecipes(Context context, Response.Listener<Recipe[]> requestSuccessListener)  {
        RequestQueue queue = Volley.newRequestQueue(context);
        GsonRequest<Recipe[]> myReq = new GsonRequest<>(
                PATH_JSON_RECIPE,
                Recipe[].class,
                null,
                requestSuccessListener,
                requestErrorListener());

        queue.add(myReq);

    }



    private Response.ErrorListener requestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.getMessage());
            }
        };
    }
}

