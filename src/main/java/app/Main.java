package app;

import org.jfree.ui.RefineryUtilities;
import setup.Parameters;
import ui.Chart;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            final Chart demo = new Chart("Line Chart");
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
            int iter = 0;

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
                demo.addResult(iter++, solution.getSolutionValue());
            }
            System.out.println(solution.getSolutionValue());


            demo.pack();
            RefineryUtilities.centerFrameOnScreen(demo);
            demo.setVisible(true);


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

