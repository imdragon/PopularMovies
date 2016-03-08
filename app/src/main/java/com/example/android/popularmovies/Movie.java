package com.example.android.popularmovies;

public class Movie {

    public String title;
    public String poster;
    public String synopsis;
    public String releaseDate;
    public String rating;

    public Movie() {

    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public Movie(String title, String poster, String synopsis, String releaseDate, String rating) {
        this.title = title;
        this.poster = poster;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }


}
