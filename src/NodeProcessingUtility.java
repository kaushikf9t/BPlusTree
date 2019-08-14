import java.util.ArrayList;

/**
 * Utility that houses split logic for BTree split in case of search nodes and BPlus tree split in case of Data Nodes
 */
public class NodeProcessingUtility {

    //Data/leaf Nodes should be split by the BPlus Tree split when a node overflows
    public static NodeCollection BPlusTreeSplit(BPlusTreeInstance bPlusTreeInstance, DataNode dNode) {
        DataNode newNode = new DataNode();
        int center = (int)Math.ceil(bPlusTreeInstance.getNodeDegree()/2f);

        for (int i = center; i <= bPlusTreeInstance.getNodeDegree(); i++) {
            newNode.keys.add(dNode.keys.get(i));
            newNode.values.add(dNode.values.get(i));
        }

        for (int i = bPlusTreeInstance.getNodeDegree(); i >= center; i--) {
            dNode.keys.remove(i);
            dNode.values.remove(i);
        }

        // Sibling pointers are set
        DataNode tmp = dNode.rightSibling;
        dNode.rightSibling = newNode;
        dNode.rightSibling.leftSibling = newNode;
        newNode.leftSibling = dNode;
        newNode.rightSibling = tmp;

        Integer splitKey = newNode.keys.get(0);
        NodeCollection newObj = new NodeCollection(splitKey, newNode);

        return newObj;
    }


    /**
     * Search Nodes should be split using BTree split in overflow scenarios
     */
    public static NodeCollection BTreeSplit(BPlusTreeInstance bPlusTreeInstance, SearchNode sNode) {
        SearchNode newSNode = new SearchNode();
        int center = (int)Math.ceil(bPlusTreeInstance.getNodeDegree()/2f);
        int splitKey = sNode.keys.get(center);
        sNode.keys.remove(center);

        newSNode.children.add(sNode.children.get(center + 1));
        sNode.children.remove(center + 1);

        while(sNode.keys.size() > center) {
            newSNode.keys.add(sNode.keys.get(center));
            sNode.keys.remove(center);
            newSNode.children.add(sNode.children.get(center+1));
            sNode.children.remove(center + 1);
        }

        NodeCollection newObj = new NodeCollection(splitKey, newSNode);

        return newObj;
    }

    /**
     * Method to handle the underflow scenario in the Leaf Node through the borrow from siblings
     */
    public static int processDataNodeUnderflow(BPlusTreeInstance bPlusTreeInstance, DataNode leftSibling, DataNode rightSibling, SearchNode parent) {

        int totalKeys = leftSibling.keys.size() + rightSibling.keys.size();
        if (totalKeys >= bPlusTreeInstance.getNodeDegree()) {

            int childIndex = parent.children.indexOf(rightSibling);

            // Store all keys and values from leftSibling to rightSibling
            ArrayList<Integer> keys = new ArrayList<>();
            ArrayList<Double> values = new ArrayList <>();

            keys.addAll(leftSibling.keys);
            keys.addAll(rightSibling.keys);

            values.addAll(leftSibling.values);
            values.addAll(rightSibling.values);

            int leftSize = totalKeys/2;

            leftSibling.keys.clear();
            rightSibling.keys.clear();
            leftSibling.values.clear();
            rightSibling.values.clear();

            // Add first half keys and values into leftSibling and rest into rightSibling
            leftSibling.keys.addAll(keys.subList(0, leftSize));
            leftSibling.values.addAll(values.subList(0, leftSize));
            rightSibling.keys.addAll(keys.subList(leftSize, keys.size()));
            rightSibling.values.addAll(values.subList(leftSize, values.size()));

            parent.keys.set(childIndex - 1, parent.children.get(childIndex).keys.get(0));
            return -1;
        }
        else {
            // remove rightSibling child
            leftSibling.keys.addAll(rightSibling.keys);
            leftSibling.values.addAll(rightSibling.values);

            leftSibling.rightSibling = rightSibling.rightSibling;
            if (rightSibling.rightSibling != null) {
                rightSibling.rightSibling.leftSibling = leftSibling;
            }

            int index = parent.children.indexOf(rightSibling) - 1;
            parent.children.remove(rightSibling);

            return index;
        }
    }

    /**
     * Process search node underflow in case the node overflows in search nodes like BTree deletes
     */
    public static int processSearchNodeUnderflow(BPlusTreeInstance bplusTreeInstance, SearchNode left, SearchNode right, SearchNode parent) {

        int splitIndex = -1;
        for (int i = 0; i < parent.keys.size(); i++) {
            if (parent.children.get(i) == left && parent.children.get(i + 1) == right) {
                splitIndex = i;
                break;
            }
        }

        if ((left.keys.size() + right.keys.size()) >= (bplusTreeInstance.getNodeDegree())) {
            // All keys including the parent key
            ArrayList<Integer> keys = new ArrayList<>();
            ArrayList<BPlusNode> children = new ArrayList<BPlusNode>();
            keys.addAll(left.keys);
            keys.add(parent.keys.get(splitIndex));
            keys.addAll(right.keys);
            children.addAll(left.children);
            children.addAll(right.children);

            // Get the index of the new parent key
            int newIndex = keys.size() / 2;
            if (keys.size() % 2 == 0) {
                newIndex -= 1;
            }
            parent.keys.set(splitIndex, keys.get(newIndex));

            left.keys.clear();
            right.keys.clear();
            left.children.clear();
            right.children.clear();

            left.keys.addAll(keys.subList(0, newIndex));
            right.keys.addAll(keys.subList(newIndex + 1, keys.size()));
            left.children.addAll(children.subList(0, newIndex + 1));
            right.children.addAll(children.subList(newIndex + 1, children.size()));

            return -1;
        }
        else {
            left.keys.add(parent.keys.get(splitIndex));
            left.keys.addAll(right.keys);
            left.children.addAll(right.children);

            parent.children.remove(parent.children.indexOf(right));
            return splitIndex;
        }
    }


}
