package setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.*;

public class Parameters {

    private static List<Double> targetValues;
    private static Map<Integer, List<Double>> heuristicValues;
    private static Map<Integer, List<Double>> pheromoneValues;
    private static Map<Integer, List<Double>> killProbabilities;
    private static int ALLOWED_TIME = 2; // TODO set time
    private static LocalTime START_TIME = LocalTime.now();
    public static double EVAPORATION_RATE = 0.1;
    public static LocalTime END_TIME = START_TIME.plusSeconds(ALLOWED_TIME);
    public static int numOfAnts;
    public static double q0 = 0.5;

    public static void readParameters(ClassLoader classLoader) throws IOException {
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

    private static void initMaps() {
        pheromoneValues = new HashMap<>();
        heuristicValues = new HashMap<>();
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

    public static void calculatePheromoneValues(double solutionValue) {
        int numOfTargets = targetValues.size();
        int numOfWeapons = killProbabilities.size();
        for (int i = 0; i < numOfTargets; i++) {
            for (int k = 0; k < numOfWeapons; k++) {
                double val = 1 / (double) numOfAnts * solutionValue;
                pheromoneValues.get(i).set(k, val);
            }
        }
    }

    public static void calculateHeuristicValues() {
        int numOfTargets = targetValues.size();
        int numOfWeapons = killProbabilities.size();

        for (int i = 0; i < numOfTargets; i++) {
            for (int k = 0; k < numOfWeapons; k++) {
                heuristicValues.get(i).set(k, targetValues.get(i) * killProbabilities.get(i).get(k));
            }
        }
    }

    public static void updatePheromoneValues(List<Integer> iterationBestSolAlloc, double bestSolValue) {
        double PHEROMONE_INCREASE_RATE = 0.1;
        for (int i = 0; i < getNumOfTargets(); i++) {
            for (int k = 0; k < getNumOfWeapons(); k++) {
                if (iterationBestSolAlloc.get(k) == i) {
                    double tmp = getPheromoneValues().get(i).get(k) * (1 - PHEROMONE_INCREASE_RATE);
                    double val = tmp + (PHEROMONE_INCREASE_RATE * (1/bestSolValue));
                    getPheromoneValues().get(i).set(k, val);
                }
            }
        }
    }

    public static void printHeuristic() {
        for (int i = 0; i < heuristicValues.size(); i++) {
            for (int k = 0; k < heuristicValues.get(i).size(); k++) {
                System.out.print(heuristicValues.get(i).get(k) + " ");
            }
            System.out.println();
        }
    }

    public static void printPheromons() {
        for (int i = 0; i < pheromoneValues.size(); i++) {
            for (int k = 0; k < pheromoneValues.get(i).size(); k++) {
                System.out.print(pheromoneValues.get(i).get(k) + " ");
            }
            System.out.println();
        }
    }

    public static Double getTargetValue(int index) {
        return targetValues.get(index);
    }

    public static Double getKillProbability(int i, int k) {
        return killProbabilities.get(i).get(k);
    }

    public static int getNumOfTargets() {
        return targetValues.size();
    }

    public static int getNumOfWeapons() {
        return killProbabilities.size();
    }

    public static void setTargetValue(int index, Double element) {
        targetValues.set(index, element);
    }

    public static Map<Integer, List<Double>> getHeuristicValues() {
        return heuristicValues;
    }

    public static Map<Integer, List<Double>> getPheromoneValues() {
        return pheromoneValues;
    }

    public static int getNumOfAnts() {
        return numOfAnts;
    }
}