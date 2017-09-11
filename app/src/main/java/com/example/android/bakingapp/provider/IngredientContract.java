package com.example.android.bakingapp.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Project name BakingApp
 * Created by kenneth on 26/08/2017.
 */

public class IngredientContract {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.REAL)
    @NotNull
    public static final String COLUMN_QUANTITY = "quantity";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_MEASURE = "measure";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_INGREDIENT = "ingredient";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    @References(table = RecipeDatabase.RECIPES,column = RecipeContract.COLUMN_ID)
    public static final String COLUMN_ID_RECIPE = "id_recipe";
}
