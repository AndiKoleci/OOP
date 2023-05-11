package simulation;

import common.Constants;
import input.Input;
import output.Output;
import output.OutputChild;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public final class Simulation {

    private int roundNr = 0;
    private final Santa santa = new Santa(Input.getInput().getSantaBudget(),
            Input.getInput().getInitialData().getChildren(),
            Input.getInput().getInitialData().getSantaGiftsList());


    public Simulation() {

    }

    /**
     * increments the number of the round, the goal is
     * to make more sense for the programmer
     */
    public void increaseRound() {
        roundNr++;
    }

    /**
     * Starts the whole Christmas Simulation
     *
     * @param i - the number of the current test, used to pass it to
     *          the output generator
     */
    public void startSimulation(final int i) throws IOException {

        Output output = new Output();
        while (roundNr <= Input.getInput().getNumberOfYears()) {

            ArrayList<Child> initialChildren = new ArrayList<>();
            ArrayList<Child> newSantaChildren = new ArrayList<>();
            for (Child child : santa.getChildren()) {
                if (child.getAge() <= Constants.TEEN_MAX_AGE) {
                    newSantaChildren.add(child);
                }
            }
            santa.setChildren(newSantaChildren);

            calculateAssignedBudgets();
            santa.applyBlackPink();
            santa.giveGifts();
            santa.applyYellow();

            for (Child child : santa.getChildren()) {
                Child copyChild = new Child(child);
                initialChildren.add(copyChild);
            }
            ArrayList<OutputChild> outputChildren = new ArrayList<>();
            for (Child child : initialChildren) {
                OutputChild outchild = new OutputChild(child);
                outputChildren.add(outchild);
            }
            output.getAnnualChildren().add(outputChildren);

            for (Child child : santa.getChildren()) {
                child.getOlder();
            }

            if (roundNr < Input.getInput().getNumberOfYears()) {
                //AnnualChanges here
                AnnualChange annualChange = Input.getInput().getAnnualChanges().get(roundNr);
                santa.setSantaBudget(annualChange.getNewSantaBudget());
                for (Child child : annualChange.getNewChildren()) {
                    santa.getChildren().add(child);
                }
                santa.getChildren().sort(Comparator.comparingInt(Child::getId));
                for (Gift gift : annualChange.getNewGifts()) {
                    santa.getSantaGiftsList().add(gift);
                }
                for (ChildUpdate update : annualChange.getChildrenUpdates()) {
                    for (Child child : santa.getChildren()) {
                        if (child.getId() == update.getId()) {
                            child.updateChild(update);
                        }
                    }
                }
            }
            increaseRound();

        }
        output.createJSON(i);
    }

    /**
     * Calculates the assigned budget for each child,
     * in accordance to the Budget Unit and the child's average score
     */
    public void calculateAssignedBudgets() {
        for (Child child : santa.getChildren()) {
            child.setAssignedBudget(santa.getBudgetUnit() * child.getAverageScore());
        }
    }
}
