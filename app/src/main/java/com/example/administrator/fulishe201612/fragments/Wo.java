package com.example.administrator.fulishe201612.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.administrator.fulishe201612.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Wo extends Fragment {


    @BindView(R.id.toolbar_persion)
    Toolbar toolbarPersion;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    public Toolbar getToolbarPersion() {
        return toolbarPersion;
    }

    public Wo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wo, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarPersion);
        initView(view);

        return view;
    }

    private void initView(View view) {

    }


}
