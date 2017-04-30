package setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

public class Parameters {

    private static List<BigDecimal> targetValues;
    private static List<BigDecimal> firstTargetValues;
    private static Map<Integer, List<BigDecimal>> heuristicValues;
    private static Map<Integer, List<BigDecimal>> pheromoneValues;
    private static Map<Integer, List<BigDecimal>> killProbabilities;
    private static int ALLOWED_TIME;
    private static LocalTime START_TIME = LocalTime.now();
    private static BigDecimal PHEROMONE_INCREASE_RATE;
    public static BigDecimal EVAPORATION_RATE;
    public static BigDecimal q0;
    public static LocalTime END_TIME;
    public static int NUMBER_OF_ANTS;
    public static int divisionPrecision = 20;

    public static void readAlgorithmParameters(ClassLoader classLoader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("algorithmParams.txt")));

        String line = bufferedReader.readLine();
        ALLOWED_TIME = Integer.valueOf(line.split(" ")[1]);
        line = bufferedReader.readLine();
        NUMBER_OF_ANTS = Integer.valueOf(line.split(" ")[1]);
        line = bufferedReader.readLine();
        PHEROMONE_INCREASE_RATE = BigDecimal.valueOf(Double.valueOf(line.split(" ")[1]));
        line = bufferedReader.readLine();
        EVAPORATION_RATE = BigDecimal.valueOf(Double.valueOf(line.split(" ")[1]));
        line = bufferedReader.readLine();
        q0 = BigDecimal.valueOf(Double.valueOf(line.split(" ")[1]));
        END_TIME = START_TIME.plusSeconds(ALLOWED_TIME);

        System.out.println("ALLOWED_TIME " + ALLOWED_TIME);
        System.out.println("NUMBER_OF_ANTS " + NUMBER_OF_ANTS);
        System.out.println("PHEROMONE_INCREASE_RATE " + PHEROMONE_INCREASE_RATE);
        System.out.println("EVAPORATION_RATE " + EVAPORATION_RATE);
        System.out.println("q0 " + q0);

        bufferedReader.close();
    }

    public static void readParameters(ClassLoader classLoader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("WTAparams.txt")));

        String line = bufferedReader.readLine();
        int weapons = Integer.valueOf(line.split(" ")[0]);
        int targets = Integer.valueOf(line.split(" ")[1]);

        targetValues = new ArrayList<BigDecimal>(targets);
        firstTargetValues = new ArrayList<BigDecimal>(targets);
        killProbabilities = new HashMap<>(weapons);

        line = bufferedReader.readLine();
        List<String> values = Arrays.asList(line.split(" "));
        values.forEach((v) -> {
            targetValues.add(BigDecimal.valueOf(Double.valueOf(v)));
            firstTargetValues.add(BigDecimal.valueOf(Double.valueOf(v)));
        });

        line = bufferedReader.readLine();
        Integer noOfWeapon = 0;

        while (line != null) {
            List<BigDecimal> probabilities = new ArrayList<>();
            Arrays.asList(line.split(" ")).forEach((v) -> {
                probabilities.add(BigDecimal.valueOf(Double.valueOf(v)));
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
                heuristicValues.get(i).add(BigDecimal.ZERO);
                pheromoneValues.get(i).add(BigDecimal.ZERO);
            }
        }
    }

    public static void calculatePheromoneValues(BigDecimal solutionValue) {
        int numOfTargets = targetValues.size();
        int numOfWeapons = killProbabilities.size();
        for (int i = 0; i < numOfTargets; i++) {
            for (int k = 0; k < numOfWeapons; k++) {
                BigDecimal division = BigDecimal.ONE.divide(BigDecimal.valueOf(NUMBER_OF_ANTS), divisionPrecision, BigDecimal.ROUND_HALF_UP);
                BigDecimal val = division.multiply(solutionValue);
                pheromoneValues.get(i).set(k, val);
            }
        }
    }

    public static void calculateHeuristicValues() {
        int numOfTargets = targetValues.size();
        int numOfWeapons = killProbabilities.size();

        for (int i = 0; i < numOfTargets; i++) {
            for (int k = 0; k < numOfWeapons; k++) {
                BigDecimal tmp = targetValues.get(i).multiply(killProbabilities.get(i).get(k));
                heuristicValues.get(i).set(k, tmp);
            }
        }
    }

    public static void updatePheromoneValues(List<Integer> iterationBestSolAlloc, BigDecimal bestSolValue) {
        for (int i = 0; i < getNumOfTargets(); i++) {
            for (int k = 0; k < getNumOfWeapons(); k++) {
                if (iterationBestSolAlloc.get(k) == i) {
                    BigDecimal tmp = getPheromoneValues().get(i).get(k).multiply(BigDecimal.ONE.subtract(PHEROMONE_INCREASE_RATE));
                    BigDecimal division = BigDecimal.ONE.divide(bestSolValue, Parameters.divisionPrecision, BigDecimal.ROUND_HALF_UP);
                    BigDecimal val = tmp.add(PHEROMONE_INCREASE_RATE.multiply(division));
                    getPheromoneValues().get(i).set(k, val);
                }
            }
        }
    }

    public static void printHeuristic() {
        System.out.println("Heuristic values:");
        for (int i = 0; i < heuristicValues.size(); i++) {
            for (int k = 0; k < heuristicValues.get(i).size(); k++) {
                System.out.print(heuristicValues.get(i).get(k) + " ");
            }
            System.out.println();
        }
    }

    public static void printPheromons() {
        System.out.println("Pheromons values:");
        for (int i = 0; i < pheromoneValues.size(); i++) {
            for (int k = 0; k < pheromoneValues.get(i).size(); k++) {
                System.out.print(pheromoneValues.get(i).get(k) + " ");
            }
            System.out.println();
        }
    }

    public static BigDecimal getTargetValue(int index) {
        return targetValues.get(index);
    }

    public static BigDecimal getKillProbability(int i, int k) {
        return killProbabilities.get(i).get(k);
    }

    public static int getNumOfTargets() {
        return targetValues.size();
    }

    public static int getNumOfWeapons() {
        return killProbabilities.size();
    }

    public static void setTargetValue(int index, BigDecimal element) {
        targetValues.set(index, element);
    }

    public static Map<Integer, List<BigDecimal>> getHeuristicValues() {
        return heuristicValues;
    }

    public static Map<Integer, List<BigDecimal>> getPheromoneValues() {
        return pheromoneValues;
    }

    public static int getNumOfAnts() {
        return NUMBER_OF_ANTS;
    }

    public static void resetTargetValues(){
        for(int i = 0; i < targetValues.size(); i++){
            targetValues.set(i,firstTargetValues.get(i));
        }
    }
}