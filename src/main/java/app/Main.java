package app;

import setup.Parameters;

import java.io.IOException;
import java.math.BigDecimal;
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
            BigDecimal minSolutionValue, bestSolValue = BigDecimal.ZERO;
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
                minSolutionValue = BigDecimal.valueOf(Double.MAX_VALUE);
                antNo = 1;

                while (antNo <= numOfAnts) {
                    constructedSol.constructSolution();
                    if (constructedSol.getSolutionValue().compareTo(minSolutionValue) == -1) {             //constructedSol.getSolutionValue() < minSolutionValue
                        minSolutionValue = constructedSol.getSolutionValue();
                        bestSolValue = constructedSol.getSolutionValue();
                        iterationBestSolAlloc = constructedSol.getAssignment();
                        if (constructedSol.getSolutionValue().compareTo(solution.getSolutionValue()) == -1) {     //constructedSol.getSolutionValue() < solution.getSolutionValue()
                            solution = constructedSol;
                        }
                    }
                    Parameters.calculateHeuristicValues();
                    antNo++;
                }
                Parameters.updatePheromoneValues(iterationBestSolAlloc, bestSolValue);

            }
            System.out.println(solution.getSolutionValue());
            solution.printAssignments();
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

