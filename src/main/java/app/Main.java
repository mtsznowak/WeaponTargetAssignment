package app;

import org.jfree.ui.RefineryUtilities;
import setup.Parameters;
import ui.Chart;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            final Chart demo = new Chart("Line Chart");
            ClassLoader classLoader = new Main().getClass().getClassLoader();
            Parameters.readParameters(classLoader);
            Parameters.readAlgorithmParameters(classLoader);
            Parameters.calculateHeuristicValues();
            Parameters.printHeuristic();

            List<Integer> iterationBestSolAlloc = null;
            BigDecimal minSolutionValue, bestSolValue = BigDecimal.ZERO;
            int numOfTargets = Parameters.getNumOfTargets();
            int numOfWeapons = Parameters.getNumOfWeapons();
            int antNo;
            int iter = 0;

            Solution solution = new Solution();

            Parameters.calculatePheromoneValues(solution.getSolutionValue());       // solution value = MAX
            Parameters.printPheromones();

            while (LocalTime.now().isBefore(Parameters.END_TIME)) {
                minSolutionValue = BigDecimal.valueOf(Double.MAX_VALUE);
                antNo = 1;

                while (antNo <= Parameters.NUMBER_OF_ANTS) {
                    Solution constructedSol = new Solution();
                    constructedSol.constructSolution();
                    if (constructedSol.getSolutionValue().compareTo(minSolutionValue) == -1) {             //constructedSol.getSolutionValue() < minSolutionValue
                        minSolutionValue = constructedSol.getSolutionValue();
                        bestSolValue = constructedSol.getSolutionValue();
                        iterationBestSolAlloc = constructedSol.getAssignment();
                        if (constructedSol.getSolutionValue().compareTo(solution.getSolutionValue()) == -1) {     //constructedSol.getSolutionValue() < solution.getSolutionValue()
                            solution.setAssignment(constructedSol.getAssignment());
                            solution.setSolutionValue(constructedSol.getSolutionValue());

                        }
                    }
                    Parameters.calculateHeuristicValues();
                    antNo++;
                    Parameters.resetTargetValues();
                }
                Parameters.updatePheromoneValues(iterationBestSolAlloc, bestSolValue);
                demo.addResult(iter++, solution.getSolutionValue().doubleValue());
            }
            System.out.println("SOLUTION VALUE: " + solution.getSolutionValue().setScale(4, BigDecimal.ROUND_HALF_UP));
            System.out.println();

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

