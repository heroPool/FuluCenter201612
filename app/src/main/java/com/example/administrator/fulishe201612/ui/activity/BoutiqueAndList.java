package com.example.administrator.fulishe201612.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.fragments.Xinpin;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoutiqueAndList extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.imageback)
    ImageView imageback;
    @BindView(R.id.textTitle)
    TextView textTitle;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_and_list);
        ButterKnife.bind(this);
        title= getIntent().getStringExtra("title");
        textTitle.setText(title);

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
}
