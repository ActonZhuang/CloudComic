package com.wallf.cloudcomic.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallf.cloudcomic.R;
import com.wallf.cloudcomic.activity.ComicPagerActivityFragment;
import com.wallf.cloudcomic.adapter.BookCoverAdapter;
import com.wallf.cloudcomic.entity.ComicCover;
import com.wallf.cloudcomic.utils.ViewUtil;
import com.wallf.cloudcomic.views.ContentScrollListener;

import java.util.logging.Logger;

/**
 * Created by dell on 2015/12/10.
 */
public class MainFragment extends Fragment implements BookCoverAdapter.OnItemClickListener {


    private ContentScrollListener contentScrollListener;


    public static MainFragment newInstance() {

        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContentScrollListener)
            contentScrollListener = (ContentScrollListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_cover_list, container, false);
    }

    RecyclerView mRecyclerView;
    BookCoverAdapter mAdapter;

//    View mToolbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);

//        mToolbar = view.findViewById(R.id.toolbar);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.coverlist);

        setupRecyclerView();
    }

    private final int COLUMN_SPAN = 2;
    private final int ITEM_SPACE = 4;

    void setupRecyclerView() {

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(COLUMN_SPAN, StaggeredGridLayoutManager.VERTICAL));

        mAdapter = new BookCoverAdapter(getActivity());
        mAdapter.setImageWorker(getFragmentManager());

        int cellWidth = itemWidth();
        mAdapter.setImageSize(cellWidth, cellWidth);
        mAdapter.setListener(this);

        mRecyclerView.addItemDecoration(new SpacesItemDecoration(ViewUtil.dip2px(getActivity(), ITEM_SPACE)));

        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener(mRecyclerView.getPaddingTop()));

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(ComicCover cover) {

        Intent intent = new Intent(getActivity(), ComicPagerActivityFragment.class);
        startActivity(intent);
    }

    class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        int lastDy;
        boolean flag;

        int paddingTop = 0;

        public RecyclerViewScrollListener(int padding) {
            paddingTop = padding;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

//        if (toolbar == null)
//            throw new IllegalStateException("BooksFragment has not a reference of the main toolbar");
            int position = recyclerView.computeVerticalScrollOffset();

            Logger.getLogger(MainFragment.class.getName()).info(position + "");
            // Is scrolling up
            if (dy > 10 && position >= paddingTop) {

                if (!flag) {

                    showView();
                    flag = true;
                }

                // is scrolling down
            } else if (dy < -10) {

                if (flag) {

                    hideView();
                    flag = false;
                }
            }

            lastDy = dy;
        }

        public void showView() {
//            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(),
//                    R.anim.translate_up_in));
            if (contentScrollListener != null)
                contentScrollListener.onScroll(ContentScrollListener.DOWN, 0);
        }

        public void hideView() {
//            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(),
//                    R.anim.translate_up_out));
            if (contentScrollListener != null)
                contentScrollListener.onScroll(ContentScrollListener.UP, 0);
        }
    }

    int itemWidth() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        int cellWidth = width / COLUMN_SPAN - ViewUtil.dip2px(getActivity(), 4f) * COLUMN_SPAN * 2;

        return cellWidth;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(space, space, space, space);
        }
    }
}
