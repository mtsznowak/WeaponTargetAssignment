package app;

import setup.Parameters;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by Karol on 24/04/2017.
 */
public class Main {
    private static int ALLOWED_TIME = 2; // TODO set time
    private static LocalTime START_TIME = LocalTime.now();
    private static LocalTime END_TIME = START_TIME.plusSeconds(ALLOWED_TIME);

    public static void main(String[] args) {
        try {
            Parameters parameters = new Parameters();
            parameters.readParameters();
            parameters.calculateHeuristicValues();
            parameters.printHeuristic();

            Solution solution = new Solution(parameters);
            Solution constructedSol = new Solution(parameters);

            List<Integer> iterationBestSolAlloc = null;
            double minSolutionValue, bestSolValue = 0;
            int numOfTargets = parameters.getNumOfTargets();
            int numOfWeapons = parameters.getNumOfWeapons();
            int numOfAnts;
            int antNo;

            if (numOfTargets > numOfWeapons) {
                numOfAnts = numOfTargets;
            } else {
                numOfAnts = numOfWeapons;
            }

            parameters.calculatePheromoneValues(numOfAnts, solution.getValue());

            while (LocalTime.now().isBefore(END_TIME)) {
                minSolutionValue = Double.MAX_VALUE;
                antNo = 1;

                while (antNo <= numOfAnts) {
                    constructedSol.constructSolution();
                    if(constructedSol.getValue() < minSolutionValue){
                        bestSolValue = constructedSol.getValue();
                        iterationBestSolAlloc = constructedSol.getAllocations();
                        if(constructedSol.getValue() < solution.getValue()){
                            solution = constructedSol;
                        }
                    }
                    parameters.calculateHeuristicValues();
                    antNo++;
                }
                updatePheromoneValues(iterationBestSolAlloc, bestSolValue);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updatePheromoneValues(List<Integer> iterationBestSolAlloc, double bestSolValue){
        // TODO: probably in parameters
    }

    static void log(String x){
        System.out.println(x);
    }
    static void log(int x){
        System.out.println(x);
    }
}

