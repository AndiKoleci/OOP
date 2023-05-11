package output;

import java.util.ArrayList;

public final class AnnualChildren {
    private ArrayList<Children> annualChildren;

    public AnnualChildren() {
        annualChildren = new ArrayList<>();
    }

    public AnnualChildren(final ArrayList<Children> children) {
        this.annualChildren = children;
    }

    public ArrayList<Children> getAnnualChildren() {
        return annualChildren;
    }

    public void setAnnualChildren(final ArrayList<Children> children) {
        this.annualChildren = children;
    }
}
