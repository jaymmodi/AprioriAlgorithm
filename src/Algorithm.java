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
    private HashMap<String, Integer> wordCount;

    public Algorithm(SparseMatrix sparseMatrix, String candidateGenerationType) {
        DataSet dataSet = sparseMatrix.dataSet;
        this.sparseMap = sparseMatrix.getIdVsIsPresentMap();
        this.supportThreshold = 100;
        this.allCandidatesWithId = dataSet.getDistinctItemsets();
        this.candidateGenerationType = candidateGenerationType;
        this.wordCount = new HashMap<>();
    }

    public void run() {

        int k = 1;
        System.out.println("************ k = " + k + " ****************");
        System.out.println("Candidates = " + this.allCandidatesWithId.size());
        List<String> freqItemsetsOfSizeOne = getFrequentItemsetsOfSize1(this.allCandidatesWithId.keySet(), k);
        System.out.println("Frequent" + freqItemsetsOfSizeOne.size());

        ++k;
        System.out.println("************ k = " + k + " ****************");
        Set<String> candidatesItemsetsFor2 = getCandidateItemsetsForSize2(freqItemsetsOfSizeOne);
        System.out.println("Candidates = " + candidatesItemsetsFor2.size());
        candidatePrune(candidatesItemsetsFor2, k);
        System.out.println("Candidates after pruning = " + candidatesItemsetsFor2.size());
        List<Set<String>> freqItemsetsHighK = getFrequentItemsets(candidatesItemsetsFor2, k);
        System.out.println("Frequent = " + freqItemsetsHighK.size());

        while (true) {
            ++k;
            Set<String> candidateItemsets = getCandidateItemsets(freqItemsetsHighK, freqItemsetsOfSizeOne, k);

            System.out.println("************ k = " + k + " ****************");
            System.out.println("Candidates = " + candidateItemsets.size());

            candidatePrune(candidateItemsets, k);
            System.out.println("Candidates after pruning= " + candidateItemsets.size());


            List<Set<String>> tempItemsets = getFrequentItemsets(candidateItemsets, k);

            if (tempItemsets == null || tempItemsets.size() == 0) {
                break;
            } else {
                freqItemsetsHighK.clear();
                freqItemsetsHighK.addAll(tempItemsets);
                System.out.println("Frequent = " + freqItemsetsHighK.size());
            }
        }
        System.out.println("********************************");
        System.out.println("Actual Frequent Size = " + freqItemsetsHighK.size());
        System.out.println(freqItemsetsHighK);
    }

    private Set<String> getCandidateItemsetsForSize2(List<String> freqItemsetsOfSizeOne) {
        Set<String> size2 = new HashSet<>();

        for (String outerString : freqItemsetsOfSizeOne) {
            size2.addAll(freqItemsetsOfSizeOne.stream()
                    .filter(innerString -> outerString.compareToIgnoreCase(innerString) < 0)
                    .map(innerString -> String.join(",", outerString, innerString))
                    .collect(Collectors.toList()));
        }
        return size2;
    }

    private void candidatePrune(Set<String> candidateItemsets, int k) {
        Set<String> prunedCandidates = new HashSet<>();
        for (String pattern : candidateItemsets) {
            String[] candidates = pattern.split(",");

            boolean allMaTch = Arrays.stream(candidates)
                    .allMatch(candidate -> this.wordCount.get(candidate) > k - 1);
            if (allMaTch) {
                prunedCandidates.add(pattern);
            }

        }
        candidateItemsets.clear();
        candidateItemsets.addAll(prunedCandidates);
    }

    private List<String> getFrequentItemsetsOfSize1(Set<String> allCandidates, int k) {

        return allCandidates.
                stream()
                .filter(string -> getSupportCount(string, k) > this.supportThreshold)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Set<String> getCandidateItemsets(List<Set<String>> freqItemsets, List<String> freqItemsetsOfSizeOne, int k) {

//        if (freqItemsets.isEmpty()) {
//            addDataToItemsets(freqItemsets, freqItemsetsOfSizeOne);
//        }

        if (this.candidateGenerationType.equals("1")) {
            return candidateKInto1(freqItemsets, freqItemsetsOfSizeOne, k);
        } else {
            return candidateKIntoKMinus1(freqItemsets, k);
        }
    }

    private Set<String> candidateKIntoKMinus1(List<Set<String>> freqItemsets, int k) {
        Set<String> candidateItemsetsK = new HashSet<>();

        for (Set<String> freqItemset : freqItemsets) {

            String freqItemsetsPatternOutside = String.join(",", freqItemset);
            String[] allCandidatesOutside = freqItemsetsPatternOutside.split(",");
            String totalMinusLastOutside = Arrays.stream(allCandidatesOutside)
                    .limit(allCandidatesOutside.length - 1)
                    .collect(Collectors.joining(","));

            String outside = allCandidatesOutside[allCandidatesOutside.length - 1];

            for (Set<String> itemset : freqItemsets) {

                String freqItemsetsPatternInside = String.join(",", itemset);
                String[] allCandidatesInside = freqItemsetsPatternInside.split(",");
                String totalMinusLastInside = Arrays.stream(allCandidatesInside)
                        .limit(allCandidatesInside.length - 1)
                        .collect(Collectors.joining(","));

                String inside = allCandidatesInside[allCandidatesOutside.length - 1];

                if (totalMinusLastOutside.equalsIgnoreCase(totalMinusLastInside) && !outside.equalsIgnoreCase(inside)) {
                    if (inside.compareToIgnoreCase(outside) < 0) {
                        candidateItemsetsK.add(String.join(",", totalMinusLastInside, inside, outside));
                    } else {
                        candidateItemsetsK.add(String.join(",", totalMinusLastInside, outside, inside));
                    }
                }

            }

        }

        return candidateItemsetsK;
    }

    private Set<String> candidateKInto1(List<Set<String>> freqItemsetsOfSizeK, List<String> freqItemsetsOfSize1, int k) {

        Set<String> candidatesItemsetsK = new HashSet<>();
        for (Set<String> itemset : freqItemsetsOfSizeK) {

            String freqKItemsets = String.join(",", itemset);
            String[] allValues = freqKItemsets.split(",");
            String lastString = allValues[allValues.length - 1];

            freqItemsetsOfSize1.stream()
                    .filter(freq1Itemset -> lastString.compareToIgnoreCase(freq1Itemset) < 0)
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

        this.wordCount.clear();

        if (!allCandidates.isEmpty()) {
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
        } else {
            return null;
        }
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
                    addToWordCountMap(itemset, 1);
                }
            }
            if (internalCount == k) {
                count++;
            }
        }

        return count;
    }

    private void addToWordCountMap(String string, int count) {
        if (this.wordCount.containsKey(string)) {
            int prevCount = this.wordCount.get(string);
            this.wordCount.put(string, prevCount + count);
        } else {
            this.wordCount.put(string, count);
        }
    }
}
