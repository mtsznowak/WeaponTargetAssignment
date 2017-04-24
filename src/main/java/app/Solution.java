package app;

import setup.Parameters;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<Integer> allocations = new ArrayList<>();
    private double solutionValue = Double.MAX_VALUE;

    public void constructSolution() {
        int i;
        for(int k = 0; k < Parameters.getNumOfWeapons(); k++){
            i = findTargetIndexForWeapon(k);
            allocations.add(i);
            updatePheromoneValuesLocally(k);
            Parameters.setTargetValue(i, Parameters.getTargetValue(i) * (1 - Parameters.getKillProbability(i, k)));
            Parameters.calculateHeuristicValues();
            k++;
        }

        solutionValue = calculateSolution(allocations);
    }

    private double calculateSolution(List<Integer> allocations) {
        double returnValue = 0.0;
        for (int i = 0; i < Parameters.getNumOfTargets(); i++) {
            for (int k = 0; k < Parameters.getNumOfWeapons(); k++) {
                if (allocations.get(k) == i) {
                    Parameters.setTargetValue(i, Parameters.getTargetValue(i) * (1 - Parameters.getKillProbability(i, k)));
                }
            }
            returnValue += Parameters.getTargetValue(i);
        }
        return returnValue;
    }

    private int findTargetIndexForWeapon(int k) {
        int targetIndex = 0;
        double randomValue = Math.random();
        double q = randomValue;

        if (q < Parameters.q0) {
            targetIndex = argMax(k);
        } else {
            double total = 0;
            for (int i = 0; i < Parameters.getNumOfTargets(); i++) {
                total = total + Parameters.getHeuristicValues().get(i).get(k) * Parameters.getPheromoneValues().get(i).get(k);
            }
            q = randomValue * total;
            total = 0;
            for (int i = 0; i < Parameters.getNumOfTargets(); i++) {
                total = total + Parameters.getHeuristicValues().get(i).get(k) * Parameters.getPheromoneValues().get(i).get(k);
                if (q <= total) {
                    targetIndex = i;
                    break;
                }
            }
        }
        return targetIndex;
    }

    private void updatePheromoneValuesLocally(int k) {
        for (int i = 0; i < Parameters.getNumOfTargets(); i++) {
            double tmp = Parameters.getPheromoneValues().get(i).get(k) * Parameters.EVAPORATION_RATE;
            double val = tmp + (Parameters.EVAPORATION_RATE * 1 / Parameters.getNumOfAnts() * solutionValue);
            Parameters.getPheromoneValues().get(i).set(k, val);
        }
    }

    private int argMax(int k) {
        int targetIndex = 0;
        double minValue = Double.MIN_VALUE;
        double maxValue = minValue;

        for (int i = 0; i < Parameters.getNumOfTargets(); i++) {
            double value = Parameters.getPheromoneValues().get(i).get(k) * Parameters.getHeuristicValues().get(i).get(k);
            if (value > maxValue) {
                targetIndex = i;
                maxValue = value;
            }
        }

        return targetIndex;
    }

    public double getSolutionValue() {
        return solutionValue;
    }

    public List<Integer> getAllocations() {
        return allocations;
    }
}
