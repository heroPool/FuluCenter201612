package com.example.administrator.fulishe201612.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.adapter.ExpandableAdapter;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.CategoryChildBean;
import com.example.administrator.fulishe201612.model.bean.CategoryGroupBean;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fenlei extends Fragment {
    ExpandableListView expandableListView;

    ArrayList<CategoryGroupBean> categoryGroupBeen;
    ArrayList<ArrayList<CategoryChildBean>> categoryChildBeen;


    ExpandableAdapter expandableAdapter;

    public Fenlei() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_fenlei, container, false);

        categoryGroupBeen = new ArrayList<>();
        categoryChildBeen = new ArrayList<>();


        initView(inflate);
        initData();
        return inflate;
    }

    private void initData() {

        final OkHttpUtils<CategoryGroupBean[]> okHttpUtils = new OkHttpUtils<>(getActivity());
        okHttpUtils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
                    @Override
                    public void onSuccess(CategoryGroupBean[] result) {
                        ArrayList<CategoryGroupBean> list = okHttpUtils.array2List(result);
                        expandableAdapter.initContactgroup(list);
                        for (CategoryGroupBean l : list) {
                            int id = l.getId();
                            downloadChildcate(okHttpUtils,id);
                        }
                        expandableAdapter.initContactchild(categoryChildBeen);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
//        ArrayList<CategoryGroupBean> categoryGroupBeenL = expandableAdapter.getCategoryGroupBeen();
//        for (int i = 0; i < categoryGroupBeenL.size(); i++) {
//            CategoryGroupBean bean = categoryGroupBeenL.get(i);
//            int id = bean.getId();
//            Log.i("main", id + "");
//            downloadChildcate(id);
//        }
    }

    private void downloadChildcate(final OkHttpUtils okHttpUtils, final int parentId) {

        okHttpUtils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, parentId + "")
                .targetClass(CategoryChildBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onSuccess(CategoryChildBean[] result) {
                        ArrayList<CategoryChildBean> list = okHttpUtils.array2List(result);
                        categoryChildBeen.add(list);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initView(View inflate) {
        expandableListView = (ExpandableListView) inflate.findViewById(R.id.expandableListView);
        expandableAdapter = new ExpandableAdapter(getActivity(), categoryGroupBeen, categoryChildBeen);
        expandableListView.setAdapter(expandableAdapter);
    }

}
