package simulation;

import common.Constants;

import java.util.ArrayList;
import java.util.Comparator;

public final class Santa {
    private double santaBudget;
    private ArrayList<Child> children;
    private ArrayList<Gift> santaGiftsList;

    public Santa() {

    }

    public Santa(final double santaBudget, final ArrayList<Child> children,
                 final ArrayList<Gift> santaGiftsList) {
        this.santaBudget = santaBudget;
        this.children = children;
        this.santaGiftsList = santaGiftsList;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public ArrayList<Gift> getSantaGiftsList() {
        return santaGiftsList;
    }

    public void setSantaBudget(final double santaBudget) {
        this.santaBudget = santaBudget;
    }

    public void setChildren(final ArrayList<Child> children) {
        this.children = children;
    }

    public void setSantaGiftsList(final ArrayList<Gift> santaGiftsList) {
        this.santaGiftsList = santaGiftsList;
    }

    /**
     * Calculates the budget unit, which will be used to
     * determine the budget for each children.
     *
     * @return value of budget unit, calculated as
     * santaBudget / sum of average scores from all children
     */
    public double getBudgetUnit() {
        double sum = 0;
        for (Child child : children) {
            sum += child.getAverageScore();
        }
        return santaBudget / sum;
    }

    /**
     * Goes through every child's gift preferences list in order
     * and, if he has the money, Santa gets him a gift from that category
     * Takes all gifts from santaGiftList who have the required category
     * and adds them to a list, which will be sorted by price.
     * The first element will be the cheapes gift, which santa will buy
     */
    public void giveGifts() {
        for (Child child : children) {
            double remaining = child.getAssignedBudget();
            ArrayList<Gift> receivedGifts = new ArrayList<>();
            for (String giftCategory : child.getGiftsPreferences()) {
                if (child.getGiftsPreferences().contains(giftCategory)) {
                    ArrayList<Gift> sortedGifts = new ArrayList<>();
                    for (Gift gift : santaGiftsList) {
                        //TODO here i check if the gift is givable
                        if (gift.getCategory().equals(giftCategory) && gift.getQuantity() != 0) {
                            sortedGifts.add(gift);
                        }
                    }
                    sortedGifts.sort(Comparator.comparingDouble(Gift::getPrice));
                    if (sortedGifts.size() != 0) {
                        //TODO here i subtract 1 from quantity
                        if (sortedGifts.get(0).getPrice() <= remaining) {
                            receivedGifts.add(sortedGifts.get(0));
                            remaining -= sortedGifts.get(0).getPrice();
                            sortedGifts.get(0).setQuantity(sortedGifts.get(0).getQuantity() - 1);
                        }
                    }
                }
            }
            child.setReceivedGifts(receivedGifts);
        }
    }

    /**
     * Goes through all the children in the list and checks what elf they have that year.
     * If their elf is Pink or Black, it applies the changes to their budget.
     */
    public void applyBlackPink() {
        double budget;
        for (Child child : getChildren()) {
            if (child.getElf().equals("black")) {
                budget = child.getAssignedBudget();
                budget = budget - budget * Constants.FIRST_NR_BONUS / Constants.SECOND_NR_BONUS;
                child.setAssignedBudget(budget);
            } else if (child.getElf().equals("pink")) {
                budget = child.getAssignedBudget();
                budget = budget + budget * Constants.FIRST_NR_BONUS / Constants.SECOND_NR_BONUS;
                child.setAssignedBudget(budget);
            }
        }
    }

    /**
     * Goes through all the children in the list and checks what elf they have that year.
     * If their elf is Yellow, it checks if the kid got a gift and, if he didnt, the elf will give
     * him a gift if all the requirements are met.
     */
    public void applyYellow() {
        for (Child child : getChildren()) {
            if (child.getElf().equals("yellow") && child.getReceivedGifts().size() == 0) {
                ArrayList<Gift> sortedGifts = new ArrayList<>();
                for (Gift gift : santaGiftsList) {
                    if (gift.getCategory().equals(child.getGiftsPreferences().get(0))) {
                        sortedGifts.add(gift);
                    }
                }
                sortedGifts.sort(Comparator.comparingDouble(Gift::getPrice));
                if (sortedGifts.get(0).getQuantity() != 0) {
                    sortedGifts.get(0).setQuantity(sortedGifts.get(0).getQuantity() - 1);
                    child.getReceivedGifts().add(sortedGifts.get(0));
                }
            }
        }
    }
}
