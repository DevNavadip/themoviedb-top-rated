package com.appbirds.movies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Results implements Parcelable {
    private String overview;

    private String original_language;

    private String original_title;

    private String video;

    private String title;

    private String[] genre_ids;

    private String poster_path;

    private String backdrop_path;

    private String release_date;

    private String popularity;

    private String vote_average;

    private String id;

    private String adult;

    private String vote_count;

    protected Results(Parcel in) {
        overview = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        video = in.readString();
        title = in.readString();
        genre_ids = in.createStringArray();
        poster_path = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
        popularity = in.readString();
        vote_average = in.readString();
        id = in.readString();
        adult = in.readString();
        vote_count = in.readString();
    }

    public static final Creator<Results> CREATOR = new Creator<Results>() {
        @Override
        public Results createFromParcel(Parcel in) {
            return new Results(in);
        }

        @Override
        public Results[] newArray(int size) {
            return new Results[size];
        }
    };

    public String getOverview() {
        return checkNull(overview);
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginal_language() {
        return checkNull(original_language);
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return checkNull(original_title);
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getVideo() {
        return checkNull(video);
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTitle() {
        return checkNull(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(String[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getPoster_path() {
        return checkNull(poster_path);
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return checkNull(backdrop_path);
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getRelease_date() {
        return checkNull(release_date);
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPopularity() {
        return checkNull(popularity);
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVote_average() {
        return checkNull(vote_average);
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getId() {
        return checkNull(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdult() {
        return checkNull(adult);
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getVote_count() {
        return checkNull(vote_count);
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    private String checkNull(String data) {
        if (data == null) {
            return "";
        }
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(overview);
        parcel.writeString(original_language);
        parcel.writeString(original_title);
        parcel.writeString(video);
        parcel.writeString(title);
        parcel.writeStringArray(genre_ids);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeString(release_date);
        parcel.writeString(popularity);
        parcel.writeString(vote_average);
        parcel.writeString(id);
        parcel.writeString(adult);
        parcel.writeString(vote_count);
    }
}
