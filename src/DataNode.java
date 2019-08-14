import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

/**
 * Node class for the Data Nodes
 */
public class DataNode extends BPlusNode{


    protected ArrayList<Double> values;
    protected DataNode rightSibling;
    protected DataNode leftSibling;

    public DataNode(int key, Double value) {
        keys = new ArrayList<Integer>();
        values = new ArrayList<Double>();
        keys.add(key);
        values.add(value);
    }

    public DataNode() {
        keys = new ArrayList<Integer>();
        values = new ArrayList<Double>();
    }

    /**
     * Insert into Data Node
     */
    public void insertToDataNode(Integer key, Double value){

        if(key.compareTo(keys.get(0)) < 0) {
            keys.add(0, key);
            values.add(0, value);
        }
        else if (key.compareTo(keys.get(keys.size() - 1)) > 0){
            keys.add(key);
            values.add(value);

        }
        else{
            ListIterator<Integer> iterator = keys.listIterator();
            while(iterator.hasNext()){
                if(iterator.next().compareTo(key) > 0){
                    int position = iterator.previousIndex();
                    keys.add(position, key);
                    values.add(position,value);
                    break;

                }
            }
        }

    }

}
