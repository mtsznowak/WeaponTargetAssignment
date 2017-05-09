package generator;

import java.io.*;
import java.nio.Buffer;
import java.util.Random;

/**
 * Created by Karol on 09/05/2017.
 */
public class ParamGenerator {

    public static void createWeaponTargetsParams(String fileName, int weapons, int targets) {
        Random rand = new Random();
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("src/main/resources/" + fileName, "UTF-8");
            writer.println(weapons + " " + targets);
            // generate target values
            StringBuilder targetValues = new StringBuilder();
            for (int i = 0; i < targets; i++) {
                targetValues.append(rand.nextInt(100));
                targetValues.append(" ");
            }
            writer.println(targetValues.toString().trim());
            // generate weapon values
            StringBuilder weaponValues = new StringBuilder();
            for (int i = 0; i < weapons; i++) {
                for (int j = 0; j < targets; j++) {
                    weaponValues.append(rand.nextDouble());
                    weaponValues.append(" ");
                }
                weaponValues.append("\n");
            }
            writer.println(weaponValues.toString().trim());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int weapons = 0;
        int targets = 0;
        try {
            System.out.println("Weapons: ");
            weapons = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Targets: ");
            targets = Integer.valueOf(bufferedReader.readLine());
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ParamGenerator.createWeaponTargetsParams("WTAparams.txt", weapons, targets);
    }
}
