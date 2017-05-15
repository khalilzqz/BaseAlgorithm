package ResultEvaluation;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Test {
	public static void main(String[] args) {
		int[] A = { 1, 3, 3, 3, 3, 3, 3, 2, 1, 0, 2, 0, 2, 0, 2, 1, 1, 0, 1, 1 };
		int[] B = { 2, 2, 0, 0, 0, 3, 2, 2, 3, 1, 3, 1, 0, 1, 2, 1, 0, 1, 3, 3 };
		ResultEvaluation t = new ResultEvaluation();
		Map<Integer, Set<Integer>> clusterA = t.clusterDistri(A);
		Map<Integer, Set<Integer>> clusterB = t.clusterDistri(B);
		for (Map.Entry<Integer, Set<Integer>> entry : clusterA.entrySet()) {
			System.out.println(entry.getKey() + "|" + entry.getValue());
		}
		int[] c = t.commNum(clusterA, clusterA);

	}
}
