package androidlab.project.models;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie {
    private int id;
    private String title;
    private int year;
    private ArrayList<String> genres;
    private String duration;
    private Date releaseDate;
    private String storyLine;
    private ArrayList<String> actors;
    private double imdbRating;
    private String posterurl;
    private float rating;
    private boolean addedToWatchList;

    public boolean isAddedToWatchList() {
        return addedToWatchList;
    }

    public void setAddedToWatchList(boolean addedToWatchList) {
        this.addedToWatchList = addedToWatchList;
    }


    public Movie() {
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Movie(int id, String title, int year, ArrayList<String> genres, String duration, Date releaseDate, String storyLine, ArrayList<String> actors, double imdbRating, String posterurl, float rating) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.storyLine = storyLine;
        this.actors = actors;
        this.imdbRating = imdbRating;
        this.posterurl = posterurl;
        this.rating = rating;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStoryLine() {
        return storyLine;
    }

    public void setStoryLine(String storyLine) {
        this.storyLine = storyLine;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    public double getImbdRating() {
        return imdbRating;
    }

    public void setImbdRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getPosterurl() {
        return posterurl;
    }

    public void setPosterurl(String posterurl) {
        this.posterurl = posterurl;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", genres=" + genres +
                ", duration='" + duration + '\'' +
                ", releaseDate=" + releaseDate +
                ", storyLine='" + storyLine + '\'' +
                ", actors=" + actors +
                ", imbdRating=" + imdbRating +
                ", posterurl=" + posterurl +
                '}';
    }
}
