package com.scrat.gogo.module.race.betting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;

import com.bumptech.glide.request.RequestOptions;
import com.scrat.gogo.R;
import com.scrat.gogo.data.model.BettingTpItem;
import com.scrat.gogo.data.model.Race;
import com.scrat.gogo.data.model.RaceInfo;
import com.scrat.gogo.databinding.ActivityBettingBinding;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.common.BaseFragmentPagerAdapter;
import com.scrat.gogo.framework.glide.GlideApp;
import com.scrat.gogo.framework.glide.GlideRequests;
import com.scrat.gogo.framework.glide.GlideRoundTransform;
import com.scrat.gogo.module.race.betting.list.BettingListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scrat on 2017/11/15.
 */

public class BettingActivity extends BaseActivity implements BettingContract.View {
    private ActivityBettingBinding binding;
    private BettingContract.Presenter presenter;
    private static final String RACE = "race";
    private GlideRequests glideRequests;
    private RequestOptions options;

    public static void show(Context context, Race race) {
        Intent i = new Intent(context, BettingActivity.class);
        i.putExtra(RACE, race);
        context.startActivity(i);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "BettingActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_betting);
        glideRequests = GlideApp.with(this);
        options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.place_holder_circle_80dp)
                .transform(new GlideRoundTransform(10));
        Race race = (Race) getIntent().getSerializableExtra(RACE);
        showRace(race);

        new BettingPresenter(this, race.getRaceId());
        presenter.loadData();
    }

    @Override
    public void setPresenter(BettingContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingBetting() {

    }

    @Override
    public void showLoadBettingError(String e) {
        showMessage(e);
    }

    @Override
    public void showBetting(RaceInfo info) {
        binding.subject.setText(info.getRace().getRaceName());
        showRace(info.getRace());
        List<BettingTpItem> items = info.getTpItems();
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (BettingTpItem item : items) {
            fragments.add(BettingListFragment.newInstance(info.getRace().getRaceId(), item.getTp()));
            titles.add(item.getName());
        }
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(
                getSupportFragmentManager(), fragments, titles);
        binding.pager.setAdapter(adapter);
        binding.pager.setCurrentItem(0);
        binding.tabs.setupWithViewPager(binding.pager);
    }

    private String formatScore(String scoreA, String scoreB) {
        return String.format("%s : %s", scoreA, scoreB);
    }

    private void showRace(Race race) {
        glideRequests.load(race.getTeamA().getLogo())
                .apply(options)
                .into(binding.teamALogo);
        binding.teamAName.setText(race.getTeamA().getTeamName());
        glideRequests.load(race.getTeamB().getLogo())
                .apply(options)
                .into(binding.teamBLogo);
        binding.teamBName.setText(race.getTeamB().getTeamName());
        binding.score.setText(formatScore(race.getScoreA(), race.getScoreB()));
        binding.date.setText(DateFormat.format("M月d日 H:mm", race.getRaceTs()));
    }
}
