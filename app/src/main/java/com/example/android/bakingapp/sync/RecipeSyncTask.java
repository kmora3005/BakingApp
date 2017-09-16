package com.example.android.bakingapp.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.android.volley.Response;
import com.example.android.bakingapp.object.Ingredient;
import com.example.android.bakingapp.object.JsonResponseRecipes;
import com.example.android.bakingapp.object.Recipe;
import com.example.android.bakingapp.object.Step;
import com.example.android.bakingapp.provider.IngredientContract;
import com.example.android.bakingapp.provider.RecipeContract;
import com.example.android.bakingapp.provider.RecipeProvider;
import com.example.android.bakingapp.provider.StepContract;

/**
 * Project name BakingApp
 * Created by kenneth on 27/08/2017.
 */

class RecipeSyncTask {

    synchronized static void syncRecipes(final Context context){

        new JsonResponseRecipes().getRecipes(context, new Response.Listener<Recipe[]> () {
                @Override
                public void onResponse(Recipe[] recipes) {
                    ContentResolver contentResolver = context.getContentResolver();
                    ContentValues[] cvIngredients;
                    ContentValues[] cvSteps;
                    if ((recipes != null ? recipes.length : 0) !=0)
                    {
                        contentResolver.delete(RecipeProvider.Recipes.CONTENT_URI, null, null);
                        contentResolver.delete(RecipeProvider.Ingredients.CONTENT_URI, null, null);
                        contentResolver.delete(RecipeProvider.Steps.CONTENT_URI, null, null);
                    }
                    for (Recipe recipe : recipes != null ? recipes : new Recipe[0]) {
                        ContentValues cvRecipe = new ContentValues();
                        cvRecipe.put(RecipeContract.COLUMN_NAME, recipe.name);
                        cvRecipe.put(RecipeContract.COLUMN_SERVINGS, recipe.servings);
                        cvRecipe.put(RecipeContract.COLUMN_IMAGE, recipe.image);

                        Uri result = contentResolver.insert(RecipeProvider.Recipes.CONTENT_URI, cvRecipe);
                        int id = Integer.parseInt(result != null ? result.getLastPathSegment() : null);
                        cvIngredients = new ContentValues[recipe.ingredients.size()];
                        for (int indexIngredient = 0; indexIngredient < recipe.ingredients.size(); indexIngredient++) {
                            Ingredient ingredient = recipe.ingredients.get(indexIngredient);
                            ContentValues cvIngredient = new ContentValues();
                            cvIngredient.put(IngredientContract.COLUMN_ID_RECIPE, id);
                            cvIngredient.put(IngredientContract.COLUMN_INGREDIENT, ingredient.ingredient);
                            cvIngredient.put(IngredientContract.COLUMN_MEASURE, ingredient.measure);
                            cvIngredient.put(IngredientContract.COLUMN_QUANTITY, ingredient.quantity);
                            cvIngredients[indexIngredient] = cvIngredient;
                        }
                        if (cvIngredients.length != 0) {
                            contentResolver.bulkInsert(RecipeProvider.Ingredients.CONTENT_URI, cvIngredients);
                        }

                        cvSteps = new ContentValues[recipe.steps.size()];
                        for (int indexStep = 0; indexStep < recipe.steps.size(); indexStep++) {
                            Step step = recipe.steps.get(indexStep);
                            ContentValues cvStep = new ContentValues();
                            cvStep.put(StepContract.COLUMN_ID_RECIPE, id);
                            cvStep.put(StepContract.COLUMN_DESCRIPTION, step.description);
                            cvStep.put(StepContract.COLUMN_SHORT_DESCRIPTION, step.shortDescription);
                            cvStep.put(StepContract.COLUMN_THUMBNAIL_URL, step.thumbnailURL);
                            cvStep.put(StepContract.COLUMN_VIDEO_URL, step.videoURL);
                            cvSteps[indexStep] = cvStep;
                        }
                        if (cvSteps.length != 0) {
                            contentResolver.bulkInsert(RecipeProvider.Steps.CONTENT_URI, cvSteps);
                        }
                    }
                }
            });


    }


}
