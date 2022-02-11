package com.scrat.gogo.module.race.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestOptions;
import com.scrat.gogo.R;
import com.scrat.gogo.data.model.Race;
import com.scrat.gogo.data.model.RaceGroupItem;
import com.scrat.gogo.databinding.FragmentRaceListBinding;
import com.scrat.gogo.framework.common.BaseFragment;
import com.scrat.gogo.framework.common.BaseOnItemClickListener;
import com.scrat.gogo.framework.common.BaseRecyclerViewAdapter;
import com.scrat.gogo.framework.common.BaseRecyclerViewHolder;
import com.scrat.gogo.framework.common.BaseRecyclerViewOnScrollListener;
import com.scrat.gogo.framework.glide.GlideApp;
import com.scrat.gogo.framework.glide.GlideRequests;
import com.scrat.gogo.framework.glide.GlideRoundTransform;
import com.scrat.gogo.module.race.betting.BettingActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by scrat on 2017/11/14.
 */

public class RaceListFragment extends BaseFragment implements RaceListContract.View {
    private FragmentRaceListBinding binding;
    private RaceListContract.Presenter presenter;
    private BaseRecyclerViewOnScrollListener loadMoreListener;
    private Adapter adapter;

    public static RaceListFragment newInstance() {
        Bundle args = new Bundle();
        RaceListFragment fragment = new RaceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String getFragmentName() {
        return "RaceListFragment";
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentRaceListBinding.inflate(inflater, container, false);

        binding.srl.setOnRefreshListener(() -> presenter.loadData(true));
        binding.list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.list.setLayoutManager(layoutManager);
        loadMoreListener = new BaseRecyclerViewOnScrollListener(
                layoutManager, () -> presenter.loadData(false));
        binding.list.addOnScrollListener(loadMoreListener);
        GlideRequests glideRequests = GlideApp.with(this);
        adapter = new Adapter(glideRequests, race -> BettingActivity.show(getActivity(), race));
        binding.list.setAdapter(adapter);
        binding.topBar.setOnClickListener(view -> layoutManager.scrollToPosition(0));

        new RaceListPresenter(this);
        presenter.loadData(true);

        return binding.getRoot();
    }

    @Override
    public void setPresenter(RaceListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingList() {
        showLoading();
    }

    @Override
    public void showListData(List<RaceGroupItem> list, boolean replace) {
        hideLoading();
        adapter.initData(list, replace);
    }

    @Override
    public void showNoMoreListData() {
        hideLoading();
    }

    @Override
    public void showEmptyList() {
        hideLoading();
    }

    @Override
    public void showLoadingListError(String e) {
        hideLoading();
        showMsg(e);
    }

    private void showLoading() {
        binding.srl.setRefreshing(true);
    }

    private void hideLoading() {
        binding.srl.setRefreshing(false);
        loadMoreListener.setLoading(false);
    }

    private static class GroupItem<T> {
        private int type;
        private T t;

        private GroupItem(int type, T t) {
            this.type = type;
            this.t = t;
        }

        public int getType() {
            return type;
        }

        public T getT() {
            return t;
        }
    }

    private static class Adapter extends BaseRecyclerViewAdapter<GroupItem> {
        private GlideRequests request;
        private SimpleDateFormat sdf;
        private BaseOnItemClickListener<Race> onItemClickListener;
        private RequestOptions options;
        private static final int TYPE_TITLE = 11;
        private static final int TYPE_ITEM = 22;

        private Adapter(GlideRequests requests, BaseOnItemClickListener<Race> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            request = requests;
            sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            options = new RequestOptions()
                    .centerCrop()
                    .transform(new GlideRoundTransform(4));
        }

        private String formatGroupTitle(String dt) {
            try {
                Date date = sdf.parse(dt);
                return DateFormat.format("M月d日", date).toString();
            } catch (ParseException e) {
                return dt;
            }
        }

        private synchronized void initData(List<RaceGroupItem> items, boolean replace) {
            if (replace) {
                list.clear();
            }
            for (RaceGroupItem item : items) {
                list.add(new GroupItem<>(TYPE_TITLE, formatGroupTitle(item.getDt())));
                for (Race race : item.getItems()) {
                    list.add(new GroupItem<>(TYPE_ITEM, race));
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            int type = super.getItemViewType(position);
            if (type != VIEW_TYPE_CONTENT) {
                return type;
            }
            GroupItem<Race> item = getItem(getRealPosition(position));
            if (item.getType() == TYPE_ITEM) {
                return TYPE_ITEM;
            }

            return TYPE_TITLE;
        }

        @Override
        protected void onBindItemViewHolder(
                BaseRecyclerViewHolder holder, int position, GroupItem groupItem) {

            if (groupItem.getType() == TYPE_TITLE) {
                holder.setText(R.id.group_name, (String) groupItem.getT());
                return;
            }

            Race race = (Race) groupItem.getT();
            holder.setText(R.id.race_name, race.getRaceName())
                    .setText(R.id.team_a_name, race.getTeamA().getTeamName())
                    .setText(R.id.team_b_name, race.getTeamB().getTeamName())
                    .setText(R.id.date, DateFormat.format("H:mm", race.getRaceTs()))
                    .setOnClickListener(view -> onItemClickListener.onItemClick(race))
                    .setOnClickListener(R.id.betting_btn, view -> onItemClickListener.onItemClick(race));

            request.load(race.getTeamA().getLogo()).apply(options)
                    .into(holder.getImageView(R.id.logo_a));
            request.load(race.getTeamB().getLogo()).apply(options)
                    .into(holder.getImageView(R.id.logo_b));
            if (!"ready".equals(race.getStatus())) {
                holder.setText(R.id.betting_btn, "结果")
                        .setText(R.id.result, getScore(race))
                        .setVisibility(R.id.ready_tip, false)
                        .setBackground(R.id.betting_btn, R.drawable.bg_c01_3dp);
            } else {
                holder.setText(R.id.betting_btn, "竞猜")
                        .setText(R.id.result, "未开始")
                        .setVisibility(R.id.ready_tip, true)
                        .setBackground(R.id.betting_btn, R.drawable.bg_c02_3dp);
            }
        }

        private String getScore(Race race) {
            return String.format("%s : %s", race.getScoreA(), race.getScoreB());
        }

        @Override
        protected BaseRecyclerViewHolder onCreateRecycleItemView(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v;
            switch (viewType) {
                case TYPE_TITLE:
                    v = inflater.inflate(R.layout.list_group_item_race, parent, false);
                    break;
                default:
                    v = inflater.inflate(R.layout.list_item_race, parent, false);
                    break;
            }
            return new BaseRecyclerViewHolder(v);
        }
    }
}
