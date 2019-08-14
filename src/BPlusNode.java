import java.util.ArrayList;

/**
 * A class for BPlus Tree Node
 */
    public class BPlusNode {
        protected ArrayList<Integer> keys;
        protected SearchNode parent;
        protected int parentIndex;


        public SearchNode getParent() {
            return this.parent;
        }

        public int getIndexInParent() {
            return this.parentIndex;
        }
    }

