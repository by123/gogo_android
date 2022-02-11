package com.scrat.gogo.module.shop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scrat.gogo.databinding.FragmentShopBinding;
import com.scrat.gogo.framework.common.BaseFragment;
import com.scrat.gogo.framework.common.BaseFragmentPagerAdapter;
import com.scrat.gogo.module.shop.list.ShopListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scrat on 2017/10/24.
 */

public class ShopFragment extends BaseFragment {
    private FragmentShopBinding binding;
    private List<Fragment> fragments;
    private BaseFragmentPagerAdapter adapter;

    public static ShopFragment newInstance() {
        Bundle args = new Bundle();

        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String getFragmentName() {
        return "ShopFragment";
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        fragments = new ArrayList<>();
        fragments.add(ShopListFragment.newEquipmentInstance());
        fragments.add(ShopListFragment.newVirtualInstance());
        fragments.add(ShopListFragment.newGameAroundInstance());
        fragments.add(ShopListFragment.newLuckMoneyInstance());
        fragments.add(ShopListFragment.newEquipmentInstance());
        List<String> titles = new ArrayList<String>() {{
            add("热门");
            add("虚拟物品");
            add("游戏周边");
            add("现金红包");
            add("电竞装备");
        }};
        adapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), fragments, titles);
        binding.pager.setAdapter(adapter);
        binding.pager.setCurrentItem(0);
        binding.tabs.setupWithViewPager(binding.pager);

        return binding.getRoot();
    }
}
