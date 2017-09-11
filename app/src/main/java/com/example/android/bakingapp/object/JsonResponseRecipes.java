package com.example.android.bakingapp.object;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Project name BakingApp
 * Created by kenneth on 26/08/2017.
 */

public class JsonResponseRecipes {
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

    public static Recipe[] getRecipes()  {
        Gson gson = new Gson();

        try {
            String json = readUrl();
            return gson.fromJson(json, Recipe[].class);

        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}

