import java.io.BufferedReader;
import java.io.FileReader;
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

        List<String> distinctItemsets = dataSet.getDistinctItemsets();

        distinctItemsets.forEach(System.out::println);

        System.out.println(distinctItemsets.size());
    }

    private static void readData(DataSet dataSet) {

        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(dataSet.getPathToFile()), charset)) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

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