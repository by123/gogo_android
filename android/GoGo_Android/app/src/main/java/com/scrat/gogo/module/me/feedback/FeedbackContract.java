package com.scrat.gogo.module.me.feedback;

import android.net.Uri;

import com.scrat.gogo.framework.common.BaseContract;

import java.util.List;

/**
 * Created by scrat on 2017/11/21.
 */

public interface FeedbackContract {
    interface Presenter {
        void feedback(String content, String verName, int verCode);

        void uploadImg(String imgPath);
    }

    interface View extends BaseContract.BaseView<Presenter> {
        void showFeedback();

        void showFeedbackFail(String e);

        void showFeedbackSuccess();

        void showImgs(List<String> imgs);

        void selectImgFail(String e);
    }
}
