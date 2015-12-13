package com.wallf.cloudcomic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallf.cloudcomic.R;
import com.wallf.cloudcomic.adapter.BookCoverAdapter;

/**
 * Created by dell on 2015/12/10.
 */
public class MainFragment extends Fragment {
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

    void setupRecyclerView() {

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter = new BookCoverAdapter(getActivity()));
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
}
