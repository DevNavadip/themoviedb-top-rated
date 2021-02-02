package com.appbirds.movies.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appbirds.movies.R;
import com.appbirds.movies.models.MovieDetail;
import com.appbirds.movies.models.Results;
import com.appbirds.movies.retrofit.ServerResponse;
import com.appbirds.movies.retrofit.Singleton;
import com.appbirds.movies.utils.Constants;
import com.appbirds.movies.utils.MyPreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends BaseActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private ImageView imgMovie, imgCompanyLogo;
    private TextView txtRateAverage, txtRateCount;
    private TextView txtMovieName, txtDesc, txtMovieGenres, txtMovieLanguage;
    private TextView txtProductionCompaniesName, txtProductionCompaniesCountry, txtMovieReleaseDate;
    private Results mResultsMovies;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setActivityContext(this);
        initView();
    }

    // initialize view
    private void initView() {
        progress_bar = findViewById(R.id.progress_bar);
        imgMovie = findViewById(R.id.imgMovie);
        imgCompanyLogo = findViewById(R.id.imgCompanyLogo);
        txtRateAverage = findViewById(R.id.txtRateAverage);
        txtRateCount = findViewById(R.id.txtRateCount);
        txtMovieName = findViewById(R.id.txtMovieName);
        txtDesc = findViewById(R.id.txtDesc);
        txtMovieGenres = findViewById(R.id.txtMovieGenres);
        txtMovieLanguage = findViewById(R.id.txtMovieLanguage);
        txtProductionCompaniesName = findViewById(R.id.txtProductionCompaniesName);
        txtProductionCompaniesCountry = findViewById(R.id.txtProductionCompaniesCountry);
        txtMovieReleaseDate = findViewById(R.id.txtMovieReleaseDate);

        mResultsMovies = getIntent().getParcelableExtra(Constants.BUNDLE_MOVIE);
        if (mResultsMovies != null) {
            callApiAndGetMovieDetails();
        }
    }

    /*
     * api call to get movie details
     * */
    private void callApiAndGetMovieDetails() {
        hideKeyboard();
        if (!isInternetConnected()) {
            makeToast(getString(R.string.internet_error));

            //check if network not available
            setMovieDataIfNetworkNotAvailable();

            return;
        }


        showProgress();
        Call<MovieDetail> responseCall = Singleton.getInstance().getRestClient().callApiGetMovieDetails(mResultsMovies.getId());
        responseCall.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    hideProgress();
                    MovieDetail movieDetail = response.body();
                    if (movieDetail != null) {
                        setMovieData(movieDetail);
                    }
                } else {
                    hideProgress();
                    makeToast(getResources().getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {
                hideProgress();
                if (t instanceof IOException) {
                    makeToast(getResources().getString(R.string.internet_error));
                } else {
                    makeToast(getResources().getString(R.string.something_went_wrong));
                }
            }
        });
    }

    private void setMovieData(MovieDetail movieDetail) {
        if (!TextUtils.isEmpty(movieDetail.getBackdrop_path())) {
            progress_bar.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(Constants.IMAGE_URL + movieDetail.getBackdrop_path())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progress_bar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgMovie);
        } else
            progress_bar.setVisibility(View.GONE);

        if (movieDetail.getProduction_companies() != null && !movieDetail.getProduction_companies().isEmpty() && movieDetail.getProduction_companies().get(0) != null) {
            if (!TextUtils.isEmpty(movieDetail.getProduction_companies().get(0).getLogo_path()))
                Glide.with(this)
                        .load(Constants.IMAGE_URL + movieDetail.getProduction_companies().get(0).getLogo_path())
                        .into(imgCompanyLogo);

            if (!TextUtils.isEmpty(movieDetail.getProduction_companies().get(0).getName()))
                txtProductionCompaniesName.setText(movieDetail.getProduction_companies().get(0).getName());
            if (!TextUtils.isEmpty(movieDetail.getProduction_companies().get(0).getOrigin_country()))
                txtProductionCompaniesCountry.setText(movieDetail.getProduction_companies().get(0).getOrigin_country());
        }

        if (!TextUtils.isEmpty(movieDetail.getVote_average()))
            txtRateAverage.setText(movieDetail.getVote_average());
        if (!TextUtils.isEmpty(movieDetail.getVote_count()))
            txtRateCount.setText(movieDetail.getVote_count());
        if (!TextUtils.isEmpty(movieDetail.getOriginal_title()))
            txtMovieName.setText(movieDetail.getOriginal_title());
        if (!TextUtils.isEmpty(movieDetail.getOverview()))
            txtDesc.setText(movieDetail.getOverview());

        if (movieDetail.getGenres() != null && !movieDetail.getGenres().isEmpty()) {
            if (movieDetail.getGenres().size() == 1) {
                if (!TextUtils.isEmpty(movieDetail.getGenres().get(0).getName()))
                    txtMovieGenres.setText(movieDetail.getGenres().get(0).getName());
            } else {
                String genres = "";
                for (int i = 0; i < movieDetail.getGenres().size(); i++) {
                    if (!TextUtils.isEmpty(movieDetail.getGenres().get(i).getName())) {
                        if (i == 0)
                            genres = genres + movieDetail.getGenres().get(i).getName();
                        else
                            genres = genres.concat(", " + movieDetail.getGenres().get(i).getName());
                    }
                }
                txtMovieGenres.setText(genres);
            }
        }

        if (movieDetail.getSpoken_languages() != null && !movieDetail.getSpoken_languages().isEmpty()) {
            if (movieDetail.getSpoken_languages().size() == 1) {
                if (!TextUtils.isEmpty(movieDetail.getSpoken_languages().get(0).getName()))
                    txtMovieLanguage.setText(movieDetail.getSpoken_languages().get(0).getName());
            } else {
                String lang = "";
                for (int i = 0; i < movieDetail.getSpoken_languages().size(); i++) {
                    if (!TextUtils.isEmpty(movieDetail.getSpoken_languages().get(i).getName())) {
                        if (i == 0)
                            lang = lang + movieDetail.getSpoken_languages().get(i).getName();
                        else
                            lang = lang.concat(", " + movieDetail.getSpoken_languages().get(i).getName());
                    }
                }
                txtMovieLanguage.setText(lang);
            }
        }

        if (!TextUtils.isEmpty(movieDetail.getRelease_date()))
            txtMovieReleaseDate.setText(movieDetail.getRelease_date());
    }

    private void setMovieDataIfNetworkNotAvailable() {
        if (!TextUtils.isEmpty(mResultsMovies.getBackdrop_path())) {
            progress_bar.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(Constants.IMAGE_URL + mResultsMovies.getBackdrop_path())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progress_bar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgMovie);
        } else
            progress_bar.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(mResultsMovies.getVote_average()))
            txtRateAverage.setText(mResultsMovies.getVote_average());
        if (!TextUtils.isEmpty(mResultsMovies.getVote_count()))
            txtRateCount.setText(mResultsMovies.getVote_count());
        if (!TextUtils.isEmpty(mResultsMovies.getOriginal_title()))
            txtMovieName.setText(mResultsMovies.getOriginal_title());
        if (!TextUtils.isEmpty(mResultsMovies.getOverview()))
            txtDesc.setText(mResultsMovies.getOverview());

        if (!TextUtils.isEmpty(mResultsMovies.getRelease_date()))
            txtMovieReleaseDate.setText(mResultsMovies.getRelease_date());
    }
}