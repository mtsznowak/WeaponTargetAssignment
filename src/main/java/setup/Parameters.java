package setup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static javafx.scene.input.KeyCode.M;

/**
 * Created by Karol on 24/04/2017.
 */
public class Parameters {

    private ClassLoader classLoader;
    private List<Integer> targetValues;
    private Map<Integer, List<Double>> killProbabilities;
    private Map<Integer, Integer> pheromoneValues;

    public Parameters() {
        this.classLoader = getClass().getClassLoader();
        this.pheromoneValues = new HashMap<>();
    }

    public void readParameters() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(classLoader.getResource("config.txt").getFile()));

        String line = bufferedReader.readLine();
        int weapons = Integer.valueOf(line.split(" ")[0]);
        int targets = Integer.valueOf(line.split(" ")[1]);

        targetValues = new ArrayList<Integer>(targets);
        killProbabilities = new HashMap<>(weapons);

        line = bufferedReader.readLine();
        List<String> values = Arrays.asList(line.split(" "));
        values.forEach((v) -> targetValues.add(Integer.valueOf(v)));

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
    }

    // TODO: now solutionValue is quite hardcoded. Should be specify type or smth
    public void calculatePheromoneValues(int noOfAnts, double solutionValue) {
//        while k <= noOfW eapons do
//            pheromoneV alues[i][k] ← 1/noOfAnts × solutionV alue

        solutionValue = 1;

        int noOfTargets = this.targetValues.size();
        int noOfWeapons = this.killProbabilities.size();
        int i = 0, k;
        while (i <= noOfTargets) {
            k = 0;
            while (k <= noOfWeapons) {
                double val = 1/noOfAnts * solutionValue;
//                this.pheromoneValues.get(i)
                k++;
            }
            i++;
        }
    }
}
