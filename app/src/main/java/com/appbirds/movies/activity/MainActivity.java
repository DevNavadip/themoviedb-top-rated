package com.appbirds.movies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appbirds.movies.R;
import com.appbirds.movies.adapter.MoviesAdapter;
import com.appbirds.movies.listener.ItemListener;
import com.appbirds.movies.models.Results;
import com.appbirds.movies.retrofit.ServerResponse;
import com.appbirds.movies.retrofit.Singleton;
import com.appbirds.movies.utils.Constants;
import com.appbirds.movies.utils.MyPreferences;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements ItemListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ShimmerFrameLayout mShimmerFrameLayout;
    private RecyclerView mRecyclerView;
    private TextView mTextViewNoServices;
    private List<Results> mResultsList;
    private MoviesAdapter mMoviesAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isLoadMore = false;
    private int page = 1, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActivityContext(this);
        initView();
    }

    // initialize view
    private void initView() {
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mShimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        mRecyclerView = findViewById(R.id.rlServices);
        mTextViewNoServices = findViewById(R.id.textViewServices);
        mResultsList = new ArrayList<>();

        //set list adapter
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMoviesAdapter = new MoviesAdapter(this, mResultsList);
        mMoviesAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        callApiAndGetTopRatedMovies(page, true);
        loadMore();
    }

    /*
     * api call to get movies list
     * */
    private void callApiAndGetTopRatedMovies(final int page_no, boolean showDialog) {
        hideKeyboard();
        if (!isInternetConnected()) {
            if (showDialog)
                makeToast(getString(R.string.internet_error));

            //check if network not available
            if (MyPreferences.getObject(MainActivity.this, MyPreferences.MODEL_MOVIES, ServerResponse.class) != null) {
                ServerResponse serverResponse = MyPreferences.getObject(MainActivity.this, MyPreferences.MODEL_MOVIES, ServerResponse.class);
                List<Results> movieList = (List<Results>) serverResponse.getResults();
                if (!movieList.isEmpty()) {
                    if (!mResultsList.isEmpty())
                        mResultsList.clear();
                    mResultsList.addAll(movieList);
                    mShimmerFrameLayout.stopShimmerAnimation();
                    mShimmerFrameLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    notifyAdapter();
                }
            } else
                mSwipeRefreshLayout.setRefreshing(false);

            return;
        }

        if (showDialog)
            showProgress();

        Call<ServerResponse<List<Results>>> responseCall = Singleton.getInstance().getRestClient().callApiAndGetServices(page_no);
        responseCall.enqueue(new Callback<ServerResponse<List<Results>>>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse<List<Results>>> call, @NonNull Response<ServerResponse<List<Results>>> response) {
                if (response.isSuccessful()) {
                    mShimmerFrameLayout.stopShimmerAnimation();
                    mShimmerFrameLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    hideProgress();
                    ServerResponse serverResponse = response.body();
                    if (serverResponse != null) {

                        List<Results> movieList = (List<Results>) serverResponse.getResults();

                        if (mResultsList.isEmpty())
                            MyPreferences.setObject(MainActivity.this, MyPreferences.MODEL_MOVIES, serverResponse);

                        //check for refresh view
                        if (page == 1)
                            mResultsList.clear();

                        mResultsList.addAll(movieList);
                        notifyAdapter();

                        //check for page no.
                        int currentPage = Integer.parseInt(serverResponse.getPage());
                        int totalPage = Integer.parseInt(serverResponse.getTotal_pages());
                        if (currentPage < totalPage)
                            page = currentPage + 1;
                        else {
                            page = 1;
                            mResultsList.clear();
                            callApiAndGetTopRatedMovies(page_no, true);
                        }
                    }
                } else {
                    hideProgress();
                    makeToast(getResources().getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse<List<Results>>> call, @NonNull Throwable t) {
                hideProgress();
                if (t instanceof IOException) {
                    makeToast(getResources().getString(R.string.internet_error));
                } else {
                    makeToast(getResources().getString(R.string.something_went_wrong));
                }
            }
        });
    }

    private void notifyAdapter() {
        if (mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);

        mMoviesAdapter.notifyDataSetChanged();
        // for load more data
        isLoadMore = false;

        if (mResultsList.size() > 0) {
            mTextViewNoServices.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mTextViewNoServices.setVisibility(View.VISIBLE);
        }
    }

    /**
     * load more movies
     */
    private void loadMore() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                Log.e(TAG, "onScrolled: visibleItemCount:" + visibleItemCount);
                Log.e(TAG, "onScrolled: firstVisibleItemPosition:" + firstVisibleItemPosition);
                Log.e(TAG, "onScrolled: totalItemCount:" + totalItemCount);
                if ((visibleItemCount + firstVisibleItemPosition) > totalItemCount - 1) {

                    if (!isLoadMore) {
                        //position increment
                        isLoadMore = true;
                        // callback API to loadmoredata
                        callApiAndGetTopRatedMovies(page, false);
                    }

                }
            }
        });

    }

    @Override
    public void onItemClick(Object item, int pos, String type) {
        Results results = (Results) item;
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(Constants.BUNDLE_MOVIE, results);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 1;
        callApiAndGetTopRatedMovies(page, false);
    }
}