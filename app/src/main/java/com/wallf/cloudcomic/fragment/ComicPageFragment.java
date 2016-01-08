package com.wallf.cloudcomic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wallf.cloudcomic.R;
import com.wallf.cloudcomic.entity.ComicPage;
import com.wallf.cloudcomic.imageloader.ImageCache;
import com.wallf.cloudcomic.imageloader.ImageFetcher;
import com.wallf.cloudcomic.imageloader.ImageWorker;
import com.wallf.cloudcomic.utils.ScreenUtil;

import java.io.Serializable;

/**
 * Created by Fei.Liu on 2016/1/8.
 */
public class ComicPageFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.page_image) {
            if (mListener != null)
                mListener.onViewClicked();
        }
    }

    public interface ComicPageFragmentListener {
        public void onViewClicked();
    }

    public final static String ARGS_COMIC_PAGE = "comic_page";

    public static ComicPageFragment newInstance(ComicPage page) {
        ComicPageFragment fragment = new ComicPageFragment();
        if (page != null) {
            Bundle args = new Bundle();
            args.putSerializable(ARGS_COMIC_PAGE, page);
            fragment.setArguments(args);
        }
        return fragment;
    }

    ImageWorker mImageWorker;

    ImageWorker getImageWorker() {
        if (mImageWorker == null) {
            Context context = getActivity();

            ImageCache.ImageCacheParams cacheParams =
                    new ImageCache.ImageCacheParams(getActivity(), "page");

            cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

            // The ImageFetcher takes care of loading images into our ImageView children asynchronously
            mImageWorker = new ImageFetcher(context, ScreenUtil.getWpx(context), ScreenUtil.getHpx(context));
//        mImageWorker.setLoadingImage(R.drawable.empty_photo);
            mImageWorker.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
        }
        return mImageWorker;
    }

    ImageView pageImg;

    private ComicPageFragmentListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ComicPageFragmentListener)
            mListener = (ComicPageFragmentListener) context;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pageImg = (ImageView) view.findViewById(R.id.page_image);

        pageImg.setOnClickListener(this);

        if (getComicPage() != null) {
            getImageWorker().loadImage(getComicPage().getUrl(), pageImg);
        }
    }

    ComicPage mComicPage;

    ComicPage getComicPage() {
        if (mComicPage == null && getArguments() != null) {
            Serializable data = getArguments().getSerializable(ARGS_COMIC_PAGE);
            if (data != null) {
                mComicPage = (ComicPage) data;
            }
        }
        return mComicPage;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_page, container, false);
    }
}
