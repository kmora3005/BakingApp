package com.example.android.bakingapp.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Project name BakingApp
 * Created by kenneth on 26/08/2017.
 */
@Database(version = RecipeDatabase.VERSION)
 class RecipeDatabase {
    public static final int VERSION = 1;

    @Table(RecipeContract.class)
    public static final String RECIPES = "recipes";

    @Table(IngredientContract.class)
    public static final String INGREDIENTS = "ingredients";

    @Table(StepContract.class)
    public static final String STEPS = "steps";
}
