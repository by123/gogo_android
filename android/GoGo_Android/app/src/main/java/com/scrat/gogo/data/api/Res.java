package com.scrat.gogo.data.api;

import com.google.gson.annotations.SerializedName;
import com.scrat.gogo.data.model.Address;
import com.scrat.gogo.data.model.Betting;
import com.scrat.gogo.data.model.BettingInfo;
import com.scrat.gogo.data.model.CoinPlan;
import com.scrat.gogo.data.model.Comment;
import com.scrat.gogo.data.model.Goods;
import com.scrat.gogo.data.model.GoodsDetail;
import com.scrat.gogo.data.model.ExchangeHistory;
import com.scrat.gogo.data.model.News;
import com.scrat.gogo.data.model.NewsDetail;
import com.scrat.gogo.data.model.RaceGroupItem;
import com.scrat.gogo.data.model.RaceInfo;
import com.scrat.gogo.data.model.SignIn;
import com.scrat.gogo.data.model.Team;
import com.scrat.gogo.data.model.TokenInfo;
import com.scrat.gogo.data.model.UpdateInfo;
import com.scrat.gogo.data.model.Uptoken;
import com.scrat.gogo.data.model.User;
import com.scrat.gogo.data.model.UserInfo;
import com.scrat.gogo.data.model.WxPayInfo;
import com.scrat.gogo.framework.common.BaseResponse;

import java.util.List;

/**
 * Created by scrat on 2017/11/4.
 */

public class Res {
    public class DefaultStrRes extends BaseResponse<String> {
    }

    public class TokenRes extends BaseResponse<TokenInfo> {
    }

    public static class ListRes<T> {
        private static final String NO_MORE_DATA_INDEX = "-1";
        private String index;
        private List<T> items;

        public static boolean hasMoreData(String index) {
            return !NO_MORE_DATA_INDEX.equals(index);
        }

        public boolean hasMoreData() {
            return !NO_MORE_DATA_INDEX.equals(index);
        }

        public boolean isEmpty() {
            return items == null || items.isEmpty();
        }

        public String getIndex() {
            return index;
        }

        public List<T> getItems() {
            return items;
        }
    }

    public class NewsListRes extends BaseResponse<ListRes<News>> {
    }

    public class BannerRes extends BaseResponse<List<News>> {}

    public class NewsDetailRes extends BaseResponse<NewsDetail> {}

    public class CommentItem {
        private Comment comment;
        private User user;

        public Comment getComment() {
            return comment;
        }

        public User getUser() {
            return user;
        }
    }

    public class CommentItemListRes extends BaseResponse<ListRes<CommentItem>> {}

    public class CommentRes extends BaseResponse<Comment> {}

    public class GoodsListRes extends BaseResponse<ListRes<Goods>> {}

    public class UptokenRes extends BaseResponse<Uptoken> {}

    public class WxPayInfoRes extends BaseResponse<WxPayInfo> {}

    public class UserInfoRes extends BaseResponse<UserInfo> {}

    public class CoinPlanListRes extends BaseResponse<List<CoinPlan>> {}

    public class RaceGroupItemRes extends BaseResponse<ListRes<RaceGroupItem>> {}

    public class TeamListRes extends BaseResponse<ListRes<Team>> {}

    public class BettingInfoRes extends BaseResponse<BettingInfo> {}

    public class BettingInfoListRes extends BaseResponse<ListRes<BettingInfo>> {}

    public class GoodsDetailRes extends BaseResponse<GoodsDetail> {}

    public class ExchangeHistoryListRes extends BaseResponse<ListRes<ExchangeHistory>> {}

    public class AddressRes extends BaseResponse<Address> {}

    public class UpdateInfoRes extends BaseResponse<UpdateInfo> {}

    public class RaceInfoRes extends BaseResponse<RaceInfo> {}

    public class BettingListRes extends BaseResponse<List<Betting>> {}

    public class SignInInfo {
        private long coin;
        @SerializedName("sign_in")
        private SignIn signIn;

        public long getCoin() {
            return coin;
        }

        public SignIn getSignIn() {
            return signIn;
        }
    }

    public class SignInRes extends BaseResponse<SignInInfo> {}
}
