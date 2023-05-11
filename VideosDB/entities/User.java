package entities;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.Map;

public final class User {
    private String username;
    private String subscriptionType;
    private Map<String, Integer> history;
    private ArrayList<String> favoriteMovies;
    private ArrayList<Rating> ratings;

    public User(final UserInputData user) {
        this.favoriteMovies = user.getFavoriteMovies();
        this.username = user.getUsername();
        this.subscriptionType = user.getSubscriptionType();
        this.history = user.getHistory();
        this.ratings = new ArrayList<>();
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public void setHistory(final Map<String, Integer> history) {
        this.history = history;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(final ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }
}
