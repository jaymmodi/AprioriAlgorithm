import java.util.HashMap;
import java.util.Map;

/**
 * Created by jay on 3/28/16.
 */
public class SparseMatrix {

    public HashMap<String, Integer> itemsetVsId;
    public DataSet dataSet;

    public SparseMatrix(DataSet dataSet) {
        this.dataSet = dataSet;
        this.itemsetVsId = new HashMap<>();
    }

    public void makeMatrix() {
        this.itemsetVsId = this.dataSet.getDistinctItemsets();
        for (Map.Entry<String, Integer> stringIntegerEntry : itemsetVsId.entrySet()) {
            System.out.println(stringIntegerEntry.getKey()+"            "+stringIntegerEntry.getValue());
        }
    }
}
