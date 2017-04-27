package com.example.administrator.fulishe201612.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.model.bean.CategoryGroupBean;
import com.example.administrator.fulishe201612.model.net.CateListModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CateListFragment extends Fragment {
    private ListView listView;
    ArrayList<CategoryGroupBean> categoryGroupBeen;
    CateListModel cateListModel;


    public CateListFragment() {
        // Required empty public constructor
    }

    ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_cate_list, container, false);
        cateListModel = new CateListModel();
        categoryGroupBeen = new ArrayList<>();
        initData();
        listView = (ListView) inflate.findViewById(R.id.listview_cateList);
        listAdapter = new ListAdapter(getContext(), categoryGroupBeen);
        listView.setAdapter(listAdapter);
        initListener();
        return inflate;
    }

    private void initListener() {
        Log.e("监听执行", "initlistener");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer parentId = categoryGroupBeen.get(position).getId();
                Log.e("点击执行", "parentId==" + parentId);
                EventBus.getDefault().post(new Integer(parentId));
            }
        });
    }

    private void initData() {
        cateListModel.loadData(getActivity(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                ArrayList<CategoryGroupBean> list = OkHttpUtils.array2List(result);
                listAdapter.initContactgroup(list);

            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ListAdapter extends BaseAdapter {
        Context context;

        public ListAdapter(Context context, ArrayList<CategoryGroupBean> categoryGroupBeen) {

            this.context = context;
            this.categoryGroupBeen = categoryGroupBeen;
        }

        ArrayList<CategoryGroupBean> categoryGroupBeen;

        @Override
        public int getCount() {
            return categoryGroupBeen.size();
        }

        @Override
        public CategoryGroupBean getItem(int position) {
            return categoryGroupBeen.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View inflate;
            if (convertView == null) {
                holder = new ViewHolder();
                inflate = View.inflate(context, R.layout.categoty_adapter_group, null);
                holder.textGorup = (TextView) inflate.findViewById(R.id.text_gorup);
                holder.imageGroup = (ImageView) inflate.findViewById(R.id.image_group);
                inflate.setTag(holder);

            } else {
                inflate = convertView;
                holder = (ViewHolder) inflate.getTag();
            }
            CategoryGroupBean bean = categoryGroupBeen.get(position);

            ImageLoader.downloadImg(context, holder.imageGroup, bean.getImageUrl());
            holder.textGorup.setText(bean.getName());
            return inflate;
        }


        class ViewHolder {

            ImageView imageGroup;

            TextView textGorup;


        }

        public void initContactgroup(ArrayList<CategoryGroupBean> goodsList) {
            this.categoryGroupBeen.clear();

            this.categoryGroupBeen.addAll(goodsList);
            notifyDataSetChanged();

        }
    }

}
