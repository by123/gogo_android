package com.scrat.gogo.data.api;

import com.scrat.gogo.data.callback.DefaultLoadObjCallback;
import com.scrat.gogo.data.local.Preferences;
import com.scrat.gogo.data.model.Address;
import com.scrat.gogo.data.model.Betting;
import com.scrat.gogo.data.model.BettingInfo;
import com.scrat.gogo.data.model.CoinPlan;
import com.scrat.gogo.data.model.Comment;
import com.scrat.gogo.data.model.ExchangeHistory;
import com.scrat.gogo.data.model.Goods;
import com.scrat.gogo.data.model.GoodsDetail;
import com.scrat.gogo.data.model.News;
import com.scrat.gogo.data.model.NewsDetail;
import com.scrat.gogo.data.model.RaceGroupItem;
import com.scrat.gogo.data.model.RaceInfo;
import com.scrat.gogo.data.model.Team;
import com.scrat.gogo.data.model.TokenInfo;
import com.scrat.gogo.data.model.UpdateInfo;
import com.scrat.gogo.data.model.Uptoken;
import com.scrat.gogo.data.model.UserInfo;
import com.scrat.gogo.data.model.WxPayInfo;
import com.scrat.gogo.framework.util.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by scrat on 2017/11/2.
 */

public class Api {

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put(APIS.PT, APIS.DEFAULT_PT);
        header.put(APIS.APP_KEY, APIS.DEFAULT_APP_KEY);
        header.put(APIS.UID, Preferences.getInstance().getUid());
        header.put(APIS.ACCESS_TOKEN, Preferences.getInstance().getAccessToken());
        return header;
    }

    public void wxLogin(String code, DefaultLoadObjCallback<TokenInfo, Res.TokenRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.CODE, code);
            OkHttpHelper.getInstance()
                    .post(APIS.WX_LOGIN, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void getAlipayCoinPlanOrder(
            String coinPlanId, DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        String url = String.format(APIS.ALIPAY_COIN_PLAN_ORDER, coinPlanId);
        try {
            OkHttpHelper.getInstance().post(url, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void getWeixinCoinPlanOrder(
            String coinPlanId, DefaultLoadObjCallback<WxPayInfo, Res.WxPayInfoRes> cb) {
        String url = String.format(APIS.COIN_PLAN_WEIXIN_ORDER_URL, coinPlanId);
        try {
            OkHttpHelper.getInstance().post(url, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public Call getNews(
            String index, DefaultLoadObjCallback<Res.ListRes<News>, Res.NewsListRes> cb) {
        Map<String, String> param = new HashMap<>();
        param.put(APIS.INDEX, index);
        try {
            return OkHttpHelper.getInstance().get(APIS.NEWS_LIST_URL, getHeader(), param, cb);
        } catch (Exception e) {
            cb.onError(e);
            return null;
        }
    }

    public void getNewsDetail(
            String newsId, DefaultLoadObjCallback<NewsDetail, Res.NewsDetailRes> cb) {
        String url = String.format(APIS.NEWS_DETAIL_URL, newsId);
        try {
            OkHttpHelper.getInstance().get(url, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    private Call getComments(String tp,
                             String tpId,
                             String index,
                             DefaultLoadObjCallback<Res.ListRes<Res.CommentItem>, Res.CommentItemListRes> cb) {
        Map<String, String> param = new HashMap<>();
        param.put(APIS.INDEX, index);
        param.put(APIS.TP, tp);
        param.put(APIS.TARGET_ID, tpId);
        try {
            return OkHttpHelper.getInstance().get(APIS.COMMENT_LIST_URL, getHeader(), param, cb);
        } catch (Exception e) {
            cb.onError(e);
            return null;
        }
    }

    public Call getNewsComments(
            String newsId,
            String index,
            DefaultLoadObjCallback<Res.ListRes<Res.CommentItem>, Res.CommentItemListRes> cb) {
        return getComments(APIS.NEWS, newsId, index, cb);
    }

    public Call getGoodsList(String tp,
                             String index,
                             DefaultLoadObjCallback<Res.ListRes<Goods>, Res.GoodsListRes> cb) {

        Map<String, String> param = new HashMap<>();
        param.put(APIS.INDEX, index);
        param.put(APIS.TP, tp);
        try {
            return OkHttpHelper.getInstance().get(APIS.GOODS_LIST_URL, getHeader(), param, cb);
        } catch (Exception e) {
            cb.onError(e);
            return null;
        }
    }

    public void getQiniuUptoken(DefaultLoadObjCallback<Uptoken, Res.UptokenRes> cb) {
        try {
            OkHttpHelper.getInstance()
                    .get(APIS.QINUIU_UPTOKEN_URL, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void sendSms(String tel, DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.TEL, tel);
            OkHttpHelper.getInstance().post(APIS.SEND_SMS_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void smsLogin(
            String tel, String code, DefaultLoadObjCallback<TokenInfo, Res.TokenRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.TEL, tel);
            obj.put(APIS.CODE, code);
            OkHttpHelper.getInstance()
                    .post(APIS.SMS_LOGIN_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void logout(String refreshToken) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.REFRESH_TOKEN, refreshToken);
            OkHttpHelper.getInstance()
                    .post(APIS.LOGOUT_URL, getHeader(), obj.toString(), null);
        } catch (Exception ignore) {
        }
    }

    public void getUserInfo(DefaultLoadObjCallback<UserInfo, Res.UserInfoRes> cb) {
        try {
            OkHttpHelper.getInstance().get(APIS.USER_INFO_URL, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void getCoinPlan(DefaultLoadObjCallback<List<CoinPlan>, Res.CoinPlanListRes> cb) {
        try {
            OkHttpHelper.getInstance().get(APIS.COIN_PLAN_URL, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    private void addComment(
            String tp,
            String tpId,
            String content,
            DefaultLoadObjCallback<Comment, Res.CommentRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.TP, tp);
            obj.put(APIS.TARGET_ID, tpId);
            obj.put(APIS.CONTENT, content);
            OkHttpHelper.getInstance()
                    .post(APIS.ADD_COMMENT_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void addNewsComment(
            String newsId, String content, DefaultLoadObjCallback<Comment, Res.CommentRes> cb) {
        addComment(APIS.NEWS, newsId, content, cb);
    }

    public Call getRaceList(
            String index,
            DefaultLoadObjCallback<Res.ListRes<RaceGroupItem>, Res.RaceGroupItemRes> cb) {
        Map<String, String> param = new HashMap<>();
        param.put(APIS.INDEX, index);
        try {
            return OkHttpHelper.getInstance().get(APIS.RACE_LIST_URL, getHeader(), param, cb);
        } catch (Exception e) {
            cb.onError(e);
            return null;
        }
    }

    public void refreshToken(
            String refreshToken, DefaultLoadObjCallback<TokenInfo, Res.TokenRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.REFRESH_TOKEN, refreshToken);
            OkHttpHelper.getInstance()
                    .post(APIS.REFRESH_TOKEN_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public Call getTeams(
            String index, DefaultLoadObjCallback<Res.ListRes<Team>, Res.TeamListRes> cb) {
        Map<String, String> param = new HashMap<>();
        param.put(APIS.INDEX, index);
        try {
            return OkHttpHelper.getInstance().get(APIS.TEAM_LIST_URL, getHeader(), param, cb);
        } catch (Exception e) {
            cb.onError(e);
            return null;
        }
    }

    public void getBanner(DefaultLoadObjCallback<List<News>, Res.BannerRes> cb) {
        try {
            OkHttpHelper.getInstance().get(APIS.BANNER_URL, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public Call getBettingDetail(
            String raceId, DefaultLoadObjCallback<BettingInfo, Res.BettingInfoRes> cb) {
        String url = String.format(APIS.RACE_DETAIL_URL, raceId);
        try {
            return OkHttpHelper.getInstance().get(url, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
            return null;
        }
    }

    public void getRaceDetail(
            String raceId, DefaultLoadObjCallback<RaceInfo, Res.RaceInfoRes> cb) {
        String url = String.format(APIS.RACE_DETAIL_URL2, raceId);
        try {
            OkHttpHelper.getInstance().get(url, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void getBettingItem(
            String raceId,
            String tp,
            DefaultLoadObjCallback<List<Betting>, Res.BettingListRes> cb) {
        String url = String.format(APIS.BETTING_ITEM_URL, raceId, tp);
        try {
            OkHttpHelper.getInstance().get(url, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void getGoodsDetail(
            String goodsId, DefaultLoadObjCallback<GoodsDetail, Res.GoodsDetailRes> cb) {

        String url = String.format(APIS.GOODS_DETAIL_URL, goodsId);
        try {
            OkHttpHelper.getInstance().get(url, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public Call exchange(String goodsId, DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        String url = String.format(APIS.EXCHANGE_URL, goodsId);
        try {
            return OkHttpHelper.getInstance().post(url, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
            return null;
        }
    }

    public Call getExchangeHistory(
            String index,
            DefaultLoadObjCallback<Res.ListRes<ExchangeHistory>, Res.ExchangeHistoryListRes> cb) {
        Map<String, String> param = new HashMap<>();
        param.put(APIS.INDEX, index);
        try {
            return OkHttpHelper.getInstance()
                    .get(APIS.EXCHANGE_HISTORY_URL, getHeader(), param, cb);
        } catch (Exception e) {
            cb.onError(e);
            return null;
        }
    }

    public void updateAddress(String receiver,
                              String tel,
                              String location,
                              String detail,
                              DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.RECEIVER, receiver);
            obj.put(APIS.TEL, tel);
            obj.put(APIS.LOCATION, location);
            obj.put(APIS.ADDRESS_DETAIL, detail);
            OkHttpHelper.getInstance()
                    .post(APIS.UPDATE_ADDRESS_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void getAddress(DefaultLoadObjCallback<Address, Res.AddressRes> cb) {
        try {
            OkHttpHelper.getInstance().get(APIS.GET_ADDRESS_URL, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public Call betting(
            int coin, String bettingItemId, DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        arr.put(bettingItemId);
        try {
            obj.put(APIS.COIN, coin);
            obj.put(APIS.BETTING_ITEM_ID_LIST, arr);
            return OkHttpHelper.getInstance()
                    .post(APIS.BETTING_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
            return null;
        }
    }

    public Call getBettingHistory(
            String index,
            DefaultLoadObjCallback<Res.ListRes<BettingInfo>, Res.BettingInfoListRes> cb) {
        Map<String, String> param = new HashMap<>();
        param.put(APIS.INDEX, index);
        try {
            return OkHttpHelper.getInstance()
                    .get(APIS.BETTING_HISTORY_URL, getHeader(), param, cb);
        } catch (Exception e) {
            cb.onError(e);
            return null;
        }
    }

    public void updateUserInfo(String username,
                               String avatar,
                               String gender,
                               DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.USERNAME, username);
            obj.put(APIS.AVATAR, avatar);
            obj.put(APIS.GENDER, gender);
            OkHttpHelper.getInstance()
                    .post(APIS.UPDATE_USER_INFO_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void checkUpdate(DefaultLoadObjCallback<UpdateInfo, Res.UpdateInfoRes> cb) {
        Map<String, String> param = new HashMap<>();
        param.put(APIS.TS, String.valueOf(new Date().getTime()));
        try {
            OkHttpHelper.getInstance().get(APIS.CHECK_UPDATE_URL, getHeader(), param, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void feedback(
            String title,
            String content,
            int verCode,
            String verName,
            List<String> imgs,
            DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.TITLE, title);
            obj.put(APIS.CONTENT, content);
            obj.put(APIS.VER_NAME, verName);
            obj.put(APIS.VER_CODE, verCode);
            JSONArray array = new JSONArray();
            for (String img : imgs) {
                array.put(img);
            }
            obj.put(APIS.IMGS, array);
            OkHttpHelper.getInstance().post(APIS.FEEDBACK_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void likeNews(String newsId, DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.NEWS_ID, newsId);
            OkHttpHelper.getInstance().post(APIS.LIKE_NEWS_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void unLikeNews(String newsId, DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.NEWS_ID, newsId);
            OkHttpHelper.getInstance().post(APIS.UNLIKE_NEWS_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void likeComment(String commentId, DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.COMMENT_ID, commentId);
            OkHttpHelper.getInstance().post(APIS.LIKE_COMMENT_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void unLikeComment(String commentId, DefaultLoadObjCallback<String, Res.DefaultStrRes> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(APIS.COMMENT_ID, commentId);
            OkHttpHelper.getInstance().post(APIS.UNLIKE_COMMENT_URL, getHeader(), obj.toString(), cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void getSigInState(DefaultLoadObjCallback<Res.SignInInfo, Res.SignInRes> cb) {
        try {
            OkHttpHelper.getInstance().get(APIS.GET_SIGN_IN_STATE_URL, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void signIn(DefaultLoadObjCallback<Res.SignInInfo, Res.SignInRes> cb) {
        try {
            OkHttpHelper.getInstance().get(APIS.SIGN_IN_STATE_URL, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    public void getHotNews(
            String game,
            String newsId,
            DefaultLoadObjCallback<Res.ListRes<News>, Res.NewsListRes> cb) {
        String url = String.format(APIS.HOT_NEWS_URL, game, newsId);
        try {
            OkHttpHelper.getInstance().get(url, getHeader(), null, cb);
        } catch (Exception e) {
            cb.onError(e);
        }
    }
}
