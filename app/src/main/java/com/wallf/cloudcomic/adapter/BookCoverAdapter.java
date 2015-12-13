package com.wallf.cloudcomic.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wallf.cloudcomic.R;
import com.wallf.cloudcomic.imageloader.ImageFetcher;
import com.wallf.cloudcomic.imageloader.ImageResizer;

import java.util.List;

/**
 * Created by dell on 2015/12/10.
 */
public class BookCoverAdapter extends RecyclerView.Adapter<BookCoverAdapter.CoverViewHolder> {

    Context mContext;

    ImageResizer mImageWorker;

    List<ComicImage> mItems;

    public BookCoverAdapter(Context context) {
        mContext = context;
        mImageWorker = new ImageFetcher(mContext, 180);
    }

    public void setImageSize(int width, int height) {
        mImageWorker.setImageSize(width,height);
    }


    @Override
    public CoverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_cover, parent, false);
        CoverViewHolder holder = new CoverViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CoverViewHolder holder, int position) {
        ComicImage data = mItems.get(position);
        mImageWorker.loadImage(data.getUrl(), holder.cover);
    }

    @Override
    public int getItemCount() {
        return (mItems == null) ? 0 : mItems.size();
    }

    public class CoverViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView cover;

        public CoverViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardview);
            cover = (ImageView) itemView.findViewById(R.id.cover);
        }
    }


    public class ComicImage {

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String url;
    }
}
