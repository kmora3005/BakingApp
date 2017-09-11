package com.example.android.bakingapp.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Project name BakingApp
 * Created by kenneth on 26/08/2017.
 */

public class RecipeContract {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_NAME = "name";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_SERVINGS = "servings";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_IMAGE = "image";
}
