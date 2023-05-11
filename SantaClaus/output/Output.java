package output;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import common.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class Output {
    private ArrayList<ArrayList<OutputChild>> annualChildren;

    public Output() {
        this.annualChildren = new ArrayList<>();
    }

    public ArrayList<ArrayList<OutputChild>> getAnnualChildren() {
        return annualChildren;
    }

    public void setAnnualChildren(final ArrayList<ArrayList<OutputChild>> annualChildren) {
        this.annualChildren = annualChildren;
    }

    /**
     * Creates the JSON file with the Output answer
     *
     * @param i - the number of the current test to be generated
     */
    public void createJSON(final int i) throws IOException {
        String fileString = Constants.OUTPUT_PATH + i
                + Constants.FILE_EXTENSION;

        AnnualChildren annualChildrenList = new AnnualChildren();
        for (ArrayList<OutputChild> array : annualChildren) {
            Children children = new Children();
            for (OutputChild child : array) {
                children.getChildren().add(child);
            }
            annualChildrenList.getAnnualChildren().add(children);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        File outputFile = new File(fileString);
        outputFile.getParentFile().mkdir();
        writer.writeValue(outputFile, annualChildrenList);
    }
}
