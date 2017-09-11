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

public class StepContract {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_SHORT_DESCRIPTION = "short_description";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_DESCRIPTION = "description";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_VIDEO_URL = "video_url";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    @References(table = RecipeDatabase.RECIPES,column = RecipeContract.COLUMN_ID)
    public static final String COLUMN_ID_RECIPE = "id_recipe";
}
