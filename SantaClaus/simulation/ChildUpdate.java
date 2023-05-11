package simulation;

import java.util.ArrayList;

public final class ChildUpdate {
    private final int id;
    private final double niceScore;
    private final ArrayList<String> giftsPreferences;
    private final String elf;

    public ChildUpdate(final int id, final double niceScore,
                       final ArrayList<String> giftsPreferences, final String elf) {
        this.id = id;
        this.niceScore = niceScore;
        this.giftsPreferences = giftsPreferences;
        this.elf = elf;
    }

    public int getId() {
        return id;
    }

    public double getNiceScore() {
        return niceScore;
    }

    public ArrayList<String> getGiftsPreferences() {
        return giftsPreferences;
    }

    public String getElf() {
        return elf;
    }
}
