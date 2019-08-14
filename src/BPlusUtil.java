import java.util.ArrayList;

/**
 * Some utility methods used in BPlus tree across different operations
 */
public abstract class BPlusUtil {
    /**
     * If the node has reached a capacity equal to the degree
     */
    public static boolean isNodeFull(BPlusNode bNode, BPlusTreeInstance bTree){
        return bNode.keys.size() == bTree.getTreeOrder();
    }

    /**
     * If the node is deficient and has less than Ceil(m/2)-1
     */
    public static boolean isNodeDeficient(BPlusNode bNode, BPlusTreeInstance bPlusTreeInstance){
        return bNode.keys.size() < bPlusTreeInstance.getNodeDegree();

    }

    /**
     * Find the data node of for given node going down the tree from root
     */
    public static BPlusNode findDataNode(BPlusNode firstNode, int key) {
        if (firstNode instanceof DataNode) {
            return firstNode;
        } else {
            SearchNode searchNode = (SearchNode) firstNode;

            int indexLastKey = searchNode.keys.size() > 1 ? searchNode.keys.size() - 1 : 0;
            if (key < searchNode.keys.get(0)) {
                return findDataNode(searchNode.children.get(0), key);

            } else if (key >= searchNode.keys.get(indexLastKey)) {
                return findDataNode(searchNode.children.get(searchNode.children.size() - 1), key);

            } else {
                for(int i = 0; i < indexLastKey; i++) {
                    if(key >= searchNode.keys.get(i) && key < searchNode.keys.get(i+1)) {
                        return findDataNode(searchNode.children.get(i+1), key);
                    }
                }
            }
            return null;
        }
    }

    public static StringBuilder returnSearchStrings(ArrayList<Double> valueList){
        StringBuilder builder = new StringBuilder();
        int count = 0;
        while(valueList.size() > count){
            if(count != valueList.size()-1){
                builder.append(valueList.get(count)+ ", ");
            } else{
                builder.append(valueList.get(count));

            }
            count++;
        }
        return builder;
    }
    /**
     * Find the position to do the insertion of a new element
     */
    public static int getNodePositionToInsert(NodeCollection collection, SearchNode sNode) {
        int count = 0;
        for (int i = 0; i < sNode.keys.size(); i++) {
            if (collection.getKey() < sNode.keys.get(count)) {
                break;
            }
            count++;
        }
        return count;
    }

    /**
     * Method to set the parent of a child
     */
    public static void setParent(BPlusNode child, SearchNode parent) {
        child.parent = parent;
        int i = 0;
        while(i < parent.children.size()){
            if (parent.children.get(i).equals(child)) {
                child.parentIndex = i;
                break;
            }

            i++;
        }
    }
}
