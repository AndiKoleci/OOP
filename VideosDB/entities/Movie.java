package entities;

import fileio.MovieInputData;

import java.util.ArrayList;

public final class Movie extends Video {

    private int duration;
    private ArrayList<Rating> ratings;

    public Movie(final MovieInputData movie) {
        super(movie);
        this.duration = movie.getDuration();
        this.ratings = new ArrayList<>();
    }

    public Movie() {
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(final ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }

    /**
     * Calculeaza media filmului ca medie a tururor ratingurilor userilor
     *
     * @return media ratingurilor filmului
     */
    public double averageRating() {
        double s = 0;
        double i = 0;
        for (Rating rating : this.getRatings()) {
            s += rating.getValue();
            i++;
        }
        if (s == 0) {
            return 0;
        }
        return s / i;
    }

    /**
     * Calculeaza de cate ori a fost vazut filmul
     *
     * @return numarul total de viewuri ale filmului
     */
    public int numberOfViews() {
        int s = 0;
        for (User user : Database.getDatabase().getUsers()) {
            if (user.getHistory().containsKey(this.getTitle())) {
                s += user.getHistory().get(this.getTitle());
            }
        }
        return s;
    }

    /**
     * Calculeaza de cate ori a primit "Favorite" filmul
     *
     * @return numarul total de liste de favorite ale userilor in care filmul se afla
     */
    public int timesFavorite() {
        int s = 0;
        for (User user : Database.getDatabase().getUsers()) {
            if (user.getFavoriteMovies().contains(this.getTitle())) {
                s++;
            }
        }
        return s;
    }

}
