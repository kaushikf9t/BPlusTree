import java.util.*;

/**
 * Main BPlus Tree classes which has the logic for all operations
 */
public class BPlusTreeInstance {


    public int getTreeOrder() {
        return treeOrder;
    }

    public int getNodeDegree() {
        return nodeDegree;
    }

    public BPlusNode getRootNode() {
        return rootNode;
    }

    private int treeOrder;
    private int nodeDegree;
    private BPlusNode rootNode = null;

    public BPlusTreeInstance(int order, int degree, BPlusNode root) {
        treeOrder = order;
        nodeDegree = degree;
        rootNode = root;
    }

    /**
     * If the root is empty, insert into a new node
     * If Root is the only node in the tree, insert it into that, and split, if necessary
     */

    public void insert(int key, Double value) {
        DataNode dataNode = new DataNode(key, value);
        NodeCollection collection = new NodeCollection(key, dataNode);

        //Handle the case of an empty tree
        insertIntoEmptyTree(collection);

        NodeCollection splitNode = performInsertRecursively(rootNode, collection, null);

        //Handle the cases of overflow of data and consequently the search node
        if (splitNode == null) {
            return;
        } else {
            SearchNode newRoot = new SearchNode(splitNode.getKey(), rootNode,
                    splitNode.getNode());
            rootNode = newRoot;
            return;
        }
    }

    /**
     * Insert into empty node, make the only node the root
     */
    private void insertIntoEmptyTree(NodeCollection entry){
        if (rootNode == null || rootNode.keys.isEmpty()) {
            rootNode = entry.getNode();
        }
    }

    //
    private NodeCollection performInsertRecursively(BPlusNode node, NodeCollection nodeCollection, NodeCollection splitChild) {
        if(node instanceof DataNode){
            DataNode dNode = (DataNode) node;
            DataNode newDataNode = (DataNode) nodeCollection.getNode();

            dNode.insertToDataNode(nodeCollection.getKey(), newDataNode.values.get(0));

            if (BPlusUtil.isNodeFull(dNode, this)) {
                splitChild = NodeProcessingUtility.BPlusTreeSplit(this, dNode);
                //If the root is a data node and gets split
                if (dNode == rootNode) {
                    //Create New Root
                    createNewRoot(splitChild, dNode);
                    return null;
                }
                return splitChild;
            }

            else {
                return null;
            }
        } else {
            SearchNode sNode = (SearchNode) node;
            splitChild = performInsertRecursively(sNode.children.get(BPlusUtil.getNodePositionToInsert(nodeCollection, sNode)), nodeCollection, splitChild);

            if (splitChild != null) {
                sNode.insertToSearchNode(splitChild, BPlusUtil.getNodePositionToInsert(nodeCollection, sNode));

                if (!BPlusUtil.isNodeFull(sNode, this)) {
                    return null;

                } else {
                    splitChild = NodeProcessingUtility.BTreeSplit(this,sNode);

                    if (sNode == rootNode) {
                        createNewRoot(splitChild, rootNode);
                        return null;
                    }
                    return splitChild;
                }
            } else {
                return null;
            }
        }
    }

    private void createNewRoot(NodeCollection splitChild, BPlusNode node) {
        SearchNode newRoot = new SearchNode(splitChild.getKey(), node, splitChild.getNode());
        rootNode = newRoot;
    }

    /**
     * Range Search - return the values in between a given range of keys
     */
    public ArrayList<Double> searchBetweenKeys(int start, int end) {
        ArrayList<Double> valuesToReturn = new ArrayList<>();
        DataNode dNode = (DataNode)BPlusUtil.findDataNode(rootNode, start);

        int count = 0;
        //Go after the keys of the current
        while(count < dNode.keys.size()){
            if(start <= dNode.keys.get(count)) {
                while(count < dNode.keys.size() && end >= dNode.keys.get(count)) {
                    valuesToReturn.add(dNode.values.get(count));
                    count++;
                    //Keep going right until the right most leaf is encountered
                    if(count == dNode.keys.size() && end >= dNode.keys.get(count-1)) {
                        if(dNode.rightSibling != null){
                            dNode = dNode.rightSibling;
                            count = 0;

                        } else {
                            break;
                        }
                    }
                }
            }
            count++;
        }

        return valuesToReturn;
    }

    /**
     * Delete operation for BPlus Tree
     */
    public void delete(int key) {
        if (rootNode == null)
            return;

        int splitIndex = performDeleteRecursively(key, rootNode, null, -1);
        if (splitIndex != -1) {
            rootNode.keys.remove(splitIndex);
            if (rootNode.keys.isEmpty()) {
                rootNode = ((SearchNode) rootNode).children.get(0);
            }
        }
        // if the new root is also empty, then the entire tree must be empty
        if (rootNode.keys.isEmpty()) {
            rootNode = null;
        }
    }

    /**
     * Method to handle deletions recursively handling overflow and underflow scenarios
     */
    private int performDeleteRecursively(int key, BPlusNode child, SearchNode parent, int splitKey) {
        if (parent != null) {
            BPlusUtil.setParent(child, parent);
        }

        // If child is a leaf, delete the key value pair from it
        if (child instanceof DataNode) {
            DataNode childDNode = (DataNode) child;
            int i = 0;
            while(i<childDNode.keys.size()){
                if (key == childDNode.keys.get(i)) {
                    childDNode.keys.remove(i);
                    childDNode.values.remove(i);
                    break;
                }
                i++;
            }

            // Handle leaf underflow
            if (BPlusUtil.isNodeDeficient(childDNode, this) && childDNode != rootNode) {
                if (childDNode.getIndexInParent() == 0) {
                    return NodeProcessingUtility.processDataNodeUnderflow(this,childDNode, childDNode.rightSibling, childDNode.getParent());
                } else {
                    return NodeProcessingUtility.processDataNodeUnderflow(this, childDNode.leftSibling, childDNode, childDNode.getParent());
                }
            }

        } else {
            SearchNode childSNode = (SearchNode) child;

            if (key < childSNode.keys.get(0)) {
                splitKey = performDeleteRecursively(key, childSNode.children.get(0), childSNode, splitKey);
            } else if (key >= childSNode.keys.get(childSNode.keys.size() - 1)) {
                splitKey = performDeleteRecursively(key, childSNode.children.get(childSNode.children.size() - 1), childSNode, splitKey);
            } else {
                for (int i = 1; i < childSNode.keys.size(); i++) {
                    if (childSNode.keys.get(i).compareTo(key) > 0) {
                        splitKey = performDeleteRecursively(key, childSNode.children.get(i), childSNode, splitKey);
                        break;
                    }
                }
            }
        }
        // delete split key and handle overflow
        if (splitKey != -1 && child != rootNode) {
            child.keys.remove(splitKey);
            if (BPlusUtil.isNodeDeficient(child, this)) {
                if (child.getIndexInParent() == 0) {
                    SearchNode rightSibling = (SearchNode) child.getParent().children.get(child.getIndexInParent() + 1);
                    splitKey = NodeProcessingUtility.processSearchNodeUnderflow(this, (SearchNode) child, rightSibling, child.getParent());
                } else {
                    SearchNode leftSibling = (SearchNode) child.getParent().children.get(child.getIndexInParent() - 1);
                    splitKey = NodeProcessingUtility.processSearchNodeUnderflow(this, leftSibling, (SearchNode) child, child.getParent());
                }
            }
            else splitKey = -1;
        }

        return splitKey;
    }



}
