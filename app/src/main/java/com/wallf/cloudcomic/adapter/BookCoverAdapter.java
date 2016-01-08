package com.wallf.cloudcomic.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallf.cloudcomic.R;
import com.wallf.cloudcomic.entity.ComicCover;
import com.wallf.cloudcomic.imageloader.ImageCache;
import com.wallf.cloudcomic.imageloader.ImageFetcher;
import com.wallf.cloudcomic.imageloader.ImageResizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2015/12/10.
 */
public class BookCoverAdapter extends RecyclerView.Adapter<BookCoverAdapter.CoverViewHolder> {

    Context mContext;

    ImageResizer mImageWorker;

    List<ComicCover> mItems;

    public BookCoverAdapter(Context context) {
        mContext = context;
//        mImageWorker = new ImageFetcher(mContext, 180);
        initialData();
    }

    private final static String IMAGE_CACHE_DIR = "cover";
    private final static int IMAGETHUMBSIZE = 180;

    public void setImageWorker(FragmentManager fragmentManager) {

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(mContext, IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageWorker = new ImageFetcher(mContext, IMAGETHUMBSIZE);
//        mImageWorker.setLoadingImage(R.drawable.empty_photo);
        mImageWorker.addImageCache(fragmentManager, cacheParams);

    }

    void initialData() {
        mItems = new ArrayList<>();
        for (String str : IMAGES) {
            mItems.add(new ComicCover(str, str.substring(str.length() - 10)));
        }
    }

    public void setImageSize(int width, int height) {
        mImageWorker.setImageSize(width, height);
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
        final ComicCover data = mItems.get(position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onItemClick(data);
            }
        });

        holder.title.setText(data.getTitle());
        mImageWorker.loadImage(data.getUrl(), holder.cover);
    }

    @Override
    public int getItemCount() {
        return (mItems == null) ? 0 : mItems.size();
    }

    public class CoverViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView cover;
        TextView title;

        public CoverViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardview);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }


    final static String[] IMAGES = new String[]{
            "http://b.hiphotos.baidu.com/zhidao/pic/item/0bd162d9f2d3572ca80cc2978913632763d0c38b.jpg",
            "http://b.hiphotos.baidu.com/zhidao/pic/item/cf1b9d16fdfaaf512a33009c8f5494eef11f7ac5.jpg",
            "http://pic.baike.soso.com/p/20130903/20130903092527-1288143690.jpg",
            "http://www.tradeduck.com/uploads/22752_10501473.jpg",
            "http://www.wallcoo.com/cg/Gift/wallpapers/147525874_ad09f8fa28_o.jpg",
            "http://e.hiphotos.baidu.com/zhidao/pic/item/a8014c086e061d9566deb1df7af40ad163d9cab9.jpg",
            "http://static.bbs.sgnet.cc/attachment/forum/201012/09/184929s087he0h4ybsbx6h.jpg",
            "http://news.5068.com/upfiles/allimg/110316/153Q344S-2.jpg"
    };

    private OnItemClickListener mListener;

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(ComicCover cover);
    }
}
