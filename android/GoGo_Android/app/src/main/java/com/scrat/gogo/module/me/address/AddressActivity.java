package com.scrat.gogo.module.me.address;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.scrat.gogo.R;
import com.scrat.gogo.data.model.Address;
import com.scrat.gogo.databinding.ActivityAddressBinding;
import com.scrat.gogo.framework.common.BaseActivity;

/**
 * Created by scrat on 2017/11/17.
 */

public class AddressActivity extends BaseActivity implements AddressContract.View {
    private ActivityAddressBinding binding;
    private AddressContract.Presenter presenter;

    public static void show(Context context) {
        Intent i = new Intent(context, AddressActivity.class);
        context.startActivity(i);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "AddressActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address);
        binding.topBar.subject.setText("我的地址");
        new AddressPresenter(this);
        presenter.loadAddress();
    }

    @Override
    public void setPresenter(AddressContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingAddress() {

    }

    @Override
    public void showLoadingAddressError(String e) {
        showMessage(e);
    }

    @Override
    public void showAddress(Address address) {
        binding.name.setText(address.getReceiver());
        binding.tel.setText(address.getTel());
        binding.detail.setText(address.getDetail());
    }

    @Override
    public void showUpdatingAddress() {

    }

    @Override
    public void showUpdateAddressError(String e) {
        showMessage(e);
    }

    @Override
    public void showUpdateAddressSuccess() {
        toast("更新成功");
        finish();
    }

    public void update(View view) {
        presenter.updateAddress(
                binding.name.getText().toString(),
                binding.tel.getText().toString(),
                "",
                binding.detail.getText().toString()
        );
    }
}
