package RelationShip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Apriori {

	private float min_sup;// minimum support
	private float min_conf;// minimum confidence

	private Map<Integer, Set<String>> TransDataBase;// transaction database
	private Integer DBnum;

	private Map<Integer, Map<Set<String>, Float>> freqItermSet;// frequent iterm
																// set,from 1 to
																// k...
	private Map<Set<String>, Set<Map<Set<String>, Float>>> associationRules;// the
																			// final
																			// associate
																			// rules

	public Apriori(Map<Integer, Set<String>> DB, float minSup, float minConf) {
		this.TransDataBase = DB;
		this.min_conf = minConf;
		this.min_sup = minSup;
		this.DBnum = DB.size();
		freqItermSet = new HashMap<Integer, Map<Set<String>, Float>>();
		associationRules = new HashMap<Set<String>, Set<Map<Set<String>, Float>>>();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// initial

		String file = "D:/aprioritest.txt";
		String delimeter = ",";

		// load data
		Map<Integer, Set<String>> data = new HashMap<Integer, Set<String>>();

		int num = 0;
		try {
			File mFile = new File(file);
			FileReader fr = new FileReader(mFile);
			BufferedReader br = new BufferedReader(fr);
			String line;

			while ((line = br.readLine()) != null) {
				line = line.trim();
				String[] str = line.split(delimeter);
				// int i = Integer.parseInt(str[0]);
				Set<String> set = new HashSet<String>();

				for (int i = 1; i < str.length; i++) {
					set.add(str[i].trim());
				}
				num++;
				data.put(num, set);

			}
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Apriori ap = new Apriori(data, 0.005f, 0.6f);
		ap.findAllFreqItermSet();
		ap.findAssociationRules();
	}

	public void findAllFreqItermSet() {

		// 找出频繁一项集

		Map<Set<String>, Float> f1 = new HashMap<Set<String>, Float>();
		Map<Set<String>, Integer> OneItermSet = new HashMap<Set<String>, Integer>();
		Iterator<Map.Entry<Integer, Set<String>>> it = this.TransDataBase.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<Integer, Set<String>> entry = it.next();
			Set<String> itermSet = entry.getValue();
			for (String iterm : itermSet) {
				Set<String> key = new HashSet<String>();
				key.add(iterm);
				if (!OneItermSet.containsKey(key)) {
					OneItermSet.put(key, 1);
				} else {
					int value = 1 + OneItermSet.get(key);
					OneItermSet.put(key, value);
				}
			}
		}
		// 找出支持度大于minSup的频繁一项集
		Iterator<Map.Entry<Set<String>, Integer>> iter = OneItermSet.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Set<String>, Integer> entry = iter.next();
			// 计算支持度
			Float support = new Float(entry.getValue().toString()) / new Float(this.DBnum);
			if (support >= this.min_sup) {
				f1.put(entry.getKey(), support);
			}
		}

		System.out.println("频繁1" + "项集:" + f1);// 打印频繁1-项集
		System.out.println("-------------------------------------------");
		this.freqItermSet.put(1, f1);

		// 由频繁k项集得到频繁k+1项集
		int k = 2;
		while (true) {

			Set<Set<String>> candFreItermSets = this.apriori_gen(k, this.freqItermSet.get(k - 1).keySet());
			Map<Set<String>, Float> freqKItermSetMap = this.getFreqKItermSet(k, candFreItermSets);
			if (!freqKItermSetMap.isEmpty()) {
				this.freqItermSet.put(k, freqKItermSetMap);
			} else {
				break;
			}
			System.out.println("频繁" + k + "项集：" + freqKItermSetMap);
			System.out.println("-------------------------------------------");
			k++;

		}

	}

	public Map<Set<String>, Float> getFreqKItermSet(int k, Set<Set<String>> candFreItermSets) {
		Map<Set<String>, Integer> candFreqKItermSetMap = new HashMap<Set<String>, Integer>();

		// 扫描事物数据库
		Iterator<Map.Entry<Integer, Set<String>>> it = this.TransDataBase.entrySet().iterator();
		// 统计支持度计数
		while (it.hasNext()) {
			Map.Entry<Integer, Set<String>> entry = it.next();
			Iterator<Set<String>> iter = candFreItermSets.iterator();
			while (iter.hasNext()) {
				Set<String> set = iter.next();
				if (entry.getValue().containsAll(set)) {
					if (!candFreqKItermSetMap.containsKey(set)) {
						candFreqKItermSetMap.put(set, 1);
					} else {
						int value = 1 + candFreqKItermSetMap.get(set);
						candFreqKItermSetMap.put(set, value);
					}
				}
			}
		}

		Iterator<Map.Entry<Set<String>, Integer>> iter = candFreqKItermSetMap.entrySet().iterator();
		Map<Set<String>, Float> freqKIntermSet = new HashMap<Set<String>, Float>();
		while (iter.hasNext()) {
			Map.Entry<Set<String>, Integer> entry = iter.next();
			float support = new Float(entry.getValue().toString()) / this.DBnum;
			if (support < this.min_sup) {
				iter.remove();
			} else {
				freqKIntermSet.put(entry.getKey(), support);
			}
		}

		return freqKIntermSet;
	}

	public Set<Set<String>> apriori_gen(int k, Set<Set<String>> fk) {
		Set<Set<String>> ck = new HashSet<Set<String>>();
		Iterator<Set<String>> it1 = fk.iterator();
		while (it1.hasNext()) {
			Set<String> itermSet1 = it1.next();
			Iterator<Set<String>> it2 = fk.iterator();
			while (it2.hasNext()) {
				Set<String> itermSet2 = it2.next();
				if (!itermSet1.equals(itermSet2)) {
					// 连接步
					Set<String> commIterms = new HashSet<String>();
					commIterms.addAll(itermSet1);
					commIterms.retainAll(itermSet2);
					if (commIterms.size() == (k - 2)) {
						Set<String> candIterms = new HashSet<String>();
						candIterms.addAll(itermSet1);
						candIterms.removeAll(itermSet2);
						candIterms.addAll(itermSet2);
						// 剪枝步骤
						if (!this.has_infrequent_subset(candIterms, fk)) {
							ck.add(candIterms);
						}
					}
				}
			}
		}
		System.out.println(ck.size());
		return ck;
	}

	public boolean has_infrequent_subset(Set<String> set, Set<Set<String>> fk) {
		Set<Set<String>> subItermSet = new HashSet<Set<String>>();
		Iterator<String> itr = set.iterator();
		while (itr.hasNext()) {
			// 深拷贝
			Set<String> subItem = new HashSet<String>();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				subItem.add(it.next());
			}

			// 去掉一个项后为k-1子集
			subItem.remove(itr.next());
			subItermSet.add(subItem);
		}

		Iterator<Set<String>> it = subItermSet.iterator();
		while (it.hasNext()) {
			if (!fk.contains(it.next())) {
				return true;
			}
		}
		return false;
	}

	public void findAssociationRulesTemp() {

	}

	public void findAssociationRules() {
		Iterator<Map.Entry<Integer, Map<Set<String>, Float>>> it = this.freqItermSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Map<Set<String>, Float>> entry = it.next();
			for (Set<String> itemSet : entry.getValue().keySet()) {
				int n = itemSet.size() / 2;
				for (int i = 1; i <= n; i++) {
					Set<Set<String>> subset = this.getProperSubset(i, itemSet);

					for (Set<String> conditionSet : subset) {
						Set<String> conclusionSet = new HashSet<String>();
						conclusionSet.addAll(itemSet);
						conclusionSet.removeAll(conditionSet);
						int s1 = conditionSet.size();
						int s2 = conclusionSet.size();

						float supF = this.freqItermSet.get(s1).get(conditionSet);
						float supS = this.freqItermSet.get(s2).get(conclusionSet);
						float sup = this.freqItermSet.get(s1 + s2).get(itemSet);

						float conf1 = sup / supF;
						float conf2 = sup / supS;

						if (conf1 >= this.min_conf) {
							if (this.associationRules.get(conditionSet) == null) {
								Set<Map<Set<String>, Float>> conclusionSetSet = new HashSet<Map<Set<String>, Float>>();
								Map<Set<String>, Float> sets = new HashMap<Set<String>, Float>();
								sets.put(conclusionSet, conf1);
								conclusionSetSet.add(sets);

								this.associationRules.put(conditionSet, conclusionSetSet);

							} else {
								Map<Set<String>, Float> sets = new HashMap<Set<String>, Float>();
								sets.put(conclusionSet, conf1);
								associationRules.get(conditionSet).add(sets);
							}
						}
						if (conf2 >= this.min_conf) {
							if (this.associationRules.get(conditionSet) == null) {
								Set<Map<Set<String>, Float>> conclusionSetSet = new HashSet<Map<Set<String>, Float>>();
								Map<Set<String>, Float> sets = new HashMap<Set<String>, Float>();
								sets.put(conclusionSet, conf2);
								conclusionSetSet.add(sets);

								this.associationRules.put(conditionSet, conclusionSetSet);

							} else {
								Map<Set<String>, Float> sets = new HashMap<Set<String>, Float>();
								sets.put(conclusionSet, conf2);
								associationRules.get(conditionSet).add(sets);
							}
						}
					}
				}

			}

		}
		System.out.println("关联规则（强规则）：" + associationRules);
	}

	private Set<Set<String>> getProperSubset(int i, Set<String> itemSet) {

		Set<Set<String>> subset = new HashSet<Set<String>>();
		if (itemSet.size() <= 1) {
			return null;
		} else if (itemSet.size() == 2) {
			for (String s : itemSet) {
				Set<String> set = new HashSet<String>();
				set.add(s);
				if (!subset.contains(s)) {
					subset.add(set);
				}

			}
			return subset;
		} else {

			Iterator<String> it = itemSet.iterator();
			String s = it.next();
			Set<String> temp = new HashSet<String>(itemSet);
			temp.remove(s);
			// 包含s的子集
			Set<Set<String>> subset0 = new HashSet<Set<String>>();
			subset0 = this.getProperSubset(i - 1, temp);
			subset.addAll(subset0);
			// 不包含s的子集
			Set<Set<String>> subset1 = new HashSet<Set<String>>();

			subset1 = this.getProperSubset(i, temp);
			subset.addAll(subset1);
			return subset;

		}

	}

}
