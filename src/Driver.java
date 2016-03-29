import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

/**
 * Created by jay on 3/28/16.
 */
public class Driver {

    public static void main(String[] args) {
        System.out.println("Jay");

        DataSet dataSet = new DataSet();

        readMetaData(dataSet, "metadata");

        readData(dataSet);
    }

    private static void readData(DataSet dataSet) {

    }

    private static void readMetaData(DataSet dataSet, String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            dataSet.setPathToFile(bufferedReader.readLine());

            dataSet.setTotalAttributes(Integer.parseInt(bufferedReader.readLine()));

            dataSet.setAttributeNames(Arrays.asList(bufferedReader.readLine().split(",")));

            dataSet.setAttributeTypes(Arrays.asList(bufferedReader.readLine().split(",")));

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}