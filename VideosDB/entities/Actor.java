package entities;

import actor.ActorsAwards;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Map;

public final class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public Actor(final ActorInputData data) {
        this.name = data.getName();
        this.awards = data.getAwards();
        this.filmography = data.getFilmography();
        this.careerDescription = data.getCareerDescription();
    }

    public Actor() {
    }

    /**
     * Calculeaza raiting-ul mediu pt filmele in care actorul a jucat
     *
     * @return media ratingurilor filmelor actorului
     */
    public double averageRating() {
        double s = 0;
        double i = 0;

        for (Movie movie : Database.getDatabase().getMovies()) {
            if (movie.getCast().contains(this.name) && movie.averageRating() != 0) {
                s += movie.averageRating();
                i++;
            }
        }
        for (Serial serial : Database.getDatabase().getSerials()) {
            if (serial.getCast().contains(this.name) && serial.averageRating() != 0) {
                s += serial.averageRating();
                i++;
            }
        }

        if (s == 0) {
            return 0;
        }
        return s / i;
    }

    /**
     * Calculeaza cate premii are un actor
     *
     * @return nr de premii ale actorului
     */
    public int timesAwards() {
        int count = 0;
        for (int i : this.getAwards().values()) {
            count += i;
        }
        return count;
    }
}
