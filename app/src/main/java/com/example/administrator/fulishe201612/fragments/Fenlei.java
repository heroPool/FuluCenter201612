package com.example.administrator.fulishe201612.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.adapter.ExpandableAdapter;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.CategoryChildBean;
import com.example.administrator.fulishe201612.model.bean.CategoryGroupBean;
import com.example.administrator.fulishe201612.model.net.CateListModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;
import com.example.administrator.fulishe201612.ui.activity.BoutiqueAndListActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fenlei extends Fragment {
    ExpandableListView expandableListView;

    ArrayList<CategoryGroupBean> categoryGroupBeen;
    ArrayList<ArrayList<CategoryChildBean>> categoryChildBeen;


    ExpandableAdapter expandableAdapter;
    @BindView(R.id.toobar_fenlei)
    Toolbar toobarFenlei;

    public Fenlei() {

    }

    CateListModel cateListModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_fenlei, container, false);
        cateListModel = new CateListModel();
        categoryGroupBeen = new ArrayList<>();
        categoryChildBeen = new ArrayList<>();
        ButterKnife.bind(this, inflate);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toobarFenlei);
        initData();
        initView(inflate);
        return inflate;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_fenlei, menu);

    }

    private void initData() {
        cateListModel.loadData(getActivity(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                ArrayList<CategoryGroupBean> list = OkHttpUtils.array2List(result);

                for (int i = 0; i < list.size(); i++) {
                    int id = list.get(i).getId();
                    categoryChildBeen.add(new ArrayList<CategoryChildBean>());
                    downloadChildcate(id, i);
                }
                expandableAdapter.initContactgroup(list);
                expandableAdapter.initContactchild(categoryChildBeen);

            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void downloadChildcate(final int parentId, final int index) {

        final OkHttpUtils<CategoryChildBean[]> okHttpUtils = new OkHttpUtils<>(getActivity());
        okHttpUtils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, parentId + "")
                .targetClass(CategoryChildBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onSuccess(CategoryChildBean[] result) {
                        ArrayList<CategoryChildBean> list = okHttpUtils.array2List(result);
                        categoryChildBeen.set(index, list);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private static final String TAG = "Fenlei";

    private void initView(View inflate) {
        expandableListView = (ExpandableListView) inflate.findViewById(R.id.expandableListView);
        expandableAdapter = new ExpandableAdapter(getActivity(), categoryGroupBeen, categoryChildBeen);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ArrayList<CategoryChildBean> categoryChildBeen = Fenlei.this.categoryChildBeen.get(groupPosition);
                String groupItem = categoryGroupBeen.get(groupPosition).getName();

                Log.i("popu", "bfore:" + categoryChildBeen.toString());
                FuLiCenterApplication.getInstance().setIndex(2);
                Log.i(TAG, "setindex=" + 2);
                startActivity(new Intent(getActivity(), BoutiqueAndListActivity.class)

                        .putExtra("title", Fenlei.this.categoryChildBeen.get(groupPosition).get(childPosition).getName())
                        .putExtra(I.NewAndBoutiqueGoods.CAT_ID, Fenlei.this.categoryChildBeen.get(groupPosition).get(childPosition).getId())
                        .putExtra("boolea", "1")
                        .putExtra("childList", categoryChildBeen)
                        .putExtra("groupItem", groupItem)
                );

                return false;
            }
        });
    }

}
