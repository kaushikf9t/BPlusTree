import java.util.ArrayList;

/**
 * The class which acts as search nodes in BPlus Tree which routes the search to the appropriate data node
 */
public class SearchNode extends BPlusNode {
    protected ArrayList<BPlusNode> children;

    public SearchNode(int key, BPlusNode leftChild, BPlusNode rightChild) {
        keys = new ArrayList<Integer>();
        keys.add(key);
        children = new ArrayList<BPlusNode>();
        children.add(leftChild);
        children.add(rightChild);
    }

    public SearchNode(){
        keys = new ArrayList<Integer>();
        children = new ArrayList<BPlusNode>();
    }

    /**
     * Insertion into Search Node
     */
    public void insertToSearchNode(NodeCollection obj, int searchIndex) {
        int key = obj.getKey();
        BPlusNode child = obj.getNode();

        if (searchIndex >= keys.size()) {
            keys.add(key);
            children.add(child);
        } else {
            keys.add(searchIndex, key);
            children.add(searchIndex +1, child);
        }
    }


}
