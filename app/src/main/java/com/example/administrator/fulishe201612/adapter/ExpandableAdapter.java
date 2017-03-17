package com.example.administrator.fulishe201612.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.model.bean.CategoryChildBean;
import com.example.administrator.fulishe201612.model.bean.CategoryGroupBean;
import com.example.administrator.fulishe201612.model.bean.NewGoodsBean;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by Administrator on 2017/3/15.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<CategoryGroupBean> categoryGroupBeen;
    ArrayList<ArrayList<CategoryChildBean>> categoryChildBeen;

    public ExpandableAdapter(Context context, ArrayList<CategoryGroupBean> categoryGroupBeen,
                             ArrayList<ArrayList<CategoryChildBean>> categoryChildBeen) {
        this.context = context;
        this.categoryGroupBeen = categoryGroupBeen;
        this.categoryChildBeen = categoryChildBeen;
    }


    @Override

    public int getGroupCount() {
        return categoryGroupBeen.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categoryChildBeen.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryGroupBeen.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categoryChildBeen.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = View.inflate(context, R.layout.item_group, null);

            holder.textGroupName = (TextView) convertView.findViewById(R.id.textGroupName);
            holder.imageGroup = (ImageView) convertView.findViewById(R.id.imageGroup);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        CategoryGroupBean bean = categoryGroupBeen.get(groupPosition);
        holder.textGroupName.setText(bean.getName());
        ImageLoader.downloadImg(context, holder.imageGroup, bean.getImageUrl());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildeHolder holder = null;
        if (convertView == null) {
            holder = new ChildeHolder();
            convertView = View.inflate(context, R.layout.item_child, null);

            holder.textChildname = (TextView) convertView.findViewById(R.id.textChildName);
            holder.imageChild = (ImageView) convertView.findViewById(R.id.imageChild);
            convertView.setTag(holder);

        } else {
            holder = (ChildeHolder) convertView.getTag();

        }
        ArrayList<CategoryChildBean> categoryChildBeen = this.categoryChildBeen.get(groupPosition);
        CategoryChildBean bean = categoryChildBeen.get(childPosition);
        holder.textChildname.setText(bean.getName());
        ImageLoader.downloadImg(context, holder.imageChild, bean.getImageUrl());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {

        ImageView imageGroup;

        TextView textGroupName;


    }

    class ChildeHolder {
        ImageView imageChild;
        TextView textChildname;
    }

    public void initContactgroup(ArrayList<CategoryGroupBean> goodsList) {
        this.categoryGroupBeen.clear();

        this.categoryGroupBeen.addAll(goodsList);
        notifyDataSetChanged();

    }

    public void initContactchild(ArrayList<ArrayList<CategoryChildBean>> goodsList) {

        this.categoryChildBeen.addAll(goodsList);
        notifyDataSetChanged();

    }
}
