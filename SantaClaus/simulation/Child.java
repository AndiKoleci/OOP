package simulation;

import common.Constants;

import java.util.ArrayList;

public final class Child {

    private final int id;
    private String lastName;
    private String firstName;
    private String city;
    private int age;
    private ArrayList<String> giftsPreferences;
    private double averageScore;
    private ArrayList<Double> niceScoreHistory = new ArrayList<>();
    private Double assignedBudget = 0.0;
    private ArrayList<Gift> receivedGifts = new ArrayList<>();
    private double niceScoreBonus;
    private String elf;

    public String getElf() {
        return elf;
    }

    public void setElf(final String elf) {
        this.elf = elf;
    }

    public double getNiceScoreBonus() {
        return niceScoreBonus;
    }

    public void setNiceScoreBonus(final double niceScoreBonus) {
        this.niceScoreBonus = niceScoreBonus;
    }

    public Child(final int id) {

        this.id = id;
    }

    public Child(final int id, final String lastName, final String firstName,
                 final int age, final String city,
                 final double niceScore, final ArrayList<String> giftsPreference,
                 final double niceScoreBonus, final String elf) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
        this.city = city;
        this.averageScore = niceScore;
        this.giftsPreferences = giftsPreference;
        this.niceScoreBonus = niceScoreBonus;
        this.elf = elf;
        niceScoreHistory.add(niceScore);
    }

    public Child(final Child child) {
        this.id = child.id;
        this.lastName = child.lastName;
        this.firstName = child.firstName;
        this.age = child.age;
        this.city = child.city;
        this.averageScore = child.averageScore;
        this.giftsPreferences = child.giftsPreferences;
        this.niceScoreHistory = new ArrayList<>();
        this.niceScoreHistory.addAll(child.getNiceScoreHistory());
        this.assignedBudget = child.assignedBudget;
        this.receivedGifts = child.receivedGifts;
        this.elf = child.elf;
        this.niceScoreBonus = child.niceScoreBonus;
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public ArrayList<String> getGiftsPreferences() {
        return giftsPreferences;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public void setCity(final String city) {
        this.city = city;
    }


    public void setGiftsPreferences(final ArrayList<String> giftsPreferences) {
        this.giftsPreferences = giftsPreferences;
    }

    public void setAverageScore(final double averageScore) {
        this.averageScore = averageScore;
    }

    public ArrayList<Double> getNiceScoreHistory() {
        return niceScoreHistory;
    }

    public void setNiceScoreHistory(final ArrayList<Double> niceScoreHistory) {
        this.niceScoreHistory = niceScoreHistory;
    }

    public Double getAssignedBudget() {
        return assignedBudget;
    }

    public void setAssignedBudget(final Double assignedBudget) {
        this.assignedBudget = assignedBudget;
    }

    public ArrayList<Gift> getReceivedGifts() {
        return receivedGifts;
    }

    public void setReceivedGifts(final ArrayList<Gift> receivedGifts) {
        this.receivedGifts = receivedGifts;
    }

    /**
     * Calculates the averageScore of the child, depending
     * on his age and nice score history.
     *
     * @return child's average score for that year
     */
    public double getAverageScore() {
        double average = 0;
        if (age < Constants.BABY_MAX_AGE) {
            average = Constants.PERFECT_NICE_SCORE;
        } else if (age < Constants.KID_MAX_AGE) {
            double sum = 0;
            double i = 0;
            for (double score : niceScoreHistory) {
                sum += score;
                i++;
            }
            average = sum / i;
        } else if (age <= Constants.TEEN_MAX_AGE) {
            double sum = 0;
            double isum = 0;
            double i = 1;
            for (double score : niceScoreHistory) {
                sum += score * i;
                isum += i;
                i++;
            }
            average = sum / isum;
        } else {
            average = 0;
        }
        averageScore += averageScore * niceScoreBonus / Constants.SECOND_NR_BONUS;
        average += average * niceScoreBonus / Constants.SECOND_NR_BONUS;
        if (averageScore > Constants.PERFECT_NICE_SCORE) {
            averageScore = Constants.PERFECT_NICE_SCORE;
        }
        if (average > Constants.PERFECT_NICE_SCORE) {
            average = Constants.PERFECT_NICE_SCORE;
        }
        return average;
    }

    /**
     * increments the child's age, the goal is
     * to make more sense for the programmer
     */
    public void getOlder() {
        this.age++;
    }

    /**
     * Updates the niceScore nd the giftPreferences for the child
     *
     * @param update the update for the specific child
     */
    public void updateChild(final ChildUpdate update) {
        if (update.getNiceScore() != -1) {
            niceScoreHistory.add(update.getNiceScore());
        }
        ArrayList<String> newGiftsPreferences = new ArrayList<>();
        for (String string : update.getGiftsPreferences()) {
            if (!newGiftsPreferences.contains(string)) {
                newGiftsPreferences.add(string);
            }
        }
        for (String string : this.giftsPreferences) {
            if (!newGiftsPreferences.contains(string)) {
                newGiftsPreferences.add(string);
            }
        }
        this.giftsPreferences = newGiftsPreferences;
        this.elf = update.getElf();
    }
}
