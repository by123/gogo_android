package com.scrat.gogo.module.me.nickname;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.scrat.gogo.R;
import com.scrat.gogo.databinding.ActivityNicknameBinding;
import com.scrat.gogo.framework.common.BaseActivity;

/**
 * Created by scrat on 2017/11/20.
 */

public class NicknameActivity extends BaseActivity implements NicknameContract.View {

    private ActivityNicknameBinding binding;
    private NicknameContract.Presenter presenter;

    public static void show(Activity activity, int requestCode) {
        Intent i = new Intent(activity, NicknameActivity.class);
        activity.startActivityForResult(i, requestCode);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "NicknameActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nickname);
        binding.topBar.subject.setText("更改昵称");
        binding.content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.clear.setVisibility(View.VISIBLE);
                } else {
                    binding.clear.setVisibility(View.GONE);
                }
            }
        });

        binding.content.setOnEditorActionListener((textView, i, keyEvent) -> {
            switch (i) {
                case EditorInfo.IME_ACTION_DONE:
                    hideSoftInput();
                    presenter.updateNickname(binding.content.getText().toString());
                    break;
            }
            return true;
        });

        new NicknamePresenter(this);
        presenter.loadNickname();
    }

    public void clear(View view) {
        binding.content.setText("");
    }

    @Override
    public void setPresenter(NicknameContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNickname(String nickname) {
        binding.content.setText(nickname);
    }

    @Override
    public void showNicknameUpdating() {

    }

    @Override
    public void showNicknameUpdateSuccess() {
        toast("更新成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showNicknameUpdateFail(String e) {
        showMessage(e);
    }
}
