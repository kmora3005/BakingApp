package com.example.android.bakingapp.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.IngredientsAndStepsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Project name BakingApp
 * Created by kenneth on 28/08/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private Cursor mCursor;
    private final StepAdapter.StepAdapterOnClickHandler mClickHandler;
    private final SparseBooleanArray selectedItems;

    public StepAdapter(StepAdapter.StepAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step_card, parent, false);

        return new StepViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.flStepCardContainer.setSelected(selectedItems.get(position, false));
        String description = mCursor.getString(IngredientsAndStepsActivity.COL_NUM_SHORT_DESCRIPTION);
        holder.tvShortDescription.setText(description);

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

    public void nextPosition() {
        int pos = mCursor.getCount() <= mCursor.getPosition() + 1 ? 0 : mCursor.getPosition() + 1;
        selectedItems.put(pos, true);
        mCursor.moveToPosition(pos);
    }

    public void setPosition(int pos) {
        mCursor.moveToPosition(pos);
    }

    public String currentDescription() {
        return mCursor.getString(IngredientsAndStepsActivity.COL_NUM_DESCRIPTION);
    }

    public String currentVideoUrl() {
        return mCursor.getString(IngredientsAndStepsActivity.COL_NUM_VIDEO_URL);
    }

    public String currentThumbnailUrl() {
        return mCursor.getString(IngredientsAndStepsActivity.COL_NUM_THUMBNAIL_URL);
    }

    public interface StepAdapterOnClickHandler {
        void onClick(int idStep, String description, String videoUrl, String thumbnailUrl);
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_short_description)
        TextView tvShortDescription;
        @BindView(R.id.fl_step_card_container)
        FrameLayout flStepCardContainer;

        private StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            selectedItems.put(adapterPosition, true);
            flStepCardContainer.setSelected(true);

            mCursor.moveToPosition(adapterPosition);
            String description = mCursor.getString(IngredientsAndStepsActivity.COL_NUM_DESCRIPTION);
            String videoUrl = mCursor.getString(IngredientsAndStepsActivity.COL_NUM_VIDEO_URL);
            String thumbnailUrl = mCursor.getString(IngredientsAndStepsActivity.COL_NUM_THUMBNAIL_URL);
            mClickHandler.onClick(mCursor.getPosition(), description, videoUrl, thumbnailUrl);
        }
    }
}
