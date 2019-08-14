/**
 * Builder for the BTPlus Tree entity
 */
public class BPlusTreeBuilder {
    private int order;
    private int degree;
    private BPlusNode root;

    public BPlusTreeBuilder setOrder(int order) {
        this.order = order;
        return this;
    }

    public BPlusTreeBuilder setDegree(int degree) {
        this.degree = degree;
        return this;
    }

    public BPlusTreeBuilder setRoot(BPlusNode root) {
        this.root = root;
        return this;
    }

    public BPlusTreeInstance createBPlusTree() {
        return new BPlusTreeInstance(order, degree, root);
    }
}