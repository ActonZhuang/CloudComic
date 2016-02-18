package com.wallf.cloudcomic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * @author acton
 */
public class ReboundViewFragment extends Fragment {

//    private RecyclerView mPhotosGrid;

    public static ReboundViewFragment newInstance() {
        Bundle args = new Bundle();
        ReboundViewFragment fragment = new ReboundViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reboundview, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {
        RecyclerView mPhotosGrid = (RecyclerView) rootView.findViewById(R.id.photos_grid);
        mPhotosGrid.setLayoutManager(new CustomStaggeredGridLayoutManager());
        mPhotosGrid.setAdapter(new CustomPhotoAdapter());
    }

    public static class CustomPhotoAdapter extends RecyclerView.Adapter<CustomPhotoViewHolder> {

        private final int[] mImageResources = new int[]{
                R.drawable.jellybean_bg,
                R.drawable.lolipop_bg,
                R.drawable.jellybean_bg,
                R.drawable.lolipop_bg,
                R.drawable.jellybean_bg,
                R.drawable.lolipop_bg,
                R.drawable.jellybean_bg,
                R.drawable.lolipop_bg,
        };

        @Override
        public CustomPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_photo_grid, parent, false);
            CustomPhotoViewHolder vh = new CustomPhotoViewHolder(itemView);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.photo_grid);
            vh.setImageView(imageView);
//            vh.setImageView(null);
            return vh;
        }

        @Override
        public void onBindViewHolder(CustomPhotoViewHolder holder, int position) {
            ImageView imgView = holder.getImageView();
            imgView.setImageResource(mImageResources[position]);
        }

        @Override
        public int getItemCount() {
            return mImageResources.length;
        }
    }

    public static class CustomPhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView mInnerImage;

        public CustomPhotoViewHolder(View itemView) {
            super(itemView);
        }

        public void setImageView(@NonNull ImageView imageView) {
            mInnerImage = imageView;
        }

        @NonNull
        public ImageView getImageView() {
            return mInnerImage;
        }
    }

    public static class CustomStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

        public CustomStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public CustomStaggeredGridLayoutManager() {
            super(2, StaggeredGridLayoutManager.VERTICAL);
        }
    }
}
