package com.example.ximalaya.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ximalaya.R;
import com.example.ximalaya.base.BaseFragment;

public class HistoryFragment extends BaseFragment {

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        View rootView = layoutInflater.inflate(R.layout.fragment_subscription,null,false);
        return rootView;
    }
}
