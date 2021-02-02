package com.appbirds.movies.retrofit;

import com.appbirds.movies.models.MovieDetail;
import com.appbirds.movies.models.Results;
import com.appbirds.movies.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestClient {

    @GET(Constants.API_URL_TOP_RATED)
    Call<ServerResponse<List<Results>>>
    callApiAndGetServices(@Query("page") int page);

    @GET(Constants.API_URL_MOVIE_DETAILS + "{movie_id}")
    Call<MovieDetail>
    callApiGetMovieDetails(@Path("movie_id") String movie_id);
}
