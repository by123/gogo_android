package com.scrat.gogo.module.login;

import com.scrat.gogo.data.DataRepository;
import com.scrat.gogo.data.api.Res;
import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.local.Preferences;
import com.scrat.gogo.data.model.TokenInfo;
import com.scrat.gogo.framework.util.L;

/**
 * Created by scrat on 2017/11/15.
 */

public class RefreshTokenHelper {
    public static void refreshToken(boolean refresh) {
        if (!Preferences.getInstance().isLogin()) {
            return;
        }

        if (!refresh) {
            long ts = Preferences.getInstance().getLastUpdateTokenTs();
            if (ts - System.currentTimeMillis() < 3600000 * 24) {
                return;
            }
        }

        String refreshToken = Preferences.getInstance().getRefreshToken();
        DataRepository.getInstance().getApi()
                .refreshToken(refreshToken, new DefaultLoadObjCallback<TokenInfo, Res.TokenRes>(Res.TokenRes.class) {
                    @Override
                    protected void onSuccess(TokenInfo tokenInfo) {
                        Preferences.getInstance().setLastUpdateRefreshTokenTs(System.currentTimeMillis());
                        Preferences.getInstance().setAccessToken(tokenInfo.getAccessToken());
                        Preferences.getInstance().setRefreshToken(tokenInfo.getRefreshToken());
                    }

                    @Override
                    public void onError(Exception e) {
                        L.e(e);
                    }
                });
    }
}
