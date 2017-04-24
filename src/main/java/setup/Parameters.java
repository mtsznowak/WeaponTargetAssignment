package setup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Karol on 24/04/2017.
 */
public class Parameters {

    ClassLoader classLoader = getClass().getClassLoader();
    private List<Integer> targetValues;
    private Map<Integer, List<Double>> killProbabilities;

    public void readParameters() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(classLoader.getResource("config.txt").getFile()));

        String line = bufferedReader.readLine();
        int weapons = Integer.valueOf(line.split(" ")[0]);
        int targets = Integer.valueOf(line.split(" ")[1]);

        targetValues = new ArrayList<Integer>(targets);
        killProbabilities = new HashMap<>();

        line = bufferedReader.readLine();
        List<String> values = Arrays.asList(line.split(","));
        values.forEach((v) -> targetValues.add(Integer.valueOf(v)));

        line = bufferedReader.readLine();
        Integer noOfWeapon = 0;

        while(line != null) {
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
}
