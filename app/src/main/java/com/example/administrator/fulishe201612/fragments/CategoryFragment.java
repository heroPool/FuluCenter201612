package com.example.administrator.fulishe201612.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.fulishe201612.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_category, container, false);

        initView();
        return inflate;

    }

    private void initView() {
        FragmentManager fagmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fagmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_group, new CateListFragment());
        fragmentTransaction.replace(R.id.fragment_child, new CateDetialsFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
