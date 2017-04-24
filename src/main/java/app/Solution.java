package app;

import setup.Parameters;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;

public class Solution {
    private List<Integer> allocations = new ArrayList<>();
    private double solutionValue = Double.MAX_VALUE;
    private Parameters parameters;
    private int numOfAnts;
    private static double EVAPORATION_RATE = 0.1;
    private static double q0 = 0.5;

    Solution(Parameters parameters, int numOfAnts) {
        this.parameters = parameters;
        this.numOfAnts = numOfAnts;

    }

    public void constructSolution() {
        int k = 1;
        int i;

        while (k <= parameters.getNumOfWeapons()) {
            i = findTargetIndexForWeapon(k);
            allocations.add(i);
            updatePheromoneValuesLocally(k);
            parameters.setTargetValue(i, parameters.getTargetValue(i) * (1 - parameters.getKillProbability(i, k)));
            parameters.calculateHeuristicValues();
            k++;
        }

        solutionValue = calculateSolution(allocations);
    }

    private double calculateSolution(List<Integer> allocations) {
        double returnValue = 0.0;
        for (int i = 0; i < parameters.getNumOfTargets(); i++) {
            for (int k = 0; k < parameters.getNumOfWeapons(); k++) {
                if(allocations.get(k) == i) {
                    parameters.setTargetValue(i, parameters.getTargetValue(i) * (1 - parameters.getKillProbability(i, k)));
                }
            }
            returnValue += parameters.getTargetValue(i);
        }
        return returnValue;
    }

    private int findTargetIndexForWeapon(int k) {
        int targetIndex = 0;
        double randomValue = Math.random();
        double q = randomValue;

        if(q < q0){
            targetIndex = argMax(k);
        }else{
            double total = 0;
            for(int i = 0; i < parameters.getNumOfTargets(); i++){
                total = total + parameters.getHeuristicValues().get(i).get(k) * parameters.getPheromoneValues().get(i).get(k);
            }
            q = randomValue * total;
            total = 0;
            for(int i = 0; i < parameters.getNumOfTargets(); i++){
                total = total + parameters.getHeuristicValues().get(i).get(k) * parameters.getPheromoneValues().get(i).get(k);
                if(q <= total){
                    targetIndex = i;
                    break;
                }
            }
        }
        return targetIndex;
    }

    private void updatePheromoneValuesLocally(int k) {
        for (int i = 0; i < parameters.getNumOfTargets(); i++) {
            double tmp = parameters.getPheromoneValues().get(i).get(k) * EVAPORATION_RATE;
            double val = tmp + (EVAPORATION_RATE * 1 / numOfAnts * solutionValue);
            parameters.getPheromoneValues().get(i).set(k, val);
        }
    }

    private int argMax(int k) {
        int targetIndex = 0;
        double minValue = Double.MIN_VALUE;
        double maxValue = minValue;

        for (int i = 0; i < parameters.getNumOfTargets(); i++) {
            double value = parameters.getPheromoneValues().get(i).get(k) * parameters.getHeuristicValues().get(i).get(k);
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
