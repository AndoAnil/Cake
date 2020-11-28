package com.example.cake.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.cake.Login.MakerLogin;
import com.example.cake.R;
import com.example.cake.Register.MakerRegister;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

public class BottomSheetMaker extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetMaker";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bottom_sheet_maker,container,false);
        viewPager=(ViewPager) view.findViewById(R.id.maker_viewPager);
        tabLayout=(TabLayout) view.findViewById(R.id.makerTab);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentpagerAdapter adapter=new FragmentpagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MakerLogin(),"Login");
        adapter.addFragment(new MakerRegister(),"Register");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
