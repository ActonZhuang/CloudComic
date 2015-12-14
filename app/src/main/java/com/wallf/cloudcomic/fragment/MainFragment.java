package com.wallf.cloudcomic.fragment;

import android.content.Context;
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
import com.wallf.cloudcomic.adapter.BookCoverAdapter;
import com.wallf.cloudcomic.utils.ViewUtil;

/**
 * Created by dell on 2015/12/10.
 */
public class MainFragment extends Fragment {


    public static MainFragment newInstance() {

        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_cover, container, false);
    }

    RecyclerView mRecyclerView;
    BookCoverAdapter mAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.coverlist);
        setupRecyclerView();
    }

    private final int COLUMN_SPAN = 2;

    void setupRecyclerView() {

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(COLUMN_SPAN, StaggeredGridLayoutManager.VERTICAL));

        mAdapter = new BookCoverAdapter(getActivity());
        mAdapter.setImageWorker(getFragmentManager());

        int cellWidth = itemWidth();
        mAdapter.setImageSize(cellWidth, cellWidth);

        mRecyclerView.addItemDecoration(new SpacesItemDecoration(ViewUtil.dip2px(getActivity(), 4f)));

        mRecyclerView.setAdapter(mAdapter);
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
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }
}
