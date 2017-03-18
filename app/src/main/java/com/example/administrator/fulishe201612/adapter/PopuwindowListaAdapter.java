package com.example.administrator.fulishe201612.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.model.bean.CategoryChildBean;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;

import java.util.ArrayList;

public class PopuwindowListaAdapter extends BaseAdapter {

    Context context;
    ArrayList<CategoryChildBean> childList;

    public PopuwindowListaAdapter(Context context, ArrayList<CategoryChildBean> childList) {
        this.context = context;
        this.childList = childList;
    }

    @Override
    public int getCount() {
        return childList.size();
    }

    @Override
    public CategoryChildBean getItem(int position) {
        return childList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryChildBean app = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {//整理创建第一屏的列表项
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_child, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageChild);
            holder.textView = (TextView) convertView.findViewById(R.id.textChildName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.textView.setText(app.getName());
        ImageLoader.downloadImg(context, holder.imageView, app.getImageUrl());
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
