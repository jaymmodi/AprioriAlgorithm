import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by jay on 3/28/16.
 */
public class Driver {

    public static void main(String[] args) {
        DataSet dataSet = new DataSet();

        readMetaData(dataSet, "metadata");

        readData(dataSet);

        SparseMatrix sparseMatrix = new SparseMatrix(dataSet);
        sparseMatrix.makeMatrix();

        String candidateTypeGeneration = getCandidateGenerationType();
        Algorithm algorithm = new Algorithm(sparseMatrix, candidateTypeGeneration);
        List<Set<String>> frequentItemsets = algorithm.run();

        GenerateRule generateRule = new GenerateRule(frequentItemsets);
        System.out.println("Total Rules = " + generateRule.getAllRules().size());
    }

    private static String getCandidateGenerationType() {
        System.out.println("Please select a number for candidate Generation Process");
        System.out.println("1. F(k-1) * F(1)  2. F(k-1) * F(k-1)");
        String line = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            line = bufferedReader.readLine();

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (line.equals("1") || line.equalsIgnoreCase("F(k-1) * F(1)")) {
            return "1";
        } else if (line.equals("2") || line.equalsIgnoreCase("F(k-1) * F(k-1)")) {
            return "2";
        } else {
            System.out.println("Please provide correct input in your next attempt");
            System.exit(1);
        }
        return null;
    }

    private static void readData(DataSet dataSet) {

        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(dataSet.getPathToFile()), charset)) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                dataSet.getInstances().add(line);
                findDistinctValuesPerColumn(values, dataSet);

            }
            bufferedReader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static void findDistinctValuesPerColumn(String[] values, DataSet dataSet) {
        HashMap<String, Set<String>> distinctValuesPerAttribute = dataSet.getDistinctValuesPerColumn();
        Set<String> classLabels = dataSet.getClassLabels();

        for (int i = 0; i < values.length; i++) {
            if (i == values.length - 1) {
                classLabels.add(values[i]);
            } else {
                String attributeName = dataSet.getAttributeNames().get(i);

                insertInMap(distinctValuesPerAttribute, attributeName, values[i]);
            }
        }
    }

    private static void insertInMap(HashMap<String, Set<String>> distinctValuesPerAttribute, String key, String value) {

        Set<String> distinctValues;
        if (distinctValuesPerAttribute.containsKey(key)) {
            distinctValues = distinctValuesPerAttribute.get(key);
            distinctValues.add(value);

        } else {
            distinctValues = new HashSet<>();
            distinctValues.add(value);
            distinctValuesPerAttribute.put(key, distinctValues);
        }
    }

    private static void readMetaData(DataSet dataSet, String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            dataSet.setPathToFile(bufferedReader.readLine());

            dataSet.setTotalAttributes(Integer.parseInt(bufferedReader.readLine()));

            dataSet.setAttributeNames(Arrays.asList(bufferedReader.readLine().split(",")));

            dataSet.setAttributeTypes(Arrays.asList(bufferedReader.readLine().split(",")));

            bufferedReader.close();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


}