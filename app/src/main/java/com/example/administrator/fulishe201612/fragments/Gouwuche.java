package com.example.administrator.fulishe201612.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.adapter.CartRecyclerviewAdapter;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.model.bean.CartBean;
import com.example.administrator.fulishe201612.model.bean.GoodsDetailsBean;
import com.example.administrator.fulishe201612.model.bean.User;
import com.example.administrator.fulishe201612.model.net.CartModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.utils.L;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;
import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Gouwuche extends Fragment {


    @BindView(R.id.text_hint)
    TextView textHint;
    @BindView(R.id.recyclerView_cart)
    RecyclerView recyclerViewCart;
    @BindView(R.id.textJiesuan)
    TextView textJiesuan;
    @BindView(R.id.texteHeji)
    TextView texteHeji;
    @BindView(R.id.textJiesheng)
    TextView textJiesheng;
    @BindView(R.id.checkBoxQuanxuan)
    CheckBox checkBoxQuanxuan;
    @BindView(R.id.layout_cart_bottom_menu)
    RelativeLayout layoutCartBottomMenu;
    Unbinder unbinder;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    CartModel cartModel;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.textview_hint)
    TextView textviewHint;
    @BindView(R.id.toobar_cart)
    Toolbar toobarCart;

    public Gouwuche() {// Required empty public constructor
    }

    private static String URL = "http://218.244.151.190/demo/charge";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gouwuche, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toobarCart);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cartModel = new CartModel();
        initView();
        initData();
        initPay();
        setListener();
    }

    private void initPay() {
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
    }


    private void setListener() {
        setPullDownListener();
        cartRecyclerAdapter.setListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (int) buttonView.getTag();
                cartList.get(position).setChecked(isChecked);
                setPriceText();
            }
        });
        cartRecyclerAdapter.setCartUpdatelistener(updateListener);
    }

    private static final String TAG = "Gouwuche";
    View.OnClickListener updateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();

            int count = 0;
            if (view.getTag(R.id.action_add_cart) != null) {
                count = (int) view.getTag(R.id.action_add_cart);
            } else if (view.getTag(R.id.action_del_cart) != null) {
                count = (int) view.getTag(R.id.action_del_cart);
            }
            L.e(TAG, "updateListener,position=" + position + ",count=" + count);
//            updateCart(position, count);
        }
    };


    private void setPullDownListener() {
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                initData();

            }
        });
    }

    User user;

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            showCartList();
        }
    }

    private void showCartList() {
        cartModel.loadData(getContext(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {

                setCartListLayout(true);
                if (result != null) {
                    cartList.clear();
                    if (result.length > 0) {
                        ArrayList<CartBean> list = OkHttpUtils.array2List(result);
                        cartList.addAll(list);
                        cartRecyclerAdapter.notifyDataSetChanged();
                    }
                }
                setRefresh(false);
            }

            @Override
            public void onError(String error) {
                setRefresh(false);

            }
        });
    }

    private void setRefresh(boolean refresh) {
        swiperefreshlayout.setRefreshing(refresh);
        textviewHint.setVisibility(refresh ? View.VISIBLE : View.GONE);
    }

    CartRecyclerviewAdapter cartRecyclerAdapter;
    ArrayList<CartBean> cartList = new ArrayList<>();

    private void initView() {
        swiperefreshlayout.setColorSchemeColors(getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        recyclerViewCart.setHasFixedSize(true);
        cartRecyclerAdapter = new CartRecyclerviewAdapter(getContext(), cartList);
        recyclerViewCart.setAdapter(cartRecyclerAdapter);
        recyclerViewCart.addItemDecoration(new SpaceItemDecoration(12));
        setCartListLayout(false);
    }

    private void setCartListLayout(boolean isShow) {
        textHint.setVisibility(isShow ? View.GONE : View.VISIBLE);
        layoutCartBottomMenu.setVisibility(isShow ? View.VISIBLE : View.GONE);
        setPriceText();
    }

    private void setPriceText() {
        int sumPrice = 0;
        int rankPrice = 0;
        for (CartBean cart : cartList) {
            if (cart.isChecked()) {
                GoodsDetailsBean goods = cart.getGoods();
                if (goods != null) {
                    sumPrice += getPrice(goods.getCurrencyPrice()) * cart.getCount();
                    rankPrice += getPrice(goods.getRankPrice()) * cart.getCount();
                }
            }
        }
    }

    private int getPrice(String price) {
        String priceStr = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(priceStr);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.textJiesuan)
    public void onViewClicked() {

        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        // 计算总金额（以分为单位）
        int amount = 100 * 100;
//        JSONArray billList = new JSONArray();
//        for (Good good : mList) {
//            amount += good.getPrice() * good.getCount() * 100;
//            billList.put(good.getName() + " x " + good.getCount());
//        }
        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", amount);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getActivity(), bill.toString(), URL, new PaymentHandler() {
            @Override
            public void handlePaymentResult(Intent data) {
                if (data != null) {
                    /**
                     * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
                     * error_msg：支付结果信息
                     */
                    int code = data.getExtras().getInt("code");
                    String result = data.getExtras().getString("result");
                }
            }
        });

    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildLayoutPosition(view);
            if (parent.getChildPosition(view) != 0)
                outRect.top = space;

            // 设置左右间距
            outRect.set(space / 2, 0, space / 2, 0);

            // 从第二行开始 top = mSpace
//            if (pos >= I.COLUM_NUM) {
            outRect.top = space;
//            } else {
//                outRect.top = 0;
//            }
        }
    }


}