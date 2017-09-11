package com.example.android.bakingapp.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.MainActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Project name BakingApp
 * Created by kenneth on 27/08/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Cursor mCursor;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler){
        mClickHandler=clickHandler;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_card, parent, false);

        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String name = mCursor.getString(MainActivity.COL_NUM_NAME);
        int servings = mCursor.getInt(MainActivity.COL_NUM_SERVINGS);
        holder.tvName.setText(name);
        holder.tvServings.setText(String.format(Locale.US,"%d", servings) );
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

    public interface RecipeAdapterOnClickHandler {
        void onClick(int id, String name);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.tv_servings) TextView tvServings;
        @BindView(R.id.iv_image) ImageView ivImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int id = mCursor.getInt(MainActivity.COL_NUM_ID);
            String name = mCursor.getString(MainActivity.COL_NUM_NAME);
            mClickHandler.onClick(id, name);
        }
    }
}
