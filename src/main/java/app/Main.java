package app;

import setup.Parameters;

import java.io.IOException;

/**
 * Created by Karol on 24/04/2017.
 */
public class Main {

    public static void main(String[] args) {
        try {
            Parameters parameters = new Parameters();
            parameters.readParameters();
            parameters.calculateHeuristicValues();
            parameters.printHeuristic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

