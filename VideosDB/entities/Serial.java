package entities;

import entertainment.Season;
import fileio.SerialInputData;

import java.util.ArrayList;

public final class Serial extends Video {

    private int numberOfSeasons;
    private ArrayList<Season> seasons;

    public Serial(final SerialInputData serial) {
        super(serial);
        this.numberOfSeasons = serial.getNumberSeason();
        this.seasons = serial.getSeasons();
    }

    public Serial() {
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(final ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    /**
     * Calculeaza media serialului ca medie a tururor ratingurilor userilor pt fiecare sezon
     *
     * @return media ratingurilor serialului
     */
    public double averageRating() {
        double s = 0;
        for (Season season : getSeasons()) {
            s += season.averageRating();
        }
        if (s == 0) {
            return 0;
        }
        return s / numberOfSeasons;
    }

    /**
     * Calculeaza de cate ori a fost vazut serialul
     *
     * @return numarul total de viewuri ale serialului
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
     * Calculeaza lungimea serialului ca suma a lungimilor sezoanelor
     *
     * @return lungimea totala a serialului
     */
    public int totalLength() {
        int s = 0;
        for (Season season : this.seasons) {
            s += season.getDuration();
        }
        return s;
    }

    /**
     * Calculeaza de cate ori a primit "Favorite" serialul
     *
     * @return numarul total de liste de favorite ale userilor in care serialul se afla
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
