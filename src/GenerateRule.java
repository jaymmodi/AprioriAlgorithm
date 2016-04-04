import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jay on 4/3/16.
 */
public class GenerateRule {
    private List<Set<String>> frequentItemsets;
    private int confidenceThreshold;

    public GenerateRule(List<Set<String>> frequentItemsets) {
        this.frequentItemsets = frequentItemsets;
        this.confidenceThreshold = 100;
    }

    public List<Set<Rule>> getAllRules() {
        List<Set<Rule>> rulesList = new ArrayList<>();

        for (Set<String> frequentItemset : this.frequentItemsets) {
            List<Rule> subsetsOfOneConsequent = getSubsetsOfOneConsequent(frequentItemset);
            subsetsOfOneConsequent = checkConfidenceThreshold(subsetsOfOneConsequent);
        }


        return rulesList;
    }

    private List<Rule> checkConfidenceThreshold(List<Rule> subsetsOfOneConsequent) {
        return null;
    }

    private List<Rule> getSubsetsOfOneConsequent(Set<String> frequentItemset) {
        List<Rule> rules = new ArrayList<>();


        String itemsetsWithComma = String.join(",", frequentItemset);

        String[] candidates = itemsetsWithComma.split(",");

        for (String candidate : candidates) {

            Rule rule = new Rule();
            rule.setEnd(candidate);

            String source = Arrays.stream(candidates)
                    .filter(string -> !string.equalsIgnoreCase(candidate))
                    .collect(Collectors.joining(","));
            rule.setSource(source);

            rules.add(rule);
        }

        return rules;
    }

}
