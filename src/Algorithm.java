import java.util.HashMap;
import java.util.List;

/**
 * Created by jay on 3/29/16.
 */
public class Algorithm {

    private HashMap<Integer, List<Integer>> sparseMap;
    private DataSet dataSet;

    public Algorithm(SparseMatrix sparseMatrix) {
        this.dataSet = sparseMatrix.dataSet;
        this.sparseMap = sparseMatrix.getIdVsIsPresentMap();
    }

    public HashMap<Integer, List<Integer>> getSparseMap() {
        return sparseMap;
    }

    public void run() {
        //f1
        // support counting

        // use f1 to get candidate itemset of size 2.
        // support count

        // iterate till there are no frequent itemsets
        // return all frequent itemsets.
    }

    public int getSupportCount(String pattern) {

        // find in tra

        return 0;
    }
}
