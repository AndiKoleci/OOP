package entertainment;

import entities.Rating;

import java.util.ArrayList;

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;
    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * List of ratings for each season
     */
    private ArrayList<Rating> ratings;

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new ArrayList<>();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public int getCurrentSeason() {
        return currentSeason;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(final ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + duration
                + '}';
    }

    /**
     * Calculeaza media sezonului ca medie a tururor ratingurilor userilor
     *
     * @return media ratingurilor sezonului
     */
    public double averageRating() {
        double s = 0;
        double i = 0;
        for (Rating rating : this.getRatings()) {
            s += rating.getValue();
            //System.out.println(rating.getValue());
            i++;
        }
        if (s == 0) {
            return 0;
        }
        return s / i;
    }
}

