package main;

import checker.Checker;
import common.Constants;
import input.Input;
import simulation.Simulation;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * Class used to run the code
 */
public final class Main {

    private Main() {
        ///constructor for checkstyle
    }

    /**
     * This method is used to call the checker which calculates the score
     *
     * @param args the arguments used to call the main method
     */
    public static void main(final String[] args) throws IOException, ParseException {
        for (int i = 1; i <= Constants.TESTS_NUMBER; i++) {
            String fileString = "tests/test" + i
                    + Constants.FILE_EXTENSION;
            Object object = new JSONParser().parse(new FileReader(fileString));
            JSONObject jsonObject = (JSONObject) object;
            Input.getInput().readInput(jsonObject);
            Simulation simulation = new Simulation();
            simulation.startSimulation(i);

        }

        Checker.calculateScore();
    }
}
