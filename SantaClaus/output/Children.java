package output;

import java.util.ArrayList;

public final class Children {
    private ArrayList<OutputChild> children;

    public Children(final ArrayList<OutputChild> children) {
        this.children = children;
    }

    public Children() {
        children = new ArrayList<>();
    }

    public ArrayList<OutputChild> getChildren() {
        return children;
    }

    public void setChildren(final ArrayList<OutputChild> children) {
        this.children = children;
    }
}
