package app;

import setup.Parameters;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            ClassLoader classLoader = new Main().getClass().getClassLoader();
            Parameters.readParameters(classLoader);
            Parameters.calculateHeuristicValues();
            Parameters.printHeuristic();

            List<Integer> iterationBestSolAlloc = null;
            double minSolutionValue, bestSolValue = 0;
            int numOfTargets = Parameters.getNumOfTargets();
            int numOfWeapons = Parameters.getNumOfWeapons();
            int numOfAnts;
            int antNo;

            if (numOfTargets > numOfWeapons) {
                numOfAnts = numOfTargets;
            } else {
                numOfAnts = numOfWeapons;
            }

            Parameters.numOfAnts = numOfAnts;

            Solution solution = new Solution();
            Solution constructedSol = new Solution();

            Parameters.calculatePheromoneValues(solution.getSolutionValue());
            Parameters.printPheromons();

            while (LocalTime.now().isBefore(Parameters.END_TIME)) {
                minSolutionValue = Double.MAX_VALUE;
                antNo = 1;

                while (antNo <= numOfAnts) {
                    constructedSol.constructSolution();
                    if (constructedSol.getSolutionValue() < minSolutionValue) {
                        bestSolValue = constructedSol.getSolutionValue();
                        iterationBestSolAlloc = constructedSol.getAssignment();
                        if (constructedSol.getSolutionValue() < solution.getSolutionValue()) {
                            solution = constructedSol;
                        }
                    }
                    Parameters.calculateHeuristicValues();
                    antNo++;
                }
                Parameters.updatePheromoneValues(iterationBestSolAlloc, bestSolValue);

            }
            System.out.println(solution.getSolutionValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void log(String x) {
        System.out.println(x);
    }

    static void log(int x) {
        System.out.println(x);
    }
}

