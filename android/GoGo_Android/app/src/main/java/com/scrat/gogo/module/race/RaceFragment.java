package com.scrat.gogo.module.race;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scrat.gogo.databinding.FragmentRaceBinding;
import com.scrat.gogo.framework.common.BaseFragment;
import com.scrat.gogo.framework.common.BaseFragmentPagerAdapter;
import com.scrat.gogo.module.race.list.RaceListFragment;
import com.scrat.gogo.module.race.team.list.TeamListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scrat on 2017/10/24.
 */
@Deprecated
public class RaceFragment extends BaseFragment {
    private FragmentRaceBinding binding;
    private List<Fragment> fragments;
    private BaseFragmentPagerAdapter adapter;

    public static RaceFragment newInstance() {
        Bundle args = new Bundle();

        RaceFragment fragment = new RaceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String getFragmentName() {
        return "RaceFragment";
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentRaceBinding.inflate(inflater, container, false);
        fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        fragments.add(RaceListFragment.newInstance());
        titles.add("赛事安排");
        fragments.add(TeamListFragment.newInstance());
        titles.add("战队介绍");

        adapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), fragments, titles);
        binding.pager.setAdapter(adapter);
        binding.pager.setCurrentItem(0);
        binding.tabs.setupWithViewPager(binding.pager);

        return binding.getRoot();
    }
}
