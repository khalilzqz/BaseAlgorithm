package RelationShip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//http://blog.csdn.net/cq1982/article/details/44622769
public class FPTree {
	/** Ƶ��ģʽ����С֧���� **/
	private int minSuport;
	/** �����������С���Ŷ� **/
	private double confident;
	/** ����������� **/
	private int totalSize;
	/** �洢ÿ��Ƶ������Ӧ�ļ��� **/
	private Map<List<String>, Integer> frequentMap = new HashMap<List<String>, Integer>();
	/** ���������У���Щ�����Ϊ���Ƶ��Ľ����Ĭ������������������Ϊ���Ƶ��Ľ�� **/
	private Set<String> decideAttr = null;

	public int getMinSuport() {
		return this.minSuport;
	}

	/**
	 * ������С֧����
	 * 
	 * @param minSuport
	 */
	public void setMinSuport(int minSuport) {
		this.minSuport = minSuport;
	}

	public double getConfident() {
		return confident;
	}

	/**
	 * ������С���Ŷ�
	 * 
	 * @param confident
	 */
	public void setConfident(double confident) {
		this.confident = confident;
	}

	/**
	 * ���þ������ԡ����Ҫ����{@linkplain #readTransRocords(String[])}����Ҫ�ڵ���
	 * {@code readTransRocords}֮���ٵ���{@code setDecideAttr}
	 * 
	 * @param decideAttr
	 */
	public void setDecideAttr(Set<String> decideAttr) {
		this.decideAttr = decideAttr;
	}

	/**
	 * ��ȡƵ���
	 * 
	 * @return
	 * @Description:
	 */
	public Map<List<String>, Integer> getFrequentItems() {
		return frequentMap;
	}

	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * ����һ��Ƶ��ģʽ�õ����ɹ�������
	 * 
	 * @param list
	 * @return
	 */
	private List<StrongAssociationRule> getRules(List<String> list) {
		List<StrongAssociationRule> rect = new LinkedList<StrongAssociationRule>();
		if (list.size() > 1) {
			for (int i = 0; i < list.size(); i++) {
				String result = list.get(i);
				if (decideAttr.contains(result)) {
					List<String> condition = new ArrayList<String>();
					condition.addAll(list.subList(0, i));
					condition.addAll(list.subList(i + 1, list.size()));
					StrongAssociationRule rule = new StrongAssociationRule();
					rule.condition = condition;
					rule.result = result;
					rect.add(rule);
				}
			}
		}
		return rect;
	}

	/**
	 * �����ɸ��ļ��ж���Transaction Record��ͬʱ������������ΪdecideAttr
	 * 
	 * @param filenames
	 * @return
	 * @Description:
	 */
	public List<List<String>> readTransRocords(String[] filenames) {
		Set<String> set = new HashSet<String>();
		List<List<String>> transaction = null;
		if (filenames.length > 0) {
			transaction = new LinkedList<List<String>>();
			for (String filename : filenames) {
				try {
					FileReader fr = new FileReader(filename);
					BufferedReader br = new BufferedReader(fr);
					try {
						String line = null;
						// һ������ռһ��
						while ((line = br.readLine()) != null) {
							if (line.trim().length() > 0) {
								// ÿ��item֮����","�ָ�
								String[] str = line.split(",");
								// ÿһ�������е��ظ�����Ҫ����
								Set<String> record = new HashSet<String>();
								for (String w : str) {
									record.add(w);
									set.add(w);
								}
								List<String> rl = new ArrayList<String>();
								rl.addAll(record);
								transaction.add(rl);
							}
						}
					} finally {
						br.close();
					}
				} catch (IOException ex) {
					System.out.println("Read transaction records failed." + ex.getMessage());
					System.exit(1);
				}
			}
		}

		this.setDecideAttr(set);
		return transaction;
	}

