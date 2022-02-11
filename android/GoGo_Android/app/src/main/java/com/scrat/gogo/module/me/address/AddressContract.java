package com.scrat.gogo.module.me.address;

import com.scrat.gogo.data.model.Address;
import com.scrat.gogo.framework.common.BaseContract;

/**
 * Created by scrat on 2017/11/17.
 */

public interface AddressContract {
    interface Presenter {
        void loadAddress();

        void updateAddress(String receiver, String tel, String location, String detail);
    }

    interface View extends BaseContract.BaseView<Presenter> {
        void showLoadingAddress();

        void showLoadingAddressError(String e);

        void showAddress(Address address);

        void showUpdatingAddress();

        void showUpdateAddressError(String e);

        void showUpdateAddressSuccess();
    }
}
