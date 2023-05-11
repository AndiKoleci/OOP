package entities;

import fileio.ShowInput;

import java.util.ArrayList;

public abstract class Video {
    private String title;
    private int year;
    private ArrayList<String> cast;
    private ArrayList<String> genres;

    protected Video(final ShowInput show) {
        this.title = show.getTitle();
        this.year = show.getYear();
        this.cast = show.getCast();
        this.genres = show.getGenres();
    }

    protected Video() {
    }

    /**
     * @return titlul filmului/serialului
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return anul filmului/serialului
     */
    public int getYear() {
        return year;
    }

    /**
     * @return actorii care au jucat in film/serial
     */
    public ArrayList<String> getCast() {
        return cast;
    }

    /**
     * @return genurile din care face parte filmul/serialul
     */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     * referinta la functia averageRating din clasa Movie/Serial
     *
     * @return media ratingurilor filmului/serialului
     */
    public abstract double averageRating();

    /**
     * referinta la functia averageRating din clasa Movie/Serial
     *
     * @return numarul total de viewuri ale filmului/serialului
     */
    public abstract int numberOfViews();

}
