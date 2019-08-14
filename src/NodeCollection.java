/**
 * Class to hold the Key and the Node pairs
 */
public class NodeCollection {
    int key;
    BPlusNode node;

    NodeCollection(int key, BPlusNode node) {
        this.key = key;
        this.node = node;
    }

    public int getKey() {
        return this.key;
    }

    public BPlusNode getNode() {
        return this.node;
    }
}
