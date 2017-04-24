package setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Karol on 24/04/2017.
 */
public class Parameters {

    private ClassLoader classLoader;
    private List<Double> targetValues;
    private Map<Integer, List<Double>> heuristicValues;
    private Map<Integer, List<Double>> pheromoneValues;
    private Map<Integer, List<Double>> killProbabilities;

    public Parameters() {
        this.classLoader = getClass().getClassLoader();
        this.pheromoneValues = new HashMap<>();
        this.heuristicValues = new HashMap<>();
    }

    public void readParameters() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("config.txt")));

        String line = bufferedReader.readLine();
        int weapons = Integer.valueOf(line.split(" ")[0]);
        int targets = Integer.valueOf(line.split(" ")[1]);

        targetValues = new ArrayList<Double>(targets);
        killProbabilities = new HashMap<>(weapons);

        line = bufferedReader.readLine();
        List<String> values = Arrays.asList(line.split(" "));
        values.forEach((v) -> targetValues.add(Double.valueOf(v)));

        line = bufferedReader.readLine();
        Integer noOfWeapon = 0;

        while (line != null) {
            List<Double> probabilities = new ArrayList<>();
            Arrays.asList(line.split(" ")).forEach((v) -> {
                probabilities.add(Double.valueOf(v));
            });
            killProbabilities.put(noOfWeapon, probabilities);
            noOfWeapon++;
            line = bufferedReader.readLine();
        }
        System.out.println(targetValues);
        System.out.println(killProbabilities);

        bufferedReader.close();
        initMaps();
    }

    // TODO: now solutionValue is quite hardcoded. Should be specify type or smth
    public void calculatePheromoneValues(int noOfAnts, double solutionValue) {
        solutionValue = 1.0;

        int numOfTargets = targetValues.size();
        int numOfWeapons = killProbabilities.size();
        for (int i = 0; i < numOfTargets; i++) {
            for (int k = 0; k < numOfWeapons; k++) {
                double val = 1/(double)noOfAnts * solutionValue;
                pheromoneValues.get(i).set(k, val);
            }
        }
    }

    public void calculateHeuristicValues() {
        int numOfTargets = targetValues.size();
        int numOfWeapons = killProbabilities.size();

        for (int i = 0; i < numOfTargets; i++) {
            for (int k = 0; k < numOfWeapons; k++) {
                heuristicValues.get(i).set(k, targetValues.get(i) * killProbabilities.get(i).get(k));
            }
        }
    }

    public void printHeuristic() {
        for (int i = 0; i < heuristicValues.size(); i++) {
            for (int k = 0; k < heuristicValues.get(i).size(); k++) {
                System.out.print(heuristicValues.get(i).get(k) + " ");
            }
            System.out.println();
        }
    }

    public void printPheromons() {
        for (int i = 0; i < pheromoneValues.size(); i++) {
            for (int k = 0; k < pheromoneValues.get(i).size(); k++) {
                System.out.print(pheromoneValues.get(i).get(k) + " ");
            }
            System.out.println();
        }
    }

    private void initMaps(){
        int numOfTargets = targetValues.size();
        int numOfWeapons = killProbabilities.size();

        for (int i = 0; i < numOfTargets; i++) {
            heuristicValues.put(i, new ArrayList<>());
            pheromoneValues.put(i, new ArrayList<>());
            for (int k = 0; k < numOfWeapons; k++) {
                heuristicValues.get(i).add(0.0);
                pheromoneValues.get(i).add(0.0);
            }
        }
    }

    public Double getTargetValue(int index){
        return targetValues.get(index);
    }

    public Double getKillProbability(int i, int k){
        return killProbabilities.get(i).get(k);
    }

    public int getNumOfTargets(){
        return targetValues.size();
    }

    public int getNumOfWeapons(){
        return killProbabilities.size();
    }

    public void setTargetValue(int index, Double element){
        targetValues.set(index, element);
    }

    public Map<Integer, List<Double>> getHeuristicValues() {
        return heuristicValues;
    }

    public Map<Integer, List<Double>> getPheromoneValues() {
        return pheromoneValues;
    }
}