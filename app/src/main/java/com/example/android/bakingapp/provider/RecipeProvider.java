package com.example.android.bakingapp.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Project name BakingApp
 * Created by kenneth on 26/08/2017.
 */
@ContentProvider(
        authority = RecipeProvider.AUTHORITY,
        database = RecipeDatabase.class)
public class RecipeProvider {
    public static final String AUTHORITY = "com.example.android.bakingapp.provider.RecipeProvider";

    @TableEndpoint(table = RecipeDatabase.RECIPES)
    public static class Recipes {
        @ContentUri(
                path = "recipes",
                type = "vnd.android.cursor.dir/recipes",
                defaultSort = RecipeContract.COLUMN_NAME + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/recipes");

        @InexactContentUri(
                path = "recipes/#",
                name = "RECIPE_ID",
                type = "vnd.android.cursor.item/recipe",
                join = "JOIN " + RecipeDatabase.INGREDIENTS + " ON " + RecipeDatabase.INGREDIENTS + "." +IngredientContract.COLUMN_ID_RECIPE+ " = " +RecipeDatabase.RECIPES+ "." +IngredientContract.COLUMN_ID
                    +" JOIN " + RecipeDatabase.STEPS + " ON " + RecipeDatabase.STEPS + "." +StepContract.COLUMN_ID_RECIPE+ " = " +RecipeDatabase.STEPS+ "." +IngredientContract.COLUMN_ID,
                whereColumn = RecipeContract.COLUMN_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/recipes/" + id);
        }
    }

    @TableEndpoint(table = RecipeDatabase.INGREDIENTS)
    public static class Ingredients {
        @ContentUri(
                path = "ingredients",
                type = "vnd.android.cursor.dir/ingredients")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/ingredients");

        @InexactContentUri(
                path = "ingredients/#",
                name = "INGREDIENT_ID",
                type = "vnd.android.cursor.item/ingredient",
                whereColumn = IngredientContract.COLUMN_ID_RECIPE,
                pathSegment = 1)
        public static Uri withIdRecipe(long id) {
            return Uri.parse("content://" + AUTHORITY + "/ingredients/" + id);
        }
    }

    @TableEndpoint(table = RecipeDatabase.STEPS)
    public static class Steps {
        @ContentUri(
                path = "steps",
                type = "vnd.android.cursor.dir/steps")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/steps");

        @InexactContentUri(
                path = "steps/#",
                name = "STEP_ID",
                type = "vnd.android.cursor.item/step",
                whereColumn = StepContract.COLUMN_ID_RECIPE,
                pathSegment = 1)
        public static Uri withIdRecipe(long id) {
            return Uri.parse("content://" + AUTHORITY + "/steps/" + id);
        }

    }


}
