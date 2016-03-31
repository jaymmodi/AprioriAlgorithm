import java.util.*;
import java.util.function.Function;
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

    public void run() {

        int k = 1;
        List<String> freqItemsetsOfSizeOne = getFrequentItemsetsOfSize1(this.allCandidatesWithId.keySet(), k);
        System.out.println(freqItemsetsOfSizeOne.size());

        List<Set<String>> freqItemsetsHighK = new ArrayList<>();
        while (freqItemsetsHighK != null) {
            ++k;
            Set<String> candidateItemsets = getCandidateItemsets(freqItemsetsHighK, freqItemsetsOfSizeOne, k);

        }
    }

    private List<String> getFrequentItemsetsOfSize1(Set<String> allCandidates, int k) {

        return allCandidates.
                stream()
                .filter(string -> getSupportCount(string, k) > this.supportThreshold)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Set<String> getCandidateItemsets(List<Set<String>> freqItemsets, List<String> freqItemsetsOfSizeOne, int k) {
        if (this.candidateGenerationType.equals("1")) {
            return candidateKInto1(freqItemsets, freqItemsetsOfSizeOne, k);
        } else {
            return candidateKIntoKMinus1(freqItemsets, k);
        }
    }

    private Set<String> candidateKIntoKMinus1(List<Set<String>> freqItemsets, int k) {
        return null;
    }

    private Set<String> candidateKInto1(List<Set<String>> freqItemsetsOfSizeK, List<String> freqItemsetsOfSize1, int k) {

        if (freqItemsetsOfSizeK.isEmpty()) {
            addDataToItemsets(freqItemsetsOfSizeK, freqItemsetsOfSize1);
        }

        Set<String> candidatesItemsetsK = new HashSet<>();
        for (Set<String> itemset : freqItemsetsOfSizeK) {
            String freqKItemsets = String.join(",", itemset);
            String lastString = ((TreeSet<String>) itemset).last();
            freqItemsetsOfSize1.stream().
                    filter(freq1Itemset -> lastString.compareToIgnoreCase(freq1Itemset) < 0)
                    .forEachOrdered(freq1Itemset -> {
                        String candidate = String.join(",", freqKItemsets, freq1Itemset);
                        candidatesItemsetsK.add(candidate);
                    });
        }

        return candidatesItemsetsK;
    }

    private void addDataToItemsets(List<Set<String>> itemsets, List<String> freqItemsetsOfSize1) {

        Function<String, Set<String>> convertToSet = string -> {
            Set<String> sortedSet = new TreeSet<>();
            sortedSet.add(string);
            return sortedSet;
        };

        itemsets.addAll(freqItemsetsOfSize1.stream()
                .map(convertToSet)
                .collect(Collectors.toList()));
    }


    private List<Set<String>> getFrequentItemsets(Set<String> allCandidates, int k) {

        Function<String, Set<String>> convertToSet = string -> {
            Set<String> sortedSet = new TreeSet<>();
            sortedSet.add(string);
            return sortedSet;
        };

        return allCandidates.
                stream()
                .filter(string -> getSupportCount(string, k) > this.supportThreshold)
                .map(convertToSet)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int getSupportCount(String pattern, int k) {
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
            if (internalCount == k) {
                count++;
            }
        }

        return count;
    }
}
