package com.example.android.bakingapp.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.IngredientsAndStepsActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Project name BakingApp
 * Created by kenneth on 28/08/2017.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private Cursor mCursor;

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        float quantity = mCursor.getFloat(IngredientsAndStepsActivity.COL_NUM_QUANTITY);
        String measure = mCursor.getString(IngredientsAndStepsActivity.COL_NUM_MEASURE);
        String ingredient = mCursor.getString(IngredientsAndStepsActivity.COL_NUM_INGREDIENT);
        holder.tvQuantity.setText(String.format(Locale.US,"%.1f", quantity) );
        holder.tvMeasure.setText(measure);
        holder.tvIngredient.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_quantity) TextView tvQuantity;
        @BindView(R.id.tv_measure) TextView tvMeasure;
        @BindView(R.id.tv_ingredient) TextView tvIngredient;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


    }
}