	/**
	 * ����һ�����еĸ��������С�����������˳��ģ�
	 * 
	 * @param residualPath
	 * @param results
	 */
	private void combine(LinkedList<TreeNode> residualPath, List<List<TreeNode>> results) {
		if (residualPath.size() > 0) {
			// ���residualPath̫���������̫�����ϣ��ڴ�ᱻ�ľ���
			TreeNode head = residualPath.poll();
			List<List<TreeNode>> newResults = new ArrayList<List<TreeNode>>();
			for (List<TreeNode> list : results) {
				List<TreeNode> listCopy = new ArrayList<TreeNode>(list);
				newResults.add(listCopy);
			}

			for (List<TreeNode> newPath : newResults) {
				newPath.add(head);
			}
			results.addAll(newResults);
			List<TreeNode> list = new ArrayList<TreeNode>();
			list.add(head);
			results.add(list);
			combine(residualPath, results);
		}
	}

	private boolean isSingleBranch(TreeNode root) {
		boolean rect = true;
		while (root.getChildren() != null) {
			if (root.getChildren().size() > 1) {
				rect = false;
				break;
			}
			root = root.getChildren().get(0);
		}
		return rect;
	}

	/**
	 * ����������ÿһ���Ƶ��
	 * 
	 * @param transRecords
	 * @return
	 */
	private Map<String, Integer> getFrequency(List<List<String>> transRecords) {
		Map<String, Integer> rect = new HashMap<String, Integer>();
		for (List<String> record : transRecords) {
			for (String item : record) {
				Integer cnt = rect.get(item);
				if (cnt == null) {
					cnt = new Integer(0);
				}
				rect.put(item, ++cnt);
			}
		}
		return rect;
	}

