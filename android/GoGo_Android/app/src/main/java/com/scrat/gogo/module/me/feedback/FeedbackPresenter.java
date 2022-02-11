package com.scrat.gogo.module.me.feedback;

import android.support.annotation.NonNull;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Api;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.model.Uptoken;
import com.scrat.gogo.framework.qiniu.DefaultUploadListener;
import com.scrat.gogo.framework.qiniu.QiniuUploadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scrat on 2017/11/21.
 */

public class FeedbackPresenter implements FeedbackContract.Presenter {
    private FeedbackContract.View view;
    private volatile List<String> imgs;

    public FeedbackPresenter(FeedbackContract.View view) {
        this.view = view;
        imgs = new ArrayList<>();
        view.setPresenter(this);
    }

    @Override
    public void feedback(String content, String verName, int verCode) {
        view.showFeedback();
        DataRepository.getInstance().getApi().feedback(
                "",
                content,
                verCode,
                verName,
                imgs,
                new DefaultLoadObjCallback<String, Res.DefaultStrRes>(Res.DefaultStrRes.class) {
                    @Override
                    protected void onSuccess(String s) {
                        view.showFeedbackSuccess();
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showFeedbackFail(e.getMessage());
                    }
                });
    }

    @Override
    public void uploadImg(final String imgPath) {
        Api api = DataRepository.getInstance().getApi();
        api.getQiniuUptoken(new DefaultLoadObjCallback<Uptoken, Res.UptokenRes>(Res.UptokenRes.class) {
            @Override
            protected void onSuccess(Uptoken uptoken) {
                QiniuUploadManager.getInstance().uploadImg(
                        uptoken.getDomain(), uptoken.getToken(), imgPath, new DefaultUploadListener() {
                            @Override
                            public void onSuccess(String path, String url) {
                                imgs.add(url);
                                view.showImgs(imgs);
                            }

                            @Override
                            public void onFail(String path, String msg) {
                                view.selectImgFail(msg);
                            }
                        });
            }

            @Override
            public void onError(Exception e) {
                view.selectImgFail(e.getMessage());
            }
        });
    }
}
