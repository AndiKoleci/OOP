package output;

import simulation.Child;
import simulation.Gift;

import java.util.ArrayList;

public final class OutputChild {

    private final int id;
    private final String lastName;
    private final String firstName;
    private final String city;
    private final int age;
    private final ArrayList<String> giftsPreferences;
    private final double averageScore;
    private ArrayList<Double> niceScoreHistory = new ArrayList<>();
    private Double assignedBudget = 0.0;
    private ArrayList<OutputGift> receivedGifts = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getCity() {
        return city;
    }

    public int getAge() {
        return age;
    }

    public ArrayList<String> getGiftsPreferences() {
        return giftsPreferences;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public ArrayList<Double> getNiceScoreHistory() {
        return niceScoreHistory;
    }

    public Double getAssignedBudget() {
        return assignedBudget;
    }

    public ArrayList<OutputGift> getReceivedGifts() {
        return receivedGifts;
    }

    public OutputChild(final int id, final String lastName, final String firstName,
                       final String city, final int age,
                       final ArrayList<String> giftsPreferences, final double averageScore) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.city = city;
        this.age = age;
        this.giftsPreferences = giftsPreferences;
        this.averageScore = averageScore;
    }

    public OutputChild(final Child child) {
        this.id = child.getId();
        this.lastName = child.getLastName();
        this.firstName = child.getFirstName();
        this.age = child.getAge();
        this.city = child.getCity();
        this.averageScore = child.getAverageScore();
        this.giftsPreferences = child.getGiftsPreferences();
        this.niceScoreHistory = new ArrayList<>();
        this.niceScoreHistory.addAll(child.getNiceScoreHistory());
        this.assignedBudget = child.getAssignedBudget();
        ArrayList<OutputGift> outputGifts = new ArrayList<>();
        for (Gift gift : child.getReceivedGifts()) {
            OutputGift outgift = new OutputGift(gift);
            outputGifts.add(outgift);
        }
        this.receivedGifts = outputGifts;
    }
}
