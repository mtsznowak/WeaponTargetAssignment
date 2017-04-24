package app;

import setup.Parameters;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<Integer> allocations = new ArrayList<>();
    private double value = Double.MAX_VALUE;
    private Parameters parameters;

    Solution(Parameters parameters){
        this.parameters = parameters;
    }

    public void constructSolution(){
        int k = 1;
        int i;

        while(k <= parameters.getNumOfWeapons()){
            i = findTargetIndexForWeapon(k);
            allocations.add(i);
            updatePheromoneValuesLocally(k);
            parameters.setTargetValue(i, parameters.getTargetValue(i) * (1 - parameters.getKillProbability(i, k)));
            parameters.calculateHeuristicValues();
            k++;
        }

        value = calculateSolutionValue(allocations);
    }

    private double calculateSolutionValue(List<Integer> allocations) {
        // TODO
        return 0.0;
    }

    private int findTargetIndexForWeapon(int k){
        // TODO
        return 0;
    }

    private int updatePheromoneValuesLocally(int k){
        // TODO
        return 0;
    }

    public double getValue() {
        return value;
    }

    public List<Integer> getAllocations() {
        return allocations;
    }
}
