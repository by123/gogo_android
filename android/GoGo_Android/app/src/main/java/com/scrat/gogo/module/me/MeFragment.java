package com.scrat.gogo.module.me;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestOptions;
import com.scrat.gogo.R;
import com.scrat.gogo.data.local.Preferences;
import com.scrat.gogo.data.model.UserInfo;
import com.scrat.gogo.databinding.FragmentMeBinding;
import com.scrat.gogo.framework.common.BaseFragment;
import com.scrat.gogo.framework.glide.GlideApp;
import com.scrat.gogo.framework.glide.GlideCircleTransform;
import com.scrat.gogo.framework.glide.GlideRequests;
import com.scrat.gogo.framework.view.IosDialog;
import com.scrat.gogo.framework.view.LoginDialog;
import com.scrat.gogo.module.coin.CoinPlanActivity;
import com.scrat.gogo.module.me.betting.BettingHistoryActivity;
import com.scrat.gogo.module.me.exchange.ExchangeHistoryActivity;
import com.scrat.gogo.module.me.profile.ProfileActivity;
import com.scrat.gogo.module.setting.SettingActivity;

/**
 * Created by scrat on 2017/10/24.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener, MeContract.View {
    private FragmentMeBinding binding;
    private MeContract.Presenter presenter;
    private RequestOptions options;
    private GlideRequests glideRequests;
    private IosDialog loginDialog;

    public static MeFragment newInstance() {
        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String getFragmentName() {
        return "MeFragment";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMeBinding.inflate(inflater, container, false);

        binding.userInfo.setOnClickListener(this);
        binding.recharge.setOnClickListener(this);
        binding.exchangeHistory.setOnClickListener(this);
        binding.betting.setOnClickListener(this);
        binding.setting.setOnClickListener(this);

        glideRequests = GlideApp.with(this);
        options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.place_holder_circle_80dp)
                .transform(new GlideCircleTransform());

        new MePresenter(this);
        loginDialog = LoginDialog.build(getContext());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadUserInfo();
    }

    @Override
    public void onDestroyView() {
        if (loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
        super.onDestroyView();
    }

    @Override
    public void setPresenter(MeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNotLogin() {
        binding.name.setText("登录");
        binding.coinTip.setVisibility(View.GONE);
        binding.coin.setVisibility(View.GONE);
        binding.img.setImageDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.place_holder_circle_80dp));
    }

    @Override
    public void showUserInfo(UserInfo info) {
        binding.name.setText(info.getUsername());
        binding.coinTip.setVisibility(View.VISIBLE);
        binding.coin.setVisibility(View.VISIBLE);
        binding.coin.setText(String.valueOf(info.getCoin()));
        glideRequests.load(info.getAvatar()).apply(options).into(binding.img);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.setting) {
            SettingActivity.show(getContext());
            return;
        }

        if (!Preferences.getInstance().isLogin()) {
            loginDialog.show(view);
            return;
        }

        if (view == binding.userInfo) {
            ProfileActivity.show(getContext());
            return;
        }

        if (view == binding.recharge) {
            CoinPlanActivity.show(getActivity());
            return;
        }

        if (view == binding.exchangeHistory) {
            ExchangeHistoryActivity.show(getContext());
            return;
        }

        if (view == binding.betting) {
            BettingHistoryActivity.show(getContext());
        }

    }
}