	/**
	 * �������񼯺Ϲ���FPTree
	 * 
	 * @param transRecords
	 * @Description:
	 */
	public void buildFPTree(List<List<String>> transRecords) {
		totalSize = transRecords.size();
		// ����ÿ���Ƶ��
		final Map<String, Integer> freqMap = getFrequency(transRecords);
		// �Ȱ�Ƶ��1���ӵ�Ƶ��ģʽ��
		// for (Entry<String, Integer> entry : freqMap.entrySet()) {
		// String name = entry.getKey();
		// int cnt = entry.getValue();
		// if (cnt >= minSuport) {
		// List<String> rule = new ArrayList<String>();
		// rule.add(name);
		// frequentMap.put(rule, cnt);
		// }
		// }
		// ÿ�������е��F1����
		for (List<String> transRecord : transRecords) {
			Collections.sort(transRecord, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return freqMap.get(o2) - freqMap.get(o1);
				}
			});
		}
		FPGrowth(transRecords, null);
	}

	/**
	 * FP���ݹ��������Ӷ��õ����е�Ƶ��ģʽ
	 * 
	 * @param cpb
	 *            ����ģʽ��
	 * @param postModel
	 *            ��׺ģʽ
	 */
	private void FPGrowth(List<List<String>> cpb, LinkedList<String> postModel) {
		// System.out.println("CPB is");
		// for (List<String> records : cpb) {
		// System.out.println(records);
		// }
		// System.out.println("PostPattern is " + postPattern);

		Map<String, Integer> freqMap = getFrequency(cpb);
		Map<String, TreeNode> headers = new HashMap<String, TreeNode>();
		for (Entry<String, Integer> entry : freqMap.entrySet()) {
			String name = entry.getKey();
			int cnt = entry.getValue();
			// ÿһ�εݹ�ʱ���п��ܳ���һ����ģʽ��Ƶ��������ֵ
			if (cnt >= minSuport) {
				TreeNode node = new TreeNode(name);
				node.setCount(cnt);
				headers.put(name, node);
			}
		}

		TreeNode treeRoot = buildSubTree(cpb, freqMap, headers);
		// ���ֻʣ������ڵ㣬��ݹ����
		if ((treeRoot.getChildren() == null) || (treeRoot.getChildren().size() == 0)) {
			return;
		}

		// ������ǵ�֦�ģ���ֱ�Ӱѡ�·���ĸ������+��׺ģʽ����ӵ�Ƶ��ģʽ���С���������ǿ�ѡ�ģ��������˲�������һ�ֵݹ�Ҳ���Եõ���ȷ�Ľ��
		if (isSingleBranch(treeRoot)) {
			LinkedList<TreeNode> path = new LinkedList<TreeNode>();
			TreeNode currNode = treeRoot;
			while (currNode.getChildren() != null) {
				currNode = currNode.getChildren().get(0);
				path.add(currNode);
			}
			// ����combineʱpath���˹����������OutOfMemory
			if (path.size() <= 20) {
				List<List<TreeNode>> results = new ArrayList<List<TreeNode>>();
				combine(path, results);
				for (List<TreeNode> list : results) {
					int cnt = 0;
					List<String> rule = new ArrayList<String>();
					for (TreeNode node : list) {
						rule.add(node.getName());
						cnt = node.getCount();// cnt��FPTreeҶ�ڵ�ļ���
					}
					if (postModel != null) {
						rule.addAll(postModel);
					}
					frequentMap.put(rule, cnt);
				}
				return;
			} else {
				System.err.println("length of path is too long: " + path.size());
			}
		}

		for (TreeNode header : headers.values()) {
			List<String> rule = new ArrayList<String>();
			rule.add(header.getName());
			if (postModel != null) {
				rule.addAll(postModel);
			}
			// ��ͷ��+��׺ģʽ ����һ��Ƶ��ģʽ��Ƶ��ģʽ�ڲ�Ҳ�ǰ���F1����ģ���Ƶ����Ϊ��ͷ��ļ���
			frequentMap.put(rule, header.getCount());
			// �µĺ�׺ģʽ����ͷ��+��һ�εĺ�׺ģʽ��ע�Ᵽ��˳��ʼ�հ�F1��˳�����У�
			LinkedList<String> newPostPattern = new LinkedList<String>();
			newPostPattern.add(header.getName());
			if (postModel != null) {
				newPostPattern.addAll(postModel);
			}
			// �µ�����ģʽ��
			List<List<String>> newCPB = new LinkedList<List<String>>();
			TreeNode nextNode = header;
			while ((nextNode = nextNode.getNextHomonym()) != null) {
				int counter = nextNode.getCount();
				// ��ô�����ڵ㣨����������ڵ㣩����ǰ�ڵ㣨��������ǰ�ڵ㣩��·������һ������ģʽ����ע�Ᵽ��˳����ڵ���ǰ���ӽڵ��ں󣬼�ʼ�ձ���Ƶ�ʸߵ���ǰ
				LinkedList<String> path = new LinkedList<String>();
				TreeNode parent = nextNode;
				while ((parent = parent.getParent()).getName() != null) {// ����ڵ��nameΪnull
					path.push(parent.getName());// ����ͷ����
				}
				// ����Ҫ�ظ����counter��
				while (counter-- > 0) {
					newCPB.add(path);
				}
			}
			FPGrowth(newCPB, newPostPattern);
		}
	}

	/**
	 * ������������뵽һ��FP������
	 * 
	 * @param transRecords
	 * @param F1
	 * @return
	 */
	private TreeNode buildSubTree(List<List<String>> transRecords, final Map<String, Integer> freqMap,
			final Map<String, TreeNode> headers) {
		TreeNode root = new TreeNode();// ����ڵ�
		for (List<String> transRecord : transRecords) {
			LinkedList<String> record = new LinkedList<String>(transRecord);
			TreeNode subTreeRoot = root;
			TreeNode tmpRoot = null;
			if (root.getChildren() != null) {
				// �����еķ�֧������ڵ������1
				while (!record.isEmpty() && (tmpRoot = subTreeRoot.findChild(record.peek())) != null) {
					tmpRoot.countIncrement(1);
					subTreeRoot = tmpRoot;
					record.poll();
				}
			}
			// �����µĽڵ�
			addNodes(subTreeRoot, record, headers);
		}
		return root;
	}

	/**
	 * ���ض��Ľڵ��²���һ������ڵ㣬ͬʱά����ͷ�ͬ���ڵ������ָ��
	 * 
	 * @param ancestor
	 * @param record
	 * @param headers
	 */
	private void addNodes(TreeNode ancestor, LinkedList<String> record, final Map<String, TreeNode> headers) {
		while (!record.isEmpty()) {
			String item = (String) record.poll();
			// ������ĳ���Ƶ�����������С֧�����������������FP�����ﵽ��С֧�ֶȵ����headers�С�ÿһ�εݹ��������ģʽ���������µ�FPTreeʱ����Ҫ��Ƶ������minSuport���ų����⣬��Ҳ����FPTree����ٷ��������ԭ��
			if (headers.containsKey(item)) {
				TreeNode leafnode = new TreeNode(item);
				leafnode.setCount(1);
				leafnode.setParent(ancestor);
				ancestor.addChild(leafnode);

				TreeNode header = headers.get(item);
				TreeNode tail = header.getTail();
				if (tail != null) {
					tail.setNextHomonym(leafnode);
				} else {
					header.setNextHomonym(leafnode);
				}
				header.setTail(leafnode);
				addNodes(leafnode, record, headers);
			}
			// else {
			// System.err.println(item + " is not F1");
			// }
		}
	}

	/**
	 * ��ȡ���е�ǿ����
	 * 
	 * @return
	 */
	public List<StrongAssociationRule> getAssociateRule() {
		assert totalSize > 0;
		List<StrongAssociationRule> rect = new ArrayList<StrongAssociationRule>();
		// ��������Ƶ��ģʽ
		for (Entry<List<String>, Integer> entry : frequentMap.entrySet()) {
			List<String> items = entry.getKey();
			int count1 = entry.getValue();
			// һ��Ƶ��ģʽ�������ɺܶ��������
			List<StrongAssociationRule> rules = getRules(items);
			// ����ÿһ�����������֧�ֶȺ����Ŷ�
			for (StrongAssociationRule rule : rules) {
				if (frequentMap.containsKey(rule.condition)) {
					int count2 = frequentMap.get(rule.condition);
					double confidence = 1.0 * count1 / count2;
					if (confidence >= this.confident) {
						rule.support = count1;
						rule.confidence = confidence;
						rect.add(rule);
					}
				} else {
					System.err.println(
							rule.condition + " is not a frequent pattern, however " + items + " is a frequent pattern");
				}
			}
		}
		return rect;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		String infile = "D:/aprioritest.txt";
		FPTree fpTree = new FPTree();
		fpTree.setConfident(0.6);
		fpTree.setMinSuport(3);
		if (args.length >= 2) {
			double confidence = Double.parseDouble(args[0]);
			int suport = Integer.parseInt(args[1]);
			fpTree.setConfident(confidence);
			fpTree.setMinSuport(suport);
		}

		List<List<String>> trans = fpTree.readTransRocords(new String[] { infile });
		Set<String> decideAttr = new HashSet<String>();
		//
		decideAttr.add("7");
		decideAttr.add("15");
		decideAttr.add("23");
		decideAttr.add("24");
		fpTree.setDecideAttr(decideAttr);
		long begin = System.currentTimeMillis();
		fpTree.buildFPTree(trans);
		long end = System.currentTimeMillis();
		System.out.println("buildFPTree use time " + (end - begin));
		Map<List<String>, Integer> pattens = fpTree.getFrequentItems();

		String outfile = "D:/pattens.txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
		System.out.println("ģʽ\tƵ��");
		bw.write("ģʽ\tƵ��");
		bw.newLine();
		for (Entry<List<String>, Integer> entry : pattens.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
			bw.write(joinList(entry.getKey()) + "\t" + entry.getValue());
			bw.newLine();
		}
		bw.close();
		System.out.println();
		List<StrongAssociationRule> rules = fpTree.getAssociateRule();

		String outfile1 = "D:/rule.txt";
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(outfile1));
		bw1 = new BufferedWriter(new FileWriter(outfile1));
		System.out.println("����\t���\t֧�ֶ�\t���Ŷ�");
		bw1.write("����\t���\t֧�ֶ�\t���Ŷ�");
		bw1.newLine();
		DecimalFormat dfm = new DecimalFormat("#.##");
		for (StrongAssociationRule rule : rules) {
			System.out.println(rule.condition + "->" + rule.result + "\t" + dfm.format(rule.support) + "\t"
					+ dfm.format(rule.confidence));
			bw1.write(rule.condition + "->" + rule.result + "\t" + dfm.format(rule.support) + "\t"
					+ dfm.format(rule.confidence));
			bw1.newLine();
		}
		bw1.close();

	}

	private static String joinList(List<String> list) {
		if (list == null || list.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String ele : list) {
			sb.append(ele);
			sb.append(",");
		}
		// �����һ������ȥ��
		return sb.substring(0, sb.length() - 1);
	}
}
