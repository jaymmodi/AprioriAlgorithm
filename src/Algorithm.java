import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jay on 3/29/16.
 */
public class Algorithm {

    private HashMap<Integer, List<Integer>> sparseMap;
    private int supportThreshold;
    private HashMap<String, Integer> allCandidatesWithId;
    private String candidateGenerationType;

    public Algorithm(SparseMatrix sparseMatrix, String candidateGenerationType) {
        DataSet dataSet = sparseMatrix.dataSet;
        this.sparseMap = sparseMatrix.getIdVsIsPresentMap();
        this.supportThreshold = 500;
        this.allCandidatesWithId = dataSet.getDistinctItemsets();
        this.candidateGenerationType = candidateGenerationType;
    }

    public HashMap<Integer, List<Integer>> getSparseMap() {
        return sparseMap;
    }

    public void run() {

        List<String> freqItemsets = getFrequentItemsets(this.allCandidatesWithId.keySet());
        System.out.println(freqItemsets.size());

        int k = 1;
        while (freqItemsets != null) {
            ++k;
            Set<String> candidateItemsets = getCandidateItemsets(freqItemsets, k);
            freqItemsets = getFrequentItemsets(candidateItemsets);
        }
    }

    private Set<String> getCandidateItemsets(List<String> freqItemsets, int k) {
        if (this.candidateGenerationType.equals("1")) {
            return candidateKInto1(freqItemsets, k);
        } else {
            return candidateKIntoKMinus1(freqItemsets, k);
        }
    }

    private Set<String> candidateKIntoKMinus1(List<String> freqItemsets, int k) {
        return null;
    }

    private Set<String> candidateKInto1(List<String> freqItemsets, int k) {
        return null;
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
