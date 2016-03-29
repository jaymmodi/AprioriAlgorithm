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
    private List<String> instances;

    public DataSet() {
        this.distinctValuesPerColumn = new HashMap<>();
        this.classLabels = new HashSet<>();
        this.instances = new ArrayList<>();
    }


    public List<String> getInstances() {
        return instances;
    }

    public void setInstances(List<String> instances) {
        this.instances = instances;
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

    public HashMap<String, Integer> getDistinctItemsets() {
        HashMap<String, Integer> itemsetsVsId = new HashMap<>();

        int id = 1;
        for (String attributeName : attributeNames) {
            Set<String> distinctValues = this.getDistinctValuesPerColumn().get(attributeName);
            for (String distinctValue : distinctValues) {
                itemsetsVsId.put(attributeName + "_" + distinctValue, id);
                id++;
            }
        }

        for (String classLabel : this.classLabels) {
            itemsetsVsId.put(classLabel, id);
            id++;
        }

        return itemsetsVsId;
    }
}
