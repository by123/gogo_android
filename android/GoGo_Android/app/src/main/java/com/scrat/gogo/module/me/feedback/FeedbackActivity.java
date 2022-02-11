package com.scrat.gogo.module.me.feedback;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.scrat.gogo.R;
import com.scrat.gogo.databinding.ActivityFeedbackBinding;
import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.glide.GlideApp;
import com.scrat.gogo.framework.glide.GlideRequests;
import com.scrat.gogo.framework.util.Utils;
import com.scrat.gogo.framework.view.SingleImgSelectorActivity;

import java.util.List;

/**
 * Created by scrat on 2017/11/21.
 */

public class FeedbackActivity extends BaseActivity implements FeedbackContract.View {
    private ActivityFeedbackBinding binding;
    private FeedbackContract.Presenter presenter;
    private static final int SELECT_IMG = 1;
    private GlideRequests requests;

    public static void show(Context context) {
        Intent i = new Intent(context, FeedbackActivity.class);
        context.startActivity(i);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "FeedbackActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        binding.topBar.subject.setText("用户反馈");
        requests = GlideApp.with(this);
        new FeedbackPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case SELECT_IMG:
                String path = SingleImgSelectorActivity.parsePath(data);
                if (!TextUtils.isEmpty(path)) {
                    presenter.uploadImg(path);
                }
                break;
        }
    }

    @Override
    public void setPresenter(FeedbackContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showFeedback() {
        binding.sr.startShimmerAnimation();
    }

    @Override
    public void showFeedbackFail(String e) {
        binding.sr.stopShimmerAnimation();
        showMessage(e);
    }

    @Override
    public void showFeedbackSuccess() {
        binding.sr.stopShimmerAnimation();
        toast("提交成功");
        finish();
    }

    @Override
    public void showImgs(List<String> imgs) {
        switch (imgs.size()) {
            case 0:
                binding.img1.setImageResource(R.drawable.ic_add_c09_24dp);
                binding.img2.setVisibility(View.INVISIBLE);
                binding.img3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                binding.img1.setImageResource(R.drawable.ic_add_c09_24dp);
                binding.img2.setVisibility(View.VISIBLE);
                binding.img3.setVisibility(View.INVISIBLE);
                requests.load(imgs.get(0)).centerCrop().into(binding.img2);
                break;
            case 2:
                binding.img1.setImageResource(R.drawable.ic_add_c09_24dp);
                binding.img2.setVisibility(View.VISIBLE);
                binding.img3.setVisibility(View.VISIBLE);
                requests.load(imgs.get(0)).centerCrop().into(binding.img2);
                requests.load(imgs.get(1)).centerCrop().into(binding.img3);
                break;
            case 3:
                binding.img1.setClickable(false);
                binding.img1.setFocusable(false);
                binding.img2.setVisibility(View.VISIBLE);
                binding.img3.setVisibility(View.VISIBLE);
                requests.load(imgs.get(2)).centerCrop().into(binding.img1);
                requests.load(imgs.get(0)).centerCrop().into(binding.img2);
                requests.load(imgs.get(1)).centerCrop().into(binding.img3);
                break;
        }
    }

    @Override
    public void selectImgFail(String e) {
        showMessage(e);
    }

    public void feedback(View view) {
        presenter.feedback(
                binding.content.getText().toString(),
                Utils.getVersionName(this),
                Utils.getVersionCode(this));
    }

    public void selectImg(View view) {
        SingleImgSelectorActivity.show(this, SELECT_IMG);
    }
}
