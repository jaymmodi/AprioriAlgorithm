import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jay on 4/3/16.
 */
public class GenerateRule {
    private List<Set<String>> frequentItemsets;
    private double confidenceThreshold;
    private HashMap<String, Integer> freqItemsetCount;

    public GenerateRule(List<Set<String>> frequentItemsets, HashMap<String, Integer> freqItemsetCount) {
        this.frequentItemsets = frequentItemsets;
        this.freqItemsetCount = freqItemsetCount;
        this.confidenceThreshold = 0.6;
    }

    public List<Rule> getAllRules() {
        List<Rule> rulesList = new ArrayList<>();

        for (Set<String> frequentItemset : this.frequentItemsets) {
            String itemsetsWithComma = String.join(",", frequentItemset);

            List<Rule> highConfRulesWithOneConsequent = getHighConfidenceRulesOfOneConsequent(itemsetsWithComma);
            rulesList.addAll(highConfRulesWithOneConsequent);

            if (highConfRulesWithOneConsequent.size() > 1) {
                List<Rule> highConfRules = generateMoreRules(highConfRulesWithOneConsequent, itemsetsWithComma);
                rulesList.addAll(highConfRules);
            }

        }

        return rulesList;
    }

    private List<Rule> generateMoreRules(List<Rule> highConfRules, String itemsetsWithComma) {

        List<Rule> mergedRules = new ArrayList<>();
        Set<String> allValuesSet = new HashSet<>(Arrays.asList(itemsetsWithComma.split(",")));
        Set<String> consequentSet = new HashSet<>();

        List<String> allOneConsequents = highConfRules.stream()
                .map(Rule::getEnd)
                .collect(Collectors.toList());

        List<String> allSuperSets = getSuperSets(allOneConsequents, itemsetsWithComma);

        for (String consequent : allSuperSets) {
            consequentSet.clear();
            consequentSet.addAll(Arrays.asList(consequent.split(",")));

            Rule rule = new Rule();
            rule.setEnd(consequent);
            rule.setEndCount(this.freqItemsetCount.get(consequent));

            String source = allValuesSet.stream()
                    .filter(s -> !consequent.contains(s))
                    .collect(Collectors.joining(","));

            rule.setSource(source);
            rule.setSourceCount(this.freqItemsetCount.get(source));
            rule.setSourceEndTogether(this.freqItemsetCount.get(itemsetsWithComma));

            if (rule.getConfidence() > this.confidenceThreshold) {
                mergedRules.add(rule);
            }
        }

        return mergedRules;
    }

    private List<String> getSuperSets(List<String> allOneConsequents, String itemsetsWithComma) {
        List<String> superSets = new ArrayList<>();

        int totalNumberOfSets = (int) Math.pow(2, allOneConsequents.size());

        for (int i = 0; i < totalNumberOfSets; i++) {
            List<String> internalList = new ArrayList<>();

            for (int j = 0; j < allOneConsequents.size(); j++) {
                if ((i & (1 << j)) > 0) {
                    internalList.add(allOneConsequents.get(j));
                }
            }
            String setString = String.join(",", internalList);

            int length = setString.split(",").length;
            if (length >= 2 && (length != itemsetsWithComma.split(",").length)) {
                superSets.add(setString);
            }
        }

        return superSets;

    }

    private List<Rule> getHighConfidenceRulesOfOneConsequent(String itemsetsWithComma) {
        List<Rule> rules = new ArrayList<>();

        String[] candidates = itemsetsWithComma.split(",");

        for (String candidate : candidates) {

            Rule rule = new Rule();
            rule.setEnd(candidate);
            rule.setEndCount(this.freqItemsetCount.get(candidate));

            String source = Arrays.stream(candidates)
                    .filter(string -> !string.equalsIgnoreCase(candidate))
                    .collect(Collectors.joining(","));
            rule.setSource(source);
            rule.setSourceCount(this.freqItemsetCount.get(source));
            rule.setSourceEndTogether(this.freqItemsetCount.get(itemsetsWithComma));

            if (rule.getConfidence() > this.confidenceThreshold) {
                rules.add(rule);
            }
        }

        return rules;
    }

}
