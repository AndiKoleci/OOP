package entities;

import common.Constants;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class Database {

    private static final Database DATABASE = new Database();

    private final List<Actor> actors = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Action> actions = new ArrayList<>();
    private final List<Movie> movies = new ArrayList<>();
    private final List<Serial> serials = new ArrayList<>();
    private final List<Video> videos = new ArrayList<>();
    private JSONArray jsonArray;

    private Database() {
    }

    public static Database getDatabase() {
        return DATABASE;
    }

    /**
     * Umple campurile din Database cu ce se afla in input
     *
     * @param input Input-ul dat in main
     */
    public void generate(final Input input) {

        this.actors.clear();
        this.serials.clear();
        this.movies.clear();
        this.actions.clear();
        this.users.clear();
        this.videos.clear();

        for (ActorInputData actorData : input.getActors()) {
            this.actors.add(new Actor(actorData));
        }
        for (UserInputData userData : input.getUsers()) {
            this.users.add(new User(userData));
        }
        for (ActionInputData actionData : input.getCommands()) {
            this.actions.add(new Action(actionData));
        }
        for (MovieInputData movieData : input.getMovies()) {
            this.movies.add(new Movie(movieData));
            this.videos.add(new Movie(movieData));
        }
        for (SerialInputData serialData : input.getSerials()) {
            this.serials.add(new Serial(serialData));
            this.videos.add(new Serial(serialData));
        }

        this.jsonArray = new JSONArray();
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Action> getCommands() {
        return actions;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public List<Serial> getSerials() {
        return serials;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public List<Video> getVideos() {
        return videos;
    }


    /**
     * Cauta in Database user-ul cu un anumit username
     *
     * @param name username-ul cautat
     * @return userul cu numele respectiv
     */
    public User findUser(final String name) {
        for (User user : this.users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * adauga element in JSON-ul care va fi returnat
     *
     * @param id      id-ul actiunii
     * @param message mesajul actiunii
     */
    @SuppressWarnings("unchecked")
    public void addElement(final int id, final String message) {
        JSONObject object = new JSONObject();
        object.put(Constants.ID_STRING, id);
        object.put(Constants.MESSAGE, message);
        jsonArray.add(object);
    }
}
