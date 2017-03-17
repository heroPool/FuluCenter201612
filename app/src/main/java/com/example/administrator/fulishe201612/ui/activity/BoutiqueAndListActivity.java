package com.example.administrator.fulishe201612.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.fragments.Xinpin;
import com.example.administrator.fulishe201612.model.net.FindGoodsDetails;
import com.example.administrator.fulishe201612.model.net.NewGoodsModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BoutiqueAndListActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.imageback)
    ImageView imageback;
    @BindView(R.id.textTitle)
    TextView textTitle;
    String title;

    Unbinder bind;
    int listid;
    @BindView(R.id.layout_Sort)
    LinearLayout layoutSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_and_list);
        bind = ButterKnife.bind(this);
        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        listid = intent.getIntExtra(I.NewAndBoutiqueGoods.CAT_ID, 0);
        String boolea = getIntent().getStringExtra("boolea");
        Log.i("boolea", boolea);
        if (boolea!=null) {
            layoutSort.setVisibility(View.VISIBLE);

        }

        textTitle.setText(this.title);


        getSupportFragmentManager().beginTransaction().add(R.id.fragment_cantai,
                new Xinpin()).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageback:
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        bind.unbind();
    }
}
