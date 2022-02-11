package com.scrat.gogo.module.race.team.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestOptions;
import com.scrat.gogo.R;
import com.scrat.gogo.data.model.Team;
import com.scrat.gogo.databinding.FragmentTeamListBinding;
import com.scrat.gogo.framework.common.BaseFragment;
import com.scrat.gogo.framework.common.BaseOnItemClickListener;
import com.scrat.gogo.framework.common.BaseRecyclerViewAdapter;
import com.scrat.gogo.framework.common.BaseRecyclerViewHolder;
import com.scrat.gogo.framework.common.BaseRecyclerViewOnScrollListener;
import com.scrat.gogo.framework.glide.GlideApp;
import com.scrat.gogo.framework.glide.GlideRequests;
import com.scrat.gogo.framework.glide.GlideRoundTransform;

import java.util.List;

/**
 * Created by scrat on 2017/11/15.
 */

public class TeamListFragment extends BaseFragment implements TeamListContract.View {

    private Adapter adapter;
    private GlideRequests glideRequests;
    private FragmentTeamListBinding binding;
    private TeamListContract.Presenter presenter;
    private BaseRecyclerViewOnScrollListener loadMoreListener;

    public static TeamListFragment newInstance() {
        Bundle args = new Bundle();
        TeamListFragment fragment = new TeamListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    protected String getFragmentName() {
        return "TeamListFragment";
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentTeamListBinding.inflate(inflater, container, false);

        binding.srl.setOnRefreshListener(() -> presenter.loadData(true));
        binding.list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.list.setLayoutManager(layoutManager);
        glideRequests = GlideApp.with(this);
        adapter = new Adapter(glideRequests, team -> {
            // TODO
        });
        binding.list.setAdapter(adapter);
        loadMoreListener = new BaseRecyclerViewOnScrollListener(
                layoutManager, () -> presenter.loadData(false));
        binding.list.addOnScrollListener(loadMoreListener);

        new TeamListPresenter(this);
        presenter.loadData(true);

        return binding.getRoot();
    }

    @Override
    public void setPresenter(TeamListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingList() {
        showLoading();
    }

    @Override
    public void showListData(List<Team> list, boolean replace) {
        hideLoading();
        adapter.setData(list, replace);
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

    private static class Adapter extends BaseRecyclerViewAdapter<Team> {
        private final GlideRequests request;
        private final BaseOnItemClickListener<Team> onItemClickListener;
        private RequestOptions options;

        private Adapter(GlideRequests requests, BaseOnItemClickListener<Team> onItemClickListener) {
            request = requests;
            options = new RequestOptions()
                    .centerCrop()
                    .transform(new GlideRoundTransform(4));
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        protected void onBindItemViewHolder(
                BaseRecyclerViewHolder holder, int position, Team team) {
            request.load(team.getLogo()).apply(options).into(holder.getImageView(R.id.logo));
            holder.setText(R.id.name, team.getTeamName());
        }

        @Override
        protected BaseRecyclerViewHolder onCreateRecycleItemView(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_team, parent, false);
            return new BaseRecyclerViewHolder(v);
        }
    }
}
