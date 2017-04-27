package com.example.administrator.fulishe201612.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.CategoryChildBean;
import com.example.administrator.fulishe201612.model.net.CateListModel;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;
import com.example.administrator.fulishe201612.ui.activity.BoutiqueAndListActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CateDetialsFragment extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<CategoryChildBean> categoryChildBeen;
    CateListModel cateListModel;
    RecyclerviewAdapter recyclerviewAdapter;

    public CateDetialsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_cate_detials, container, false);
        EventBus.getDefault().register(this);

        cateListModel = new CateListModel();
        categoryChildBeen = new ArrayList<>();
        initData();
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_catedeticals);

        recyclerviewAdapter = new RecyclerviewAdapter(getContext(), categoryChildBeen);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));
        recyclerView.setAdapter(recyclerviewAdapter);

        return inflate;
    }

    private void initData() {
        onShowChildBean(344);
    }

    @Subscribe
    public void onShowChildBean(Integer parentId) {
        Log.e("Event执行", "parentId==" + parentId);
        final OkHttpUtils<CategoryChildBean[]> okHttpUtils = new OkHttpUtils<>(getActivity());
        okHttpUtils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, parentId + "")
                .targetClass(CategoryChildBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onSuccess(CategoryChildBean[] result) {
                        ArrayList<CategoryChildBean> list = okHttpUtils.array2List(result);
                        Log.e("请求小类成功", "list==" + list.toString());
                        recyclerviewAdapter.initContactchild(list);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        ArrayList<CategoryChildBean> categoryChildBeen;

        public RecyclerviewAdapter(Context context, ArrayList<CategoryChildBean> categoryChildBeen) {
            this.context = context;
            this.categoryChildBeen = categoryChildBeen;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.categoty_adapter_child, null);
            return new ViewHolderitem(inflate);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            ViewHolderitem hd = (ViewHolderitem) holder;
            CategoryChildBean bean1 = categoryChildBeen.get(position);
            hd.tvChild.setText(bean1.getName());
            ImageLoader.downloadImg(context, hd.ivChild, bean1.getImageUrl());
//            Glide.with(getActivity()).load(bean1.getImageUrl()).into(hd.ivChild);
            hd.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryChildBean bean = categoryChildBeen.get(position);

                    String groupItem = categoryChildBeen.get(position).getName();

                    FuLiCenterApplication.getInstance().setIndex(2);
                    startActivity(new Intent(getActivity(), BoutiqueAndListActivity.class)
                            .putExtra("title", categoryChildBeen.get(position).getName())
                            .putExtra(I.NewAndBoutiqueGoods.CAT_ID, categoryChildBeen.get(position).getId())
                            .putExtra("boolea", "1")
                            .putExtra("childList", categoryChildBeen)
                            .putExtra("groupItem", groupItem)
                    );
                }
            });
        }

        @Override
        public int getItemCount() {
            return categoryChildBeen.size();
        }


        class ViewHolderitem extends RecyclerView.ViewHolder {

            @BindView(R.id.iv_child)
            ImageView ivChild;
            @BindView(R.id.tv_child)
            TextView tvChild;

            ViewHolderitem(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public void initContactchild(ArrayList<CategoryChildBean> goodsList) {
            this.categoryChildBeen.clear();
            this.categoryChildBeen.addAll(goodsList);
            notifyDataSetChanged();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
