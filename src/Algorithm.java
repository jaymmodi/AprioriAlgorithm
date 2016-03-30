import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jay on 3/29/16.
 */
public class Algorithm {

    private HashMap<Integer, List<Integer>> sparseMap;
    private int supportThreshold;
    private HashMap<String, Integer> allCandidatesWithId;

    public Algorithm(SparseMatrix sparseMatrix) {
        DataSet dataSet = sparseMatrix.dataSet;
        this.sparseMap = sparseMatrix.getIdVsIsPresentMap();
        this.supportThreshold = 10;
        this.allCandidatesWithId = dataSet.getDistinctItemsets();
    }

    public HashMap<Integer, List<Integer>> getSparseMap() {
        return sparseMap;
    }

    public void run() {
        //f1
        // support counting
        List<String> freqItemsets = getFrequentItemsets(this.allCandidatesWithId.keySet());
        // use f1 to get candidate itemset of size 2.
        // support count

        // iterate till there are no frequent itemsets
        // return all frequent itemsets.
    }

    private List<String> getFrequentItemsets(Set<String> allCandidates) {

        return allCandidates.
                stream()
                .filter(string -> getSupportCount(string) > this.supportThreshold)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public int getSupportCount(String pattern) {
        String[] individualItemsets = pattern.split(",");
        int count = 0;
        int internalCount;

        for (Map.Entry<Integer, List<Integer>> transactionsWithId : this.sparseMap.entrySet()) {
            List<Integer> transaction = transactionsWithId.getValue();
            internalCount = 0;

            for (String itemset : individualItemsets) {
                if (transaction.contains(this.allCandidatesWithId.get(itemset))) {
                    internalCount++;
                }
            }
            if (internalCount == individualItemsets.length) {
                count++;
            }
        }

        return count;
    }
}
