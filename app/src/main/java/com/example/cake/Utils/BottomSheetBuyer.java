package com.example.cake.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.utils.Utils;
import com.example.cake.Login.BuyerLogin;
import com.example.cake.R;
import com.example.cake.Register.BuyerRegister;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

public class BottomSheetBuyer  extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetBuyer";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bottom_sheet_buyer,container,false);
        viewPager=(ViewPager) view.findViewById(R.id.buyer_viewPager);
        tabLayout=(TabLayout) view.findViewById(R.id.buyertab);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentpagerAdapter adapter=new FragmentpagerAdapter(getChildFragmentManager());
        adapter.addFragment(new BuyerLogin(),"Login");
        adapter.addFragment(new BuyerRegister(),"Register");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
