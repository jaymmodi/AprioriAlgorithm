import java.util.*;

/**
 * Created by jay on 3/28/16.
 */
public class DataSet {

    private int totalAttributes;
    private List<String> attributeNames;
    private List<String> attributeTypes;
    private Set<String> classLabels;
    private HashMap<String, Set<String>> distinctValuesPerColumn;
    private String pathToFile;

    public DataSet() {
        this.distinctValuesPerColumn = new HashMap<>();
        this.classLabels = new HashSet<>();
    }

    public int getTotalAttributes() {
        return totalAttributes;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public void setTotalAttributes(int totalAttributes) {
        this.totalAttributes = totalAttributes;
    }

    public List<String> getAttributeNames() {
        return attributeNames;
    }

    public void setAttributeNames(List<String> attributeNames) {
        this.attributeNames = attributeNames;
    }

    public List<String> getAttributeTypes() {
        return attributeTypes;
    }

    public void setAttributeTypes(List<String> attributeTypes) {
        this.attributeTypes = attributeTypes;
    }

    public Set<String> getClassLabels() {
        return classLabels;
    }

    public void setClassLabels(Set<String> classLabels) {
        this.classLabels = classLabels;
    }

    public HashMap<String, Set<String>> getDistinctValuesPerColumn() {
        return distinctValuesPerColumn;
    }

    public void setDistinctValuesPerColumn(HashMap<String, Set<String>> distinctValuesPerColumn) {
        this.distinctValuesPerColumn = distinctValuesPerColumn;
    }

    public List<String> getDistinctItemsets() {
        List<String> itemsets = new ArrayList<>();

        for (String attributeName : attributeNames) {
            Set<String> distinctValues = this.getDistinctValuesPerColumn().get(attributeName);
            for (String distinctValue : distinctValues) {
                itemsets.add(attributeName + "_" + distinctValue);
            }
        }
        itemsets.addAll(classLabels);
        return itemsets;
    }
}
