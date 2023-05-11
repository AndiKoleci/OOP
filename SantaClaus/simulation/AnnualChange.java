package simulation;

import java.util.ArrayList;

public final class AnnualChange {
    private final double newSantaBudget;
    private final ArrayList<Gift> newGifts;
    private final ArrayList<Child> newChildren;
    private final ArrayList<ChildUpdate> childrenUpdates;
    private final String strategy;

    public AnnualChange(final double newSantaBudget, final ArrayList<Gift> newGifts,
                        final ArrayList<Child> newChildren,
                        final ArrayList<ChildUpdate> childrenUpdates, final String strategy) {
        this.newSantaBudget = newSantaBudget;
        this.newGifts = newGifts;
        this.newChildren = newChildren;
        this.childrenUpdates = childrenUpdates;
        this.strategy = strategy;
    }

    public double getNewSantaBudget() {
        return newSantaBudget;
    }

    public ArrayList<Gift> getNewGifts() {
        return newGifts;
    }

    public ArrayList<Child> getNewChildren() {
        return newChildren;
    }

    public ArrayList<ChildUpdate> getChildrenUpdates() {
        return childrenUpdates;
    }

    public String getStrategy() {
        return strategy;
    }
}
