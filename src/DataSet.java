import java.util.HashMap;
import java.util.List;

/**
 * Created by jay on 3/28/16.
 */
public class DataSet {

    public int totalAttributes;
    public List<String> attributeNames;
    public List<String> attributeTypes;
    public List<String> classLabels;
    public HashMap<String, List<String>> distinctValuesPerColumn;
    public String pathToFile;

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

    public List<String> getClassLabels() {
        return classLabels;
    }

    public void setClassLabels(List<String> classLabels) {
        this.classLabels = classLabels;
    }

    public HashMap<String, List<String>> getDistinctValuesPerColumn() {
        return distinctValuesPerColumn;
    }

    public void setDistinctValuesPerColumn(HashMap<String, List<String>> distinctValuesPerColumn) {
        this.distinctValuesPerColumn = distinctValuesPerColumn;
    }
}
