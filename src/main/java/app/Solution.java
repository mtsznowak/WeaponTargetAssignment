package app;

import setup.Parameters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<Integer> assignment = new ArrayList<>();
    private BigDecimal solutionValue = BigDecimal.valueOf(Double.MAX_VALUE);

    public void constructSolution() {
        int i;
        for(int k = 0; k < Parameters.getNumOfWeapons(); k++){
            i = findTargetIndexForWeapon(k);
            if (assignment.size() != Parameters.getNumOfWeapons()) {            // awesome workaround
                assignment.add(i);
            } else {
                assignment.set(k, i);
            }
            updatePheromoneValuesLocally(k);
//            BigDecimal subtract = BigDecimal.ONE.subtract(Parameters.getKillProbability(i, k));
//            Parameters.setTargetValue(i, Parameters.getTargetValue(i).multiply(subtract));
            Parameters.calculateHeuristicValues();
        }

        solutionValue = calculateSolution(assignment);
    }

    public void resetAssignments() {
        assignment.clear();
    }

    private BigDecimal calculateSolution(List<Integer> allocations) {
        BigDecimal returnValue = BigDecimal.ZERO;
        for (int i = 0; i < Parameters.getNumOfTargets(); i++) {
            for (int k = 0; k < Parameters.getNumOfWeapons(); k++) {
                if (allocations.get(k) == i) {
                    BigDecimal subtract = BigDecimal.ONE.subtract(Parameters.getKillProbability(i, k));
                    Parameters.setTargetValue(i, Parameters.getTargetValue(i).multiply(subtract));
                }
            }
            returnValue = returnValue.add(Parameters.getTargetValue(i));
        }
        return returnValue;
    }

    private int findTargetIndexForWeapon(int k) {
        int targetIndex = 0;
        BigDecimal randomValue = BigDecimal.valueOf(Math.random());
        BigDecimal q = randomValue;

        if (q.compareTo(Parameters.q0) == -1) {     //q < Parameters.q0
            targetIndex = argMax(k);
        } else {
            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < Parameters.getNumOfTargets(); i++) {
                BigDecimal tmp = Parameters.getHeuristicValues().get(i).get(k).multiply(Parameters.getPheromoneValues().get(i).get(k));
                total = total.add(tmp);
            }
            q = BigDecimal.valueOf(Math.random()).multiply(total);
            total = BigDecimal.ZERO;
            for (int i = 0; i < Parameters.getNumOfTargets(); i++) {
                BigDecimal tmp = Parameters.getHeuristicValues().get(i).get(k).multiply(Parameters.getPheromoneValues().get(i).get(k));
                total = total.add(tmp);
                if (q.compareTo(total) <= 0) {         //q <= total
                    targetIndex = i;
                    break;
                }
            }
        }
        return targetIndex;
    }

    private void updatePheromoneValuesLocally(int k) {
        for (int i = 0; i < Parameters.getNumOfTargets(); i++) {
            BigDecimal tmp = Parameters.getPheromoneValues().get(i).get(k).multiply(Parameters.EVAPORATION_RATE);
            BigDecimal augend = Parameters.EVAPORATION_RATE.multiply(BigDecimal.ONE)
                    .divide(BigDecimal.valueOf(Parameters.getNumOfAnts()), Parameters.divisionPrecision, BigDecimal.ROUND_HALF_UP)
                    .multiply(solutionValue);
            BigDecimal val = tmp.add(augend);
            Parameters.getPheromoneValues().get(i).set(k, val);
        }
    }

    private int argMax(int k) {
        int targetIndex = 0;
        BigDecimal minValue = BigDecimal.valueOf(Double.MIN_VALUE);
        BigDecimal maxValue = minValue;

        for (int i = 0; i < Parameters.getNumOfTargets(); i++) {
            BigDecimal value = Parameters.getPheromoneValues().get(i).get(k).multiply(Parameters.getHeuristicValues().get(i).get(k));
            if (value.compareTo(maxValue) == 1) {
                targetIndex = i;
                maxValue = value;
            }
        }

        return targetIndex;
    }

    public BigDecimal getSolutionValue() {
        return solutionValue;
    }

    public List<Integer> getAssignment() {
        return assignment;
    }

    public void setAssignment(List<Integer> assignment) {
        this.assignment = assignment;
    }

    public void setSolutionValue(BigDecimal solutionValue) {
        this.solutionValue = solutionValue;
    }

    public void printAssignments() {
        for(int i = 0; i < assignment.size(); i++){
            System.out.println("WEAPON " + i + " ASSIGNMENT " + assignment.get(i));
        }
    }
}
