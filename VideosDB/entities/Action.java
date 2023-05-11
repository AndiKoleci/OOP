package entities;

import common.Constants;
import entertainment.Genre;
import entertainment.Season;
import fileio.ActionInputData;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class Action {

    private final int actionId;
    private final String actionType;
    private final String type;
    private final String username;
    private final String objectType;
    private final String sortType;
    private final String criteria;
    private final String title;
    private final String genre;
    private final int number;
    private final double grade;
    private final int seasonNumber;
    private final List<List<String>> filters;

    public Action(final ActionInputData action) {
        this.actionId = action.getActionId();
        this.actionType = action.getActionType();
        this.type = action.getType();
        this.username = action.getUsername();
        this.objectType = action.getObjectType();
        this.sortType = action.getSortType();
        this.criteria = action.getCriteria();
        this.title = action.getTitle();
        this.genre = action.getGenre();
        this.number = action.getNumber();
        this.grade = action.getGrade();
        this.seasonNumber = action.getSeasonNumber();
        this.filters = action.getFilters();
    }

    /**
     * Executa fiecare comanda din main, in functie de tipul/criteriile comenzii
     * Raspunsul e scris in JSON-ul din database
     */
    public void run() {
        if (this.actionType.equals("command")) {
            if (this.type.equals("favorite")) {
                Database.getDatabase().addElement(this.actionId, favorite());
            }
            if (this.type.equals("view")) {
                Database.getDatabase().addElement(this.actionId, view());
            }
            if (this.type.equals("rating")) {
                Database.getDatabase().addElement(this.actionId, rating());
            }
        }
        if (this.actionType.equals("query")) {
            if (this.criteria.equals("average")) {
                Database.getDatabase().addElement(this.actionId, averageActors());
            }
            if (this.criteria.equals("most_viewed")) {
                Database.getDatabase().addElement(this.actionId, mostViewed());
            }
            if (this.criteria.equals("num_ratings")) {
                Database.getDatabase().addElement(this.actionId, numRatings());
            }
            if (this.criteria.equals("longest")) {
                Database.getDatabase().addElement(this.actionId, longest());
            }
            if (this.criteria.equals("favorite")) {
                Database.getDatabase().addElement(this.actionId, mostFavorite());
            }
            if (this.criteria.equals("ratings")) {
                Database.getDatabase().addElement(this.actionId, bestRatings());
            }
            if (this.criteria.equals("awards")) {
                Database.getDatabase().addElement(this.actionId, bestAwards());
            }
            if (this.criteria.equals("filter_description")) {
                Database.getDatabase().addElement(this.actionId, filterDesc());
            }
        }
        if (this.actionType.equals("recommendation")) {
            if (this.type.equals("standard")) {
                Database.getDatabase().addElement(this.actionId, unseen());
            }
            if (this.type.equals("favorite")) {
                Database.getDatabase().addElement(this.actionId, recommendFavorite());
            }
            if (this.type.equals("best_unseen")) {
                Database.getDatabase().addElement(this.actionId, bestUnseen());
            }
            if (this.type.equals("search")) {
                Database.getDatabase().addElement(this.actionId, search());
            }
            if (this.type.equals("popular")) {
                Database.getDatabase().addElement(this.actionId, recommendPopular());
            }
        }
    }

    private String favorite() {
        User user = Database.getDatabase().findUser(this.username);
        assert user != null;
        if (!user.getHistory().containsKey(this.title)) {
            return "error -> " + this.title + " is not seen";
        }
        if (user.getFavoriteMovies().contains(this.title)) {
            return "error -> " + this.title + " is already in favourite list";
        }
        user.getFavoriteMovies().add(this.title);
        return "success -> " + this.title + " was added as favourite";
    }

    private String view() {
        User user = Database.getDatabase().findUser(this.username);
        assert user != null;
        if (!user.getHistory().containsKey(this.title)) {
            user.getHistory().put(this.title, 1);
        } else {
            user.getHistory().put(this.title, user.getHistory().get(this.title) + 1);
        }
        return "success -> " + this.title + " was viewed with total views of "
                + user.getHistory().get(this.title);
    }

    private String rating() {
        User user = Database.getDatabase().findUser(this.username);
        assert user != null;
        if (!user.getHistory().containsKey(this.title)) {
            return "error -> " + this.title + " is not seen";
        } else {
            for (Movie movie : Database.getDatabase().getMovies()) {
                if (movie.getTitle().equals(this.title)) {
                    for (Rating rating : movie.getRatings()) {
                        if (rating.getUserOrTitle().equals(user.getUsername())) {
                            return "error -> " + this.title + " has been already rated";
                        }
                    }
                }
            }
            for (Serial serial : Database.getDatabase().getSerials()) {
                if (serial.getTitle().equals(this.title)) {
                    for (Season season : serial.getSeasons()) {
                        if (season.getCurrentSeason() == this.seasonNumber) {
                            for (Rating rating : season.getRatings()) {
                                if (rating.getUserOrTitle().equals(user.getUsername())) {
                                    return "error -> " + this.title + " has been already rated";
                                }
                            }
                        }
                    }
                }
            }
            for (Movie movie : Database.getDatabase().getMovies()) {
                if (movie.getTitle().equals(this.title)) {
                    movie.getRatings().add(new Rating(this.username, this.grade));
                    user.getRatings().add(new Rating(this.title, this.grade));
                }
            }
            for (Serial serial : Database.getDatabase().getSerials()) {
                if (serial.getTitle().equals(this.title)) {
                    for (Season season : serial.getSeasons()) {
                        if (season.getCurrentSeason() == this.seasonNumber) {
                            season.getRatings().add(new Rating(this.username, this.grade));
                            user.getRatings().add(new Rating(this.title, this.grade));
                        }
                    }
                }
            }
        }
        return "success -> " + this.title + " was rated with " + this.grade + " by "
                + this.username;
    }

    private String averageActors() {
        List<Actor> actorsMax = new ArrayList<>();
        List<Actor> actorsMin = new ArrayList<>();
        List<Actor> actorsInOrder = new ArrayList<>();
        for (Actor actor : Database.getDatabase().getActors()) {
            if (actor.averageRating() != 0) {
                actorsMax.add(actor);
                actorsMin.add(actor);
            }
        }

        int end = 0;
        for (int i = 0; i < this.number; i++) {
            if (this.sortType.equals("asc")) {
                Actor minActor = getMinActor(actorsMin);
                actorsMin.remove(minActor);
                if (minActor.getName() != null) {
                    actorsInOrder.add(minActor);
                }
            } else {
                Actor maxActor = getMaxActor(actorsMax);
                actorsMax.remove(maxActor);
                if (maxActor.getName() != null) {
                    actorsInOrder.add(maxActor);
                }
            }
        }
        StringBuilder returnString = new StringBuilder("Query result: [");
        for (Actor actor : actorsInOrder) {
            returnString.append(actor.getName());
            end++;
            if (actorsInOrder.size() != end) {
                returnString.append(", ");
            }
        }
        returnString.append("]");
        return returnString.toString();
    }

    private String bestAwards() {
        List<String> awards = this.filters.get(Constants.AWARDS_CT);
        List<Actor> okActors = new ArrayList<>();
        for (Actor actor : Database.getDatabase().getActors()) {
            int done = 0;
            for (String award : awards) {
                if (!actor.getAwards().containsKey(Utils.stringToAwards(award))) {
                    done = 1;
                    break;
                }
            }
            if (done == 0) {
                okActors.add(actor);
            }
        }

        okActors.sort(Comparator.comparingInt(Actor::timesAwards).thenComparing(Actor::getName));
        if (this.sortType.equals("desc")) {
            Collections.reverse(okActors);
        }

        StringBuilder returnString = new StringBuilder("Query result: [");
        int i = 0;
        for (Actor actor : okActors) {
            if (i != okActors.size() - 1) {
                returnString.append(actor.getName()).append(", ");
                i++;
            } else {
                returnString.append(actor.getName());
            }
        }
        returnString.append("]");
        return returnString.toString();

    }

    private String filterDesc() {
        List<Actor> actorList = new ArrayList<>();
        for (Actor actor : Database.getDatabase().getActors()) {
            int done = 0;
            String actorDesc = actor.getCareerDescription().toLowerCase();
            for (String word : filters.get(2)) {
                StringBuilder wordString1 = new StringBuilder(" ");
                StringBuilder wordString2 = new StringBuilder("-");
                StringBuilder wordString3 = new StringBuilder(word);
                StringBuilder wordString4 = new StringBuilder(word);
                wordString3.append(" ");
                wordString1.append(word);
                wordString2.append(word);
                wordString4.append(".");

                if (!actorDesc.contains(wordString3) && !actorDesc.contains(wordString4)) {
                    if (!actorDesc.contains(wordString1) && !actorDesc.contains(wordString2)) {
                        done = 1;
                    }
                }
            }
            if (done == 0) {
                actorList.add(actor);
            }
        }
        actorList.sort(Comparator.comparing(Actor::getName));
        if (this.sortType.equals("desc")) {
            Collections.reverse(actorList);
        }
        StringBuilder returnString = new StringBuilder("Query result: [");
        int i = 0;
        for (Actor actor : actorList) {
            if (i != actorList.size() - 1) {
                i++;
                returnString.append(actor.getName()).append(", ");

            } else {
                returnString.append(actor.getName());
            }
        }
        returnString.append("]");
        return returnString.toString();
    }

    private String numRatings() {
        List<User> userMax = new ArrayList<>();
        List<User> userMin = new ArrayList<>();
        List<User> userInOrder = new ArrayList<>();
        for (User user : Database.getDatabase().getUsers()) {
            if (user.getRatings().size() != 0) {
                userMax.add(user);
                userMin.add(user);
            }
        }
        for (int i = 0; i < this.number; i++) {
            if (this.sortType.equals("asc")) {
                User minUser = getMinUser(userMin);
                userMin.remove(minUser);
                userInOrder.add(minUser);
                if (userMin.size() == 0) {
                    break;
                }
            } else {
                User maxUser = getMaxUser(userMax);
                userMax.remove(maxUser);
                userInOrder.add(maxUser);
                if (userMax.size() == 0) {
                    break;
                }
            }
        }
        int end = 0;
        StringBuilder returnString = new StringBuilder("Query result: [");
        for (User user : userInOrder) {
            returnString.append(user.getUsername());
            end++;
            if (userInOrder.size() != end) {
                returnString.append(", ");
            }
        }
        returnString.append("]");
        return returnString.toString();
    }

    private String mostFavorite() {
        List<Movie> movieMax = new ArrayList<>();
        List<Movie> movieMin = new ArrayList<>();
        List<Serial> serialMax = new ArrayList<>();
        List<Serial> serialMin = new ArrayList<>();
        List<Movie> moviesInOrder = new ArrayList<>();
        List<Serial> serialsInOrder = new ArrayList<>();
        List<List<String>> filter = this.filters;
        if (this.objectType.equals("movies")) {
            for (Movie movie : Database.getDatabase().getMovies()) {
                if (filter.get(0).get(0) != null && filter.get(1).get(0) == null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == movie.getYear()) {
                        movieMax.add(movie);
                        movieMin.add(movie);
                    }
                } else if (filter.get(1).get(0) != null && filter.get(0).get(0) == null) {
                    if (movie.getGenres().contains(filter.get(1).get(0))) {
                        movieMax.add(movie);
                        movieMin.add(movie);
                    }
                } else if (filter.get(0).get(0) != null && filter.get(1).get(0) != null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == movie.getYear()) {
                        if (movie.getGenres().contains(filter.get(1).get(0))) {
                            movieMax.add(movie);
                            movieMin.add(movie);
                        }
                    }
                } else {
                    movieMax.add(movie);
                    movieMin.add(movie);
                }
            }
            for (int i = 0; i < this.number; i++) {
                if (this.sortType.equals("asc")) {
                    Movie minMovie = getMinMaxMovieFavorite(movieMin);
                    movieMin.remove(minMovie);
                    if (minMovie.getTitle() != null) {
                        moviesInOrder.add(minMovie);
                    }
                    if (movieMin.size() == 0) {
                        break;
                    }
                } else {
                    Movie maxMovie = getMinMaxMovieFavorite(movieMax);
                    movieMax.remove(maxMovie);
                    if (maxMovie.getTitle() != null) {
                        moviesInOrder.add(maxMovie);
                    }
                    if (movieMax.size() == 0) {
                        break;
                    }
                }
            }
            int end = 0;
            StringBuilder returnString = new StringBuilder("Query result: [");
            for (Movie movie : moviesInOrder) {
                returnString.append(movie.getTitle());
                end++;
                if (moviesInOrder.size() != end) {
                    returnString.append(", ");
                }
            }
            returnString.append("]");
            return returnString.toString();
        } else {
            for (Serial serial : Database.getDatabase().getSerials()) {
                if (filter.get(0).get(0) != null && filter.get(1).get(0) == null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == serial.getYear()) {
                        serialMax.add(serial);
                        serialMin.add(serial);
                    }
                } else if (filter.get(1).get(0) != null && filter.get(0).get(0) == null) {
                    if (serial.getGenres().contains(filter.get(1).get(0))) {
                        serialMax.add(serial);
                        serialMin.add(serial);
                    }
                } else if (filter.get(0).get(0) != null && filter.get(1).get(0) != null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == serial.getYear()) {
                        if (serial.getGenres().contains(filter.get(1).get(0))) {
                            serialMax.add(serial);
                            serialMin.add(serial);
                        }
                    }
                } else {
                    serialMax.add(serial);
                    serialMin.add(serial);
                }
            }
            for (int i = 0; i < this.number; i++) {
                if (this.sortType.equals("asc")) {
                    Serial minSerial = getMinMaxSerialFavorite(serialMin);
                    serialMin.remove(minSerial);
                    if (minSerial.getTitle() != null) {
                        serialsInOrder.add(minSerial);
                    }
                    if (serialMin.size() == 0) {
                        break;
                    }
                } else {
                    Serial maxSerial = getMinMaxSerialFavorite(serialMax);
                    serialMax.remove(maxSerial);
                    if (maxSerial.getTitle() != null) {
                        serialsInOrder.add(maxSerial);
                    }
                    if (serialMax.size() == 0) {
                        break;
                    }
                }
            }
            int end = 0;
            StringBuilder returnString = new StringBuilder("Query result: [");
            for (Serial serial : serialsInOrder) {
                returnString.append(serial.getTitle());
                end++;
                if (serialsInOrder.size() != end) {
                    returnString.append(", ");
                }
            }
            returnString.append("]");
            return returnString.toString();
        }
    }

    private String longest() {
        List<Movie> movieMax = new ArrayList<>();
        List<Movie> movieMin = new ArrayList<>();
        List<Serial> serialMax = new ArrayList<>();
        List<Serial> serialMin = new ArrayList<>();
        List<Movie> moviesInOrder = new ArrayList<>();
        List<Serial> serialsInOrder = new ArrayList<>();
        List<List<String>> filter = this.filters;
        if (this.objectType.equals("movies")) {
            for (Movie movie : Database.getDatabase().getMovies()) {
                if (filter.get(0).get(0) != null && filter.get(1).get(0) == null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == movie.getYear()) {
                        movieMax.add(movie);
                        movieMin.add(movie);
                    }
                } else if (filter.get(1).get(0) != null && filter.get(0).get(0) == null) {
                    if (movie.getGenres().contains(filter.get(1).get(0))) {
                        movieMax.add(movie);
                        movieMin.add(movie);
                    }
                } else if (filter.get(0).get(0) != null && filter.get(1).get(0) != null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == movie.getYear()) {
                        if (movie.getGenres().contains(filter.get(1).get(0))) {
                            movieMax.add(movie);
                            movieMin.add(movie);
                        }
                    }
                } else {
                    movieMax.add(movie);
                    movieMin.add(movie);
                }
            }
            for (int i = 0; i < this.number; i++) {
                if (this.sortType.equals("asc")) {
                    Movie minMovie = getMinMaxMovieLength(movieMin);
                    movieMin.remove(minMovie);
                    if (minMovie.getTitle() != null) {
                        moviesInOrder.add(minMovie);
                    }
                    if (movieMin.size() == 0) {
                        break;
                    }
                } else {
                    Movie maxMovie = getMinMaxMovieLength(movieMax);
                    movieMax.remove(maxMovie);
                    if (maxMovie.getTitle() != null) {
                        moviesInOrder.add(maxMovie);
                    }
                    if (movieMax.size() == 0) {
                        break;
                    }
                }
            }
            int end = 0;
            StringBuilder returnString = new StringBuilder("Query result: [");
            for (Movie movie : moviesInOrder) {
                returnString.append(movie.getTitle());
                end++;
                if (moviesInOrder.size() != end) {
                    returnString.append(", ");
                }
            }
            returnString.append("]");
            return returnString.toString();
        } else {
            for (Serial serial : Database.getDatabase().getSerials()) {
                if (filter.get(0).get(0) != null && filter.get(1).get(0) == null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == serial.getYear()) {
                        serialMax.add(serial);
                        serialMin.add(serial);
                    }
                } else if (filter.get(1).get(0) != null && filter.get(0).get(0) == null) {
                    if (serial.getGenres().contains(filter.get(1).get(0))) {
                        serialMax.add(serial);
                        serialMin.add(serial);
                    }
                } else if (filter.get(0).get(0) != null && filter.get(1).get(0) != null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == serial.getYear()) {
                        if (serial.getGenres().contains(filter.get(1).get(0))) {
                            serialMax.add(serial);
                            serialMin.add(serial);
                        }
                    }
                } else {
                    serialMax.add(serial);
                    serialMin.add(serial);
                }
            }
            for (int i = 0; i < this.number; i++) {
                if (this.sortType.equals("asc")) {
                    Serial minSerial = getMinMaxSerialLength(serialMin);
                    serialMin.remove(minSerial);
                    if (minSerial.getTitle() != null) {
                        serialsInOrder.add(minSerial);
                    }
                    if (serialMin.size() == 0) {
                        break;
                    }
                } else {
                    Serial maxSerial = getMinMaxSerialLength(serialMax);
                    serialMax.remove(maxSerial);
                    if (maxSerial.getTitle() != null) {
                        serialsInOrder.add(maxSerial);
                    }
                    if (serialMax.size() == 0) {
                        break;
                    }
                }
            }
            int end = 0;
            StringBuilder returnString = new StringBuilder("Query result: [");
            for (Serial serial : serialsInOrder) {
                returnString.append(serial.getTitle());
                end++;
                if (serialsInOrder.size() != end) {
                    returnString.append(", ");
                }
            }
            returnString.append("]");
            return returnString.toString();
        }
    }

    private String mostViewed() {
        List<Movie> movieMax = new ArrayList<>();
        List<Movie> movieMin = new ArrayList<>();
        List<Serial> serialMax = new ArrayList<>();
        List<Serial> serialMin = new ArrayList<>();
        List<Movie> moviesInOrder = new ArrayList<>();
        List<Serial> serialsInOrder = new ArrayList<>();
        List<List<String>> filter = this.filters;
        if (this.objectType.equals("movies")) {
            for (Movie movie : Database.getDatabase().getMovies()) {
                if (filter.get(0).get(0) != null && filter.get(1).get(0) == null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == movie.getYear()) {
                        movieMax.add(movie);
                        movieMin.add(movie);
                    }
                } else if (filter.get(1).get(0) != null && filter.get(0).get(0) == null) {
                    if (movie.getGenres().contains(filter.get(1).get(0))) {
                        movieMax.add(movie);
                        movieMin.add(movie);
                    }
                } else if (filter.get(0).get(0) != null && filter.get(1).get(0) != null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == movie.getYear()) {
                        if (movie.getGenres().contains(filter.get(1).get(0))) {
                            movieMax.add(movie);
                            movieMin.add(movie);
                        }
                    }
                } else {
                    movieMax.add(movie);
                    movieMin.add(movie);
                }
            }
            for (int i = 0; i < this.number; i++) {
                if (this.sortType.equals("asc")) {
                    Movie minMovie = getMinMaxMovieViews(movieMin);
                    movieMin.remove(minMovie);
                    if (minMovie.getTitle() != null) {
                        moviesInOrder.add(minMovie);
                    }
                    if (movieMin.size() == 0) {
                        break;
                    }
                } else {
                    Movie maxMovie = getMinMaxMovieViews(movieMax);
                    movieMax.remove(maxMovie);
                    if (maxMovie.getTitle() != null) {
                        moviesInOrder.add(maxMovie);
                    }
                    if (movieMax.size() == 0) {
                        break;
                    }
                }
            }
            int end = 0;
            StringBuilder returnString = new StringBuilder("Query result: [");
            for (Movie movie : moviesInOrder) {
                returnString.append(movie.getTitle());
                end++;
                if (moviesInOrder.size() != end) {
                    returnString.append(", ");
                }
            }
            returnString.append("]");
            return returnString.toString();
        } else {
            for (Serial serial : Database.getDatabase().getSerials()) {
                if (filter.get(0).get(0) != null && filter.get(1).get(0) == null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == serial.getYear()) {
                        serialMax.add(serial);
                        serialMin.add(serial);
                    }
                } else if (filter.get(1).get(0) != null && filter.get(0).get(0) == null) {
                    if (serial.getGenres().contains(filter.get(1).get(0))) {
                        serialMax.add(serial);
                        serialMin.add(serial);
                    }
                } else if (filter.get(0).get(0) != null && filter.get(1).get(0) != null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == serial.getYear()) {
                        if (serial.getGenres().contains(filter.get(1).get(0))) {
                            serialMax.add(serial);
                            serialMin.add(serial);
                        }
                    }
                } else {
                    serialMax.add(serial);
                    serialMin.add(serial);
                }
            }
            for (int i = 0; i < this.number; i++) {
                if (this.sortType.equals("asc")) {
                    Serial minSerial = getMinMaxSerialViews(serialMin);
                    serialMin.remove(minSerial);
                    if (minSerial.getTitle() != null) {
                        serialsInOrder.add(minSerial);
                    }
                    if (serialMin.size() == 0) {
                        break;
                    }
                } else {
                    Serial maxSerial = getMinMaxSerialViews(serialMax);
                    serialMax.remove(maxSerial);
                    if (maxSerial.getTitle() != null) {
                        serialsInOrder.add(maxSerial);
                    }
                    if (serialMax.size() == 0) {
                        break;
                    }
                }
            }
            int end = 0;
            StringBuilder returnString = new StringBuilder("Query result: [");
            for (Serial serial : serialsInOrder) {
                returnString.append(serial.getTitle());
                end++;
                if (serialsInOrder.size() != end) {
                    returnString.append(", ");
                }
            }
            returnString.append("]");
            return returnString.toString();
        }
    }

    private String bestRatings() {
        List<Movie> movieMax = new ArrayList<>();
        List<Movie> movieMin = new ArrayList<>();
        List<Serial> serialMax = new ArrayList<>();
        List<Serial> serialMin = new ArrayList<>();
        List<Movie> moviesInOrder = new ArrayList<>();
        List<Serial> serialsInOrder = new ArrayList<>();
        List<List<String>> filter = this.filters;
        if (this.objectType.equals("movies")) {
            for (Movie movie : Database.getDatabase().getMovies()) {
                if (filter.get(0).get(0) != null && filter.get(1).get(0) == null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == movie.getYear()) {
                        movieMax.add(movie);
                        movieMin.add(movie);
                    }
                } else if (filter.get(1).get(0) != null && filter.get(0).get(0) == null) {
                    if (movie.getGenres().contains(filter.get(1).get(0))) {
                        movieMax.add(movie);
                        movieMin.add(movie);
                    }
                } else if (filter.get(0).get(0) != null && filter.get(1).get(0) != null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == movie.getYear()) {
                        if (movie.getGenres().contains(filter.get(1).get(0))) {
                            movieMax.add(movie);
                            movieMin.add(movie);
                        }
                    }
                } else {
                    movieMax.add(movie);
                    movieMin.add(movie);
                }
            }
            for (int i = 0; i < this.number; i++) {
                if (this.sortType.equals("asc")) {
                    Movie minMovie = getMinMaxMovieRating(movieMin);
                    movieMin.remove(minMovie);
                    if (minMovie.getTitle() != null) {
                        moviesInOrder.add(minMovie);
                    }
                    if (movieMin.size() == 0) {
                        break;
                    }
                } else {
                    Movie maxMovie = getMinMaxMovieRating(movieMax);
                    movieMax.remove(maxMovie);
                    if (maxMovie.getTitle() != null) {
                        moviesInOrder.add(maxMovie);
                    }
                    if (movieMax.size() == 0) {
                        break;
                    }
                }
            }
            int end = 0;
            StringBuilder returnString = new StringBuilder("Query result: [");
            for (Movie movie : moviesInOrder) {
                returnString.append(movie.getTitle());
                end++;
                if (moviesInOrder.size() != end) {
                    returnString.append(", ");
                }
            }
            returnString.append("]");
            return returnString.toString();
        } else {
            for (Serial serial : Database.getDatabase().getSerials()) {
                if (filter.get(0).get(0) != null && filter.get(1).get(0) == null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == serial.getYear()) {
                        serialMax.add(serial);
                        serialMin.add(serial);
                    }
                } else if (filter.get(1).get(0) != null && filter.get(0).get(0) == null) {
                    if (serial.getGenres().contains(filter.get(1).get(0))) {
                        serialMax.add(serial);
                        serialMin.add(serial);
                    }
                } else if (filter.get(0).get(0) != null && filter.get(1).get(0) != null) {
                    if (Integer.parseInt(filter.get(0).get(0)) == serial.getYear()) {
                        if (serial.getGenres().contains(filter.get(1).get(0))) {
                            serialMax.add(serial);
                            serialMin.add(serial);
                        }
                    }
                }
            }
            for (int i = 0; i < this.number; i++) {
                if (this.sortType.equals("asc")) {
                    Serial minSerial = getMinMaxSerialRating(serialMin);
                    serialMin.remove(minSerial);
                    if (minSerial.getTitle() != null) {
                        serialsInOrder.add(minSerial);
                    }
                    if (serialMin.size() == 0) {
                        break;
                    }
                } else {
                    Serial maxSerial = getMinMaxSerialRating(serialMax);
                    serialMax.remove(maxSerial);
                    if (maxSerial.getTitle() != null) {
                        serialsInOrder.add(maxSerial);
                    }
                    if (serialMax.size() == 0) {
                        break;
                    }
                }
            }
            int end = 0;
            StringBuilder returnString = new StringBuilder("Query result: [");
            for (Serial serial : serialsInOrder) {
                returnString.append(serial.getTitle());
                end++;
                if (serialsInOrder.size() != end) {
                    returnString.append(", ");
                }
            }
            returnString.append("]");
            return returnString.toString();
        }
    }

    private String unseen() {
        User user = Database.getDatabase().findUser(this.username);
        assert user != null;
        for (Video video : Database.getDatabase().getVideos()) {
            if (!user.getHistory().containsKey(video.getTitle())) {
                return "StandardRecommendation result: " + video.getTitle();
            }
        }
        return "StandardRecommendation cannot be applied!";
    }

    private String bestUnseen() {
        User user = Database.getDatabase().findUser(this.username);
        assert user != null;
        double max = 0;
        Movie favMovie = new Movie();
        Serial favSerial = new Serial();
        Video returnVideo = null;

        for (Movie movie : Database.getDatabase().getMovies()) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                if (movie.averageRating() > max) {
                    max = movie.averageRating();
                    favMovie = movie;
                } else if (max == 0 && movie.averageRating() == 0 && favMovie.getTitle() == null) {
                    favMovie = movie;
                }
            }
        }
        max = 0;
        for (Serial serial : Database.getDatabase().getSerials()) {
            if (!user.getHistory().containsKey(serial.getTitle())) {
                if (serial.averageRating() > max) {
                    max = serial.averageRating();
                    favSerial = serial;
                } else if (max == 0 && serial.averageRating() == 0) {
                    if (favSerial.getTitle() == null) {
                        favSerial = serial;
                    }
                }
            }

        }

        if (favMovie.getTitle() == null && favSerial.getTitle() == null) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        } else if (favMovie.getTitle() == null && favSerial.getTitle() != null) {
            returnVideo = favMovie;
        } else if (favMovie.getTitle() != null && favSerial.getTitle() == null) {
            returnVideo = favMovie;
        } else if (favMovie.averageRating() > favSerial.averageRating()) {
            returnVideo = favMovie;
        } else if (favMovie.averageRating() < favSerial.averageRating()) {
            returnVideo = favSerial;
        } else if (favMovie.averageRating() == favSerial.averageRating()) {
            returnVideo = favMovie;
        }
        if (returnVideo == null) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        }
        return "BestRatedUnseenRecommendation result: " + returnVideo.getTitle();
    }

    private String recommendPopular() {

        User user = Database.getDatabase().findUser(this.username);
        assert user != null;
        if (user.getSubscriptionType().equals("BASIC")) {
            return "PopularRecommendation cannot be applied!";
        }
        List<String> allGenres = new ArrayList<>();
        List<String> genresInOrder = new ArrayList<>();
        String topGenre;
        Video returnVideo = null;
        int max;
        for (Genre genree : Genre.values()) {
            allGenres.add(genree.toString());
        }
        int count;
        int flag;
        int times = allGenres.size();
        for (int i = 0; i < times; i++) {
            topGenre = null;
            max = 0;
            for (String genree : allGenres) {
                count = 0;
                for (Video video : Database.getDatabase().getVideos()) {
                    flag = 0;
                    for (String s : video.getGenres()) {
                        if (s.equalsIgnoreCase(genree)) {
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 1) {
                        count += video.numberOfViews();
                    }
                }
                if (count > max) {
                    max = count;
                    topGenre = genree;
                }
            }
            if (topGenre != null) {
                allGenres.remove(topGenre);
                genresInOrder.add(topGenre);
            }
        }
        int done = 0;
        for (String genree : genresInOrder) {
            for (Video video : Database.getDatabase().getVideos()) {
                flag = 0;
                for (String s : video.getGenres()) {
                    if (s.equalsIgnoreCase(genree)) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1 && !user.getHistory().containsKey(video.getTitle())) {
                    returnVideo = video;
                    done = 1;
                    break;
                }
            }
            if (done == 1) {
                break;
            }
        }
        if (returnVideo == null) {
            return "PopularRecommendation cannot be applied!";
        }
        return "PopularRecommendation result: " + returnVideo.getTitle();
    }

    private String recommendFavorite() {
        User user = Database.getDatabase().findUser(this.username);
        assert user != null;
        if (user.getSubscriptionType().equals("BASIC")) {
            return "FavoriteRecommendation cannot be applied!";
        }
        int max = 0;
        Movie favMovie = new Movie();
        Serial favSerial = new Serial();
        Video returnVideo = null;
        for (Movie movie : Database.getDatabase().getMovies()) {
            if (movie.timesFavorite() > max && !user.getHistory().containsKey(movie.getTitle())) {
                max = movie.timesFavorite();
                favMovie = movie;
            }
        }
        max = 0;
        for (Serial serial : Database.getDatabase().getSerials()) {
            if (serial.timesFavorite() > max && !user.getHistory().containsKey(serial.getTitle())) {
                max = serial.timesFavorite();
                favSerial = serial;
            }
        }
        if (favMovie.getTitle() == null && favSerial.getTitle() == null) {
            return "FavoriteRecommendation cannot be applied!";
        } else if (favMovie.getTitle() == null && favSerial.getTitle() != null) {
            returnVideo = favMovie;
        } else if (favMovie.getTitle() != null && favSerial.getTitle() == null) {
            returnVideo = favMovie;
        } else if (favMovie.timesFavorite() > favSerial.timesFavorite()) {
            returnVideo = favMovie;
        } else if (favMovie.timesFavorite() < favSerial.timesFavorite()) {
            returnVideo = favSerial;
        } else if (favMovie.timesFavorite() == favSerial.timesFavorite()) {
            returnVideo = favMovie;
        }
        if (returnVideo == null) {
            return "FavoriteRecommendation cannot be applied!";
        }
        return "FavoriteRecommendation result: " + returnVideo.getTitle();
    }

    private String search() {
        User user = Database.getDatabase().findUser(this.username);
        assert user != null;
        if (user.getSubscriptionType() != null && user.getSubscriptionType().equals("BASIC")) {
            return "SearchRecommendation cannot applied!";
        }
        List<Video> vList = new ArrayList<>();
        for (Movie movie : Database.getDatabase().getMovies()) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                if (movie.getGenres().contains(genre)) {
                    vList.add(movie);
                }
            }
        }
        for (Serial serial : Database.getDatabase().getSerials()) {
            if (!user.getHistory().containsKey(serial.getTitle())) {
                if (serial.getGenres().contains(genre)) {
                    vList.add(serial);
                }
            }
        }

        vList.sort(Comparator.comparingDouble(Video::averageRating).thenComparing(Video::getTitle));
        if (vList.size() == 0) {
            return "SearchRecommendation cannot be applied!";
        }
        StringBuilder returnString = new StringBuilder("SearchRecommendation result: [");
        int i = 0;
        for (Video video : vList) {
            if (i != vList.size() - 1) {
                i++;
                returnString.append(video.getTitle()).append(", ");
            } else {
                returnString.append(video.getTitle());
            }
        }
        returnString.append("]");
        return returnString.toString();
    }

    /**
     * @param actors lista de actori
     * @return actorul cu cel mai mare rating
     */
    private Actor getMaxActor(final List<Actor> actors) {
        double max = 0;
        Actor returnedActor = new Actor();
        for (Actor actor : actors) {
            if (actor.averageRating() > max && actor.averageRating() != 0) {
                max = actor.averageRating();
                returnedActor = actor;

            } else if (actor.averageRating() == max) {
                if (actor.getName().compareTo(returnedActor.getName()) > 0) {
                    max = actor.averageRating();
                    returnedActor = actor;
                }
            }
        }
        return returnedActor;
    }

    /**
     * @param actors lista de actori
     * @return actorul cu cel mai mic rating
     */
    private Actor getMinActor(final List<Actor> actors) {
        double min = Constants.MAX_VERIFY;
        Actor returnedActor = new Actor();
        for (Actor actor : actors) {
            if (actor.averageRating() < min && actor.averageRating() != 0.0) {
                min = actor.averageRating();
                returnedActor = actor;
            } else if (actor.averageRating() == min) {
                if (actor.getName().compareTo(returnedActor.getName()) < 0) {
                    min = actor.averageRating();
                    returnedActor = actor;
                }
            }
        }
        return returnedActor;
    }

    /**
     * @param users lista de useri
     * @return userul cu cele mai multe ratings
     */
    private User getMaxUser(final List<User> users) {
        int max = 0;
        int done = 0;
        User returnedUser = new User();
        for (User user : users) {
            if (user.getRatings().size() > max) {
                max = user.getRatings().size();
                returnedUser = user;
                done = 1;
            } else if (user.getRatings().size() == max && done == 1) {
                if (user.getUsername().compareTo(returnedUser.getUsername()) > 0) {
                    returnedUser = user;
                }
            }
        }
        return returnedUser;
    }

    /**
     * @param users lista de useri
     * @return userul cu cele mai putine ratings
     */
    private User getMinUser(final List<User> users) {
        int min = Constants.MIN_COMPARE;
        int done = 0;
        User returnedUser = new User();
        for (User user : users) {
            if (user.getRatings().size() < min && user.getRatings().size() != 0) {
                min = user.getRatings().size();
                returnedUser = user;
                done = 1;
            } else if (user.getRatings().size() == min && done == 1) {
                if (user.getUsername().compareTo(returnedUser.getUsername()) < 0) {
                    returnedUser = user;
                }
            }
        }
        return returnedUser;
    }

    /**
     * @param movies lista de filme
     * @return filmul cel mai vizionat/nevizionat, in functie de sortType-ul actiunii
     */
    private Movie getMinMaxMovieViews(final List<Movie> movies) {
        int min = Constants.MIN_COMPARE;
        int max = 0;
        int done = 0;
        Movie returnedMovie = new Movie();
        if (this.sortType != null && this.sortType.equals("asc")) {
            for (Movie movie : movies) {
                if (movie.numberOfViews() < min && movie.numberOfViews() != 0) {
                    min = movie.numberOfViews();
                    returnedMovie = movie;
                    done = 1;
                } else if (movie.numberOfViews() == min && done == 1) {
                    if (movie.getTitle().compareTo(returnedMovie.getTitle()) < 0) {
                        min = movie.numberOfViews();
                        returnedMovie = movie;
                    }
                }
            }
        } else {
            for (Movie movie : movies) {
                if (movie.numberOfViews() > max && movie.numberOfViews() != 0) {
                    max = movie.numberOfViews();
                    returnedMovie = movie;
                    done = 1;
                } else if (movie.numberOfViews() == max && done == 1) {
                    if (movie.getTitle().compareTo(returnedMovie.getTitle()) > 0) {
                        max = movie.numberOfViews();
                        returnedMovie = movie;
                    }
                }
            }
        }
        return returnedMovie;
    }

    /**
     * @param serials lista de seriale
     * @return serialul cel mai vizionat/nevizionat, in functie de sortType-ul actiunii
     */
    private Serial getMinMaxSerialViews(final List<Serial> serials) {
        int min = Constants.MIN_COMPARE;
        int max = 0;
        int done = 0;
        Serial returnedSerial = new Serial();
        if (this.sortType != null && this.sortType.equals("asc")) {
            for (Serial serial : serials) {
                if (serial.numberOfViews() < min && serial.numberOfViews() != 0) {
                    min = serial.numberOfViews();
                    returnedSerial = serial;
                    done = 1;
                } else if (serial.numberOfViews() == min && done == 1) {
                    if (serial.getTitle().compareTo(returnedSerial.getTitle()) < 0) {
                        min = serial.numberOfViews();
                        returnedSerial = serial;
                    }
                }
            }
        } else {
            for (Serial serial : serials) {
                if (serial.numberOfViews() > max && serial.numberOfViews() != 0) {
                    max = serial.numberOfViews();
                    returnedSerial = serial;
                    done = 1;
                } else if (serial.numberOfViews() == max && done == 1) {
                    if (serial.getTitle().compareTo(returnedSerial.getTitle()) > 0) {
                        max = serial.numberOfViews();
                        returnedSerial = serial;
                    }
                }
            }
        }
        return returnedSerial;
    }

    /**
     * @param movies lista de filme
     * @return filmul cel mai lung/scurt, in functie de sortType-ul actiunii
     */
    private Movie getMinMaxMovieLength(final List<Movie> movies) {
        int min = Constants.MIN_COMPARE;
        int max = 0;
        int done = 0;
        Movie returnedMovie = new Movie();
        if (this.sortType.equals("asc")) {
            for (Movie movie : movies) {
                if (movie.getDuration() < min && movie.getDuration() != 0) {
                    min = movie.getDuration();
                    returnedMovie = movie;
                    done = 1;
                } else if (movie.getDuration() == min && done == 1) {
                    if (movie.getTitle().compareTo(returnedMovie.getTitle()) < 0) {
                        min = movie.getDuration();
                        returnedMovie = movie;
                    }
                }
            }
        } else {
            for (Movie movie : movies) {
                if (movie.getDuration() > max && movie.getDuration() != 0) {
                    max = movie.getDuration();
                    returnedMovie = movie;
                    done = 1;
                } else if (movie.getDuration() == max && done == 1) {
                    if (movie.getTitle().compareTo(returnedMovie.getTitle()) > 0) {
                        max = movie.getDuration();
                        returnedMovie = movie;
                    }
                }
            }
        }
        return returnedMovie;
    }

    /**
     * @param serials lista de seriale
     * @return serialul cel mai lung/scurt, in functie de sortType-ul actiunii
     */
    private Serial getMinMaxSerialLength(final List<Serial> serials) {
        int min = Constants.MIN_COMPARE;
        int max = 0;
        int done = 0;
        Serial returnedSerial = new Serial();
        if (this.sortType.equals("asc")) {
            for (Serial serial : serials) {
                if (serial.totalLength() < min && serial.totalLength() != 0) {
                    min = serial.totalLength();
                    returnedSerial = serial;
                    done = 1;
                } else if (serial.totalLength() == min && done == 1) {
                    if (serial.getTitle().compareTo(returnedSerial.getTitle()) < 0) {
                        min = serial.totalLength();
                        returnedSerial = serial;
                    }
                }
            }
        } else {
            for (Serial serial : serials) {
                if (serial.totalLength() > max && serial.totalLength() != 0) {
                    max = serial.totalLength();
                    returnedSerial = serial;
                    done = 1;
                } else if (serial.totalLength() == max && done == 1) {
                    if (serial.getTitle().compareTo(returnedSerial.getTitle()) > 0) {
                        max = serial.totalLength();
                        returnedSerial = serial;
                    }
                }
            }
        }
        return returnedSerial;
    }

    /**
     * @param movies lista de filme
     * @return filmul care se afla de cele mai multe/putine ori la favorite,
     * in functie de sortType-ul actiunii
     */
    private Movie getMinMaxMovieFavorite(final List<Movie> movies) {
        int min = Constants.MIN_COMPARE;
        int max = 0;
        int done = 0;
        Movie returnedMovie = new Movie();

        if (this.sortType == null || this.sortType.equals("desc")) {
            for (Movie movie : movies) {
                if (movie.timesFavorite() > max && movie.timesFavorite() != 0) {
                    max = movie.timesFavorite();
                    returnedMovie = movie;
                    done = 1;
                } else if (movie.timesFavorite() == max && done == 1) {
                    if (movie.getTitle().compareTo(returnedMovie.getTitle()) > 0) {
                        max = movie.timesFavorite();
                        returnedMovie = movie;
                    }
                }
            }
        } else {
            for (Movie movie : movies) {
                if (movie.timesFavorite() < min && movie.timesFavorite() != 0) {
                    min = movie.timesFavorite();
                    returnedMovie = movie;
                    done = 1;
                } else if (movie.timesFavorite() == min && done == 1) {
                    if (movie.getTitle().compareTo(returnedMovie.getTitle()) < 0) {
                        min = movie.timesFavorite();
                        returnedMovie = movie;
                    }
                }
            }
        }
        return returnedMovie;
    }

    /**
     * @param serials lista de seriale
     * @return serialul care se afla de cele mai multe/putine ori la favorite,
     * in functie de sortType-ul actiunii
     */
    private Serial getMinMaxSerialFavorite(final List<Serial> serials) {
        int min = Constants.MIN_COMPARE;
        int max = 0;
        int done = 0;
        Serial returnedSerial = new Serial();
        if (this.sortType == null || this.sortType.equals("desc")) {
            for (Serial serial : serials) {
                if (serial.timesFavorite() > max && serial.timesFavorite() != 0) {
                    max = serial.timesFavorite();
                    returnedSerial = serial;
                    done = 1;
                } else if (serial.timesFavorite() == max && done == 1) {
                    if (serial.getTitle().compareTo(returnedSerial.getTitle()) > 0) {
                        max = serial.timesFavorite();
                        returnedSerial = serial;
                    }
                }
            }
        } else {
            for (Serial serial : serials) {
                if (serial.timesFavorite() < min && serial.timesFavorite() != 0) {
                    min = serial.timesFavorite();
                    returnedSerial = serial;
                    done = 1;
                } else if (serial.timesFavorite() == min && done == 1) {
                    if (serial.getTitle().compareTo(returnedSerial.getTitle()) < 0) {
                        min = serial.timesFavorite();
                        returnedSerial = serial;
                    }
                }
            }
        }
        return returnedSerial;
    }

    /**
     * @param movies lista de filme
     * @return filmul care are cele mai bune/slabe ratinguri, in functie de sortType-ul actiunii
     */
    private Movie getMinMaxMovieRating(final List<Movie> movies) {
        double min = Constants.MIN_COMPARE;
        double max = 0;
        int done = 0;
        Movie returnedMovie = new Movie();
        if (this.sortType != null && this.sortType.equals("asc")) {
            for (Movie movie : movies) {
                if (movie.averageRating() < min && movie.averageRating() != 0) {
                    min = movie.averageRating();
                    returnedMovie = movie;
                    done = 1;
                } else if (movie.averageRating() == min && done == 1) {
                    if (movie.getTitle().compareTo(returnedMovie.getTitle()) < 0) {
                        min = movie.averageRating();
                        returnedMovie = movie;
                    }
                }
            }
        } else {
            for (Movie movie : movies) {
                if (movie.averageRating() > max && movie.averageRating() != 0) {
                    max = movie.averageRating();
                    returnedMovie = movie;
                    done = 1;
                } else if (movie.averageRating() == max && done == 1) {
                    if (movie.getTitle().compareTo(returnedMovie.getTitle()) > 0) {
                        max = movie.averageRating();
                        returnedMovie = movie;
                    }
                }
            }
        }
        return returnedMovie;
    }

    /**
     * @param serials lista de seriale
     * @return serialul care are cele mai bune/slabe ratinguri, in functie de sortType-ul actiunii
     */
    private Serial getMinMaxSerialRating(final List<Serial> serials) {
        double min = Constants.MIN_COMPARE;
        double max = 0;
        int done = 0;
        Serial returnedSerial = new Serial();
        if (this.sortType != null && this.sortType.equals("asc")) {
            for (Serial serial : serials) {
                if (serial.averageRating() < min && serial.averageRating() != 0) {
                    min = serial.averageRating();
                    returnedSerial = serial;
                    done = 1;
                } else if (serial.averageRating() == min && done == 1) {
                    if (serial.getTitle().compareTo(returnedSerial.getTitle()) < 0) {
                        min = serial.averageRating();
                        returnedSerial = serial;
                    }
                }
            }
        } else {
            for (Serial serial : serials) {
                if (serial.averageRating() > max && serial.averageRating() != 0) {
                    max = serial.averageRating();
                    returnedSerial = serial;
                    done = 1;
                } else if (serial.averageRating() == max && done == 1) {
                    if (serial.getTitle().compareTo(returnedSerial.getTitle()) > 0) {
                        max = serial.averageRating();
                        returnedSerial = serial;
                    }
                }
            }
        }
        return returnedSerial;
    }
}
