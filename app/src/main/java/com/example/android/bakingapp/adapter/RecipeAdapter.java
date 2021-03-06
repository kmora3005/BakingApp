package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.provider.RecipeProvider;
import com.example.android.bakingapp.ui.IngredientsAndStepsActivity;
import com.example.android.bakingapp.ui.MainActivity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.ui.IngredientsAndStepsActivity.STEPS_PROJECTION;
import static com.example.android.bakingapp.ui.MainActivity.COL_NUM_ID;
import static com.example.android.bakingapp.ui.MainActivity.COL_NUM_IMAGE;
import static com.example.android.bakingapp.ui.MainActivity.COL_NUM_NAME;

/**
 * Project name BakingApp
 * Created by kenneth on 27/08/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private Cursor mCursor;
    private Context mContext;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler clickHandler){
        mContext = context;
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

        int id = mCursor.getInt(COL_NUM_ID);
        String name = mCursor.getString(COL_NUM_NAME);
        int servings = mCursor.getInt(MainActivity.COL_NUM_SERVINGS);
        String urlImage = mCursor.getString(COL_NUM_IMAGE);
        holder.tvName.setText(name);
        holder.tvServings.setText(String.format(Locale.US,"%d", servings) );
        boolean isThereImage = !TextUtils.isEmpty(urlImage);
        if (isThereImage){
            Glide.with(mContext).load(urlImage).placeholder(R.mipmap.ic_launcher).into(holder.ivImage);
        }
        else {
            Cursor cursorSteps = mContext.getContentResolver().query(RecipeProvider.Steps.withIdRecipe(id),
                    STEPS_PROJECTION, null, null, null);
            if (cursorSteps != null) {
                cursorSteps.moveToPosition(0);
                String videoUrl = cursorSteps.getString(IngredientsAndStepsActivity.COL_NUM_VIDEO_URL);
                cursorSteps.close();

                try {
                    ByteArrayOutputStream stream = retriveVideoFrameFromVideo(videoUrl);
                    Glide.with(mContext).load(stream.toByteArray()).asBitmap().placeholder(R.mipmap.ic_launcher).into(holder.ivImage);
                } catch (Throwable throwable) {
                    Log.e(TAG,"It can't generate thumbnail");
                    throwable.printStackTrace();
                }
            }
        }
    }

    private static ByteArrayOutputStream retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();

            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());

            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream;
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

    public int firstIdRecipe(){
        mCursor.moveToPosition(0);
        if (getItemCount()>0)
            return mCursor.getInt(COL_NUM_ID);
        return 0;
    }

    public String firstNameRecipe(){
        mCursor.moveToPosition(0);
        if (getItemCount()>0)
            return mCursor.getString(COL_NUM_NAME);
        return "...";
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
            int id = mCursor.getInt(COL_NUM_ID);
            String name = mCursor.getString(COL_NUM_NAME);
            mClickHandler.onClick(id, name);
        }
    }
}
