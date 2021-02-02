package com.appbirds.movies.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/original";
    //themoviedb user token
    public static String bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwNjlhNDIxZDViMmY4MDA4ZDY0NDNkYWUyZWFjNGJiNCIsInN1YiI6IjYwMTgwNGVmNzMxNGExMDAzYmY1MzhiMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.jgsmbaUEiEAN62A--p7makQRM0qd_J3yTyN6mYIRRyQ";

    //api endpoints
    public static final String API_URL_TOP_RATED = "movie/top_rated";
    public static final String API_URL_MOVIE_DETAILS = "movie/";

    //    public static final String INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String INPUT_FORMAT = "MM-dd-yyyy HH:mm:ss";
    public static final String OUTPUT_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MMM_dd_yyyy = "MMM dd, yyyy";
    public static final String DATE_FORMAT_dd_MM_yyyy = "dd/MM/yyyy";

    //bundle
    public static final String BUNDLE_MOVIE = "BUNDLE_SERVICE";

    public static String changeDateFormat(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(INPUT_FORMAT, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(OUTPUT_FORMAT, Locale.getDefault());
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String changeDateFormat(String time, String INPUT_FORMAT, String OUTPUT_FORMAT) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(INPUT_FORMAT, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(OUTPUT_FORMAT, Locale.getDefault());
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
