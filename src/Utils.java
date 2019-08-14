
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
public class Utils {

	public static void bulkInsert(BPlusTreeInstance b, int[] tests) {
		for (int i = 0; i < tests.length; i++) {
			b.insert(tests[i], (double)tests[i]);
		}

	}
	public static void printTree(BPlusTreeInstance tree) {
		LinkedBlockingQueue<BPlusNode> queue;

		queue = new LinkedBlockingQueue<BPlusNode>();
		String result = "";

		int nodesInCurrentLevel = 1;
		int nodesInNextLevel = 0;
		ArrayList<Integer> childrenPerIndex = new ArrayList<Integer>();
		queue.add(tree.getRootNode());
		System.out.println(queue.peek().keys);
		while (!queue.isEmpty()) {
			BPlusNode target = queue.poll();
			nodesInCurrentLevel--;
			if (target instanceof DataNode) {
				DataNode leaf = (DataNode) target;
				result += "[";
				for (int i = 0; i < leaf.keys.size(); i++) {
					result += "(" + leaf.keys.get(i) + " , "
							+ leaf.values.get(i) + ");";
				}
                                if(childrenPerIndex.size()>0){
				childrenPerIndex.set(0, childrenPerIndex.get(0) - 1);
				if (childrenPerIndex.get(0) == 0) {
					result += "] $ ";
					childrenPerIndex.remove(0);
				} else {
					result += "] # ";
				}
                        }else{
                                   result += "] # "; 
                                }
			} else {
				SearchNode index = ((SearchNode) target);
				result += "@ ";
				for (int i = 0; i < index.keys.size(); i++) {
					result += "" + index.keys.get(i) + "/";
				}
				result += "@   ";
				queue.addAll(index.children);
				if (index.children.get(0) instanceof DataNode) {
					childrenPerIndex.add(index.children.size());
				}
				nodesInNextLevel += index.children.size();
			}

			if (nodesInCurrentLevel == 0) {
				result += "\n";
				nodesInCurrentLevel = nodesInNextLevel;
				nodesInNextLevel = 0;
			}

		}
		System.out.println(result);

	}
}
