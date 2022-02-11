package com.scrat.gogo;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.scrat.gogo.data.local.Preferences;
import com.scrat.gogo.data.model.UpdateInfo;
import com.scrat.gogo.databinding.ActivityMainBinding;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.view.IosDialog;
import com.scrat.gogo.module.news.list.HomeFragment;
import com.scrat.gogo.module.login.RefreshTokenHelper;
import com.scrat.gogo.module.me.MeFragment;
import com.scrat.gogo.module.race.list.RaceListFragment;
import com.scrat.gogo.module.shop.ShopFragment;
import com.scrat.gogo.module.update.UpdateHelper;

public class MainActivity extends BaseActivity {

    private HomeFragment homeFragment;
    private RaceListFragment raceFragment;
    private ShopFragment shopFragment;
    private MeFragment meFragment;

    private Fragment currFragment;
    private ActivityMainBinding binding;
    private IosDialog updateDialog;

    public static void redirect(Activity activity) {
        Intent i = new Intent(activity, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(i);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "MainActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initFragment();
        navigateToHome(null);
        RefreshTokenHelper.refreshToken(true);
        UpdateHelper.checkUpdate(
                this.getApplicationContext(), false, new UpdateHelper.UpdateListener() {
                    @Override
                    public void update(boolean force, final UpdateInfo info) {
                        updateDialog.setTitle(info.getTitle())
                                .setContent(info.getContent())
                                .setPositive("立即更新", view -> {
                                    Preferences.getInstance().setLastCheckVerCode(info.getVer());
                                    UpdateHelper.downloadApk(MainActivity.this, info.getUrl());
                                });
                        if (!force) {
                            updateDialog.setNegative(
                                    "稍后再说",
                                    view -> Preferences.getInstance().setLastCheckVerCode(info.getVer()));
                        }
                        updateDialog.show(binding.content);
                    }

                    @Override
                    public void showNoNeedToUpdate() {
                        // ignore
                    }
                });
        updateDialog = new IosDialog(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshTokenHelper.refreshToken(false);
    }

    @Override
    protected void onDestroy() {
        if (updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (currFragment != homeFragment) {
            navigateToHome(binding.home);
            return;
        }
        super.onBackPressed();
    }

    private void initFragment() {
        homeFragment = HomeFragment.newInstance();
        raceFragment = RaceListFragment.newInstance();
        shopFragment = ShopFragment.newInstance();
        meFragment = MeFragment.newInstance();
    }

    public void navigateToHome(View view) {
        switchFragment(homeFragment);

        binding.home.setTextColor(ContextCompat.getColor(this, R.color.c01_blue));
        binding.home.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_1_24, 0, 0);
        binding.race.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.race.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_game_0_24, 0, 0);
        binding.shop.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.shop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_store_0_24, 0, 0);
        binding.me.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.me.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_me_0_24, 0, 0);
    }

    public void navigateToRace(View view) {
        switchFragment(raceFragment);

        binding.home.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.home.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_0_24, 0, 0);
        binding.race.setTextColor(ContextCompat.getColor(this, R.color.c01_blue));
        binding.race.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_game_1_24, 0, 0);
        binding.shop.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.shop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_store_0_24, 0, 0);
        binding.me.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.me.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_me_0_24, 0, 0);
    }

    public void navigateToShop(View view) {
        switchFragment(shopFragment);

        binding.home.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.home.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_0_24, 0, 0);
        binding.race.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.race.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_game_0_24, 0, 0);
        binding.shop.setTextColor(ContextCompat.getColor(this, R.color.c01_blue));
        binding.shop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_store_1_24, 0, 0);
        binding.me.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.me.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_me_0_24, 0, 0);
    }

    public void navigateToMe(View view) {
        switchFragment(meFragment);

        binding.home.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.home.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_0_24, 0, 0);
        binding.race.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.race.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_game_0_24, 0, 0);
        binding.shop.setTextColor(ContextCompat.getColor(this, R.color.c10_icon));
        binding.shop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_store_0_24, 0, 0);
        binding.me.setTextColor(ContextCompat.getColor(this, R.color.c01_blue));
        binding.me.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_me_1_24, 0, 0);
    }

    private void switchFragment(Fragment target) {
        currFragment = switchFragment(R.id.content, currFragment, target);
    }

    private Fragment switchFragment(int containerViewId, Fragment from, Fragment to) {
        if (from != null && from == to) {
            return to;
        }
        Animation fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeOut.setDuration(100L);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(100L);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (from != null) {
            if (from.getView() != null) {
                from.getView().startAnimation(fadeOut);
            }
            transaction.hide(from);
        }
        if (!to.isAdded()) {
            transaction.add(containerViewId, to);
        } else {
            transaction.show(to);
        }
        if (to.getView() != null) {
            to.getView().startAnimation(fadeIn);
        }
        transaction.commit();
        return to;
    }
}
