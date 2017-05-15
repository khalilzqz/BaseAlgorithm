package ResultEvaluation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.rmi.CORBA.Util;

/*function:���õľ�������ָ����purity, precision, recall��  RI �� F-score,jaccard  */  

public class ResultEvaluation {
	public static void main(String[] args) {
		int[] A = { 1, 3, 3, 3, 3, 3, 3, 2, 1, 0, 2, 0, 2, 0, 2, 1, 1, 0, 1, 1 };
		int[] B = { 2, 2, 0, 0, 0, 3, 2, 2, 3, 1, 3, 1, 0, 1, 2, 1, 0, 1, 3, 3 };
		double purity = Purity(A, B);
		System.out.println("purity\t\t" + purity);
		System.out.println("Pre\t\t" + Precision(A, B));
		System.out.println("Recall\t\t" + Recall(A, B));
		System.out.println("RI(Accuracy)\t\t" + RI(A, B));
		System.out.println("Fvalue\t\t" + F_score(A, B));
		System.out.println("NMI\t\t" + NMI(A, B));

	}

	/*
	 * ����һ���������Ĵصĸ������Լ�ÿһ���еĶ������,
	 */
	public static Map<Integer, Set<Integer>> clusterDistri(int[] A) {
		Map<Integer, Set<Integer>> clusterD = new HashMap<Integer, Set<Integer>>();
		int max = -1;
		for (int i = 0; i < A.length; i++) {

			if (max < A[i]) {
				max = A[i];
			}
		}
		for (int i = 0; i < A.length; i++) {
			int temp = A[i];
			if (temp < max + 1) {
				if (clusterD.containsKey(temp)) {
					Set<Integer> set = clusterD.get(temp);
					set.add(i + 1);
					clusterD.put(temp, set);
				} else {
					Set<Integer> set = new HashSet<Integer>();
					set.add(i + 1);
					clusterD.put(temp, set);
				}
			}
		}
		return clusterD;
	}

	public static double ClusEvaluate(String method, int[] A, int[] B) {

		switch (method) {
		case "Purity":
			return Purity(A, B);
		case "Precision":
			return Precision(A, B);
		case "Recall":
			return Recall(A, B);
		case "RI":
			return RI(A, B);
		case "F_score":
			return F_score(A, B);
		case "NMI":
			return NMI(A, B);
		case "Jaccard":
			return Jaccard(A, B);
		default:
			return -1.0;
		}

	}

	public static int[] commNum(Map<Integer, Set<Integer>> A, Map<Integer, Set<Integer>> B) {
		int[] commonNo = new int[A.size()];
		int com = 0;
		Iterator<Map.Entry<Integer, Set<Integer>>> itA = A.entrySet().iterator();
		int i = 0;
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();
			Set<Integer> setA = entryA.getValue();
			Iterator<Map.Entry<Integer, Set<Integer>>> itB = B.entrySet().iterator();
			int maxComm = -1;
			while (itB.hasNext()) {
				Entry<Integer, Set<Integer>> entryB = itB.next();
				Set<Integer> setB = entryB.getValue();
				int lengthA = setA.size();
				Set<Integer> temp = new HashSet<Integer>(setA);

				temp.removeAll(setB);

				int lengthCom = lengthA - temp.size();

				if (maxComm < lengthCom) {
					maxComm = lengthCom;
				}

			}

			commonNo[i] = maxComm;
			com = com + maxComm;
			i++;
		}

		return commonNo;
	}

	/*
	 * ���дط�����ȷ�ĳ����ܵġ�����B�ǶԱȵı�׼��ǩ��
	 */
	public static double Purity(int[] A, int[] B) {
		double value;
		Map<Integer, Set<Integer>> clusterA = clusterDistri(A);
		Map<Integer, Set<Integer>> clusterB = clusterDistri(B);
		int[] commonNo = commNum(clusterA, clusterB);
		int com = 0;
		for (int i = 0; i < commonNo.length; i++) {
			com = com + commonNo[i];
		}
		value = com * 1.0 / A.length;

		return value;
	}

	/*
	 * @param A,B
	 * 
	 * @return ����
	 */
	public static double Precision(int[] A, int[] B) {
		double value = 0.0;
		Map<Integer, Set<Integer>> clusterA = clusterDistri(A);// �õ�������A����ֲ�
		Map<Integer, Set<Integer>> clusterB = clusterDistri(B);// �õ�����B����׼������ֲ�
		int[] commonNo = commNum(clusterA, clusterB);// �õ�A��ÿ�����о�����ȷ����Ŀ��
		int allP = 0;
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
		Iterator<Map.Entry<Integer, Set<Integer>>> itA = clusterA.entrySet().iterator();
		int i = 0;
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();
			allP = allP + combination(entryA.getValue().size(), 2);
			TP = TP + combination(commonNo[i], 2);
			i++;
		}

		FP = allP - TP;

		itA = clusterA.entrySet().iterator();
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();

			Iterator<Map.Entry<Integer, Set<Integer>>> itA2 = clusterA.entrySet().iterator();
			while (itA2.hasNext()) {
				Entry<Integer, Set<Integer>> entryA2 = itA2.next();
				if (entryA != entryA2) {
					Set<Integer> s1 = entryA.getValue();
					Set<Integer> s2 = entryA2.getValue();
					for (Integer i1 : s1) {
						for (Integer i2 : s2) {
							if (B[i1 - 1] != B[i2 - 1]) {
								TN++;
							} else {
								FN++;
							}
						}
					}

				}
			}
		}

		double P = TP * 1.0 / (TP + FP);
		return P;
	}

	/*
	 * @param A,B
	 * 
	 * @return recal�ٻ���
	 */
	public static double Recall(int[] A, int[] B) {
		double value = 0.0;
		Map<Integer, Set<Integer>> clusterA = clusterDistri(A);// �õ�������A����ֲ�
		Map<Integer, Set<Integer>> clusterB = clusterDistri(B);// �õ�����B����׼������ֲ�
		int[] commonNo = commNum(clusterA, clusterB);// �õ�A��ÿ�����о�����ȷ����Ŀ��
		int allP = 0;
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
		Iterator<Map.Entry<Integer, Set<Integer>>> itA = clusterA.entrySet().iterator();
		int i = 0;
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();
			allP = allP + combination(entryA.getValue().size(), 2);
			TP = TP + combination(commonNo[i], 2);
			i++;
		}

		FP = allP - TP;

		itA = clusterA.entrySet().iterator();
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();

			Iterator<Map.Entry<Integer, Set<Integer>>> itA2 = clusterA.entrySet().iterator();
			while (itA2.hasNext()) {
				Entry<Integer, Set<Integer>> entryA2 = itA2.next();
				if (entryA != entryA2) {
					Set<Integer> s1 = entryA.getValue();
					Set<Integer> s2 = entryA2.getValue();
					for (Integer i1 : s1) {
						for (Integer i2 : s2) {
							if (B[i1 - 1] != B[i2 - 1]) {
								TN++;
							} else {
								FN++;
							}
						}
					}

				}
			}
		}

		double R = TP * 1.0 / (TP + FN);
		return R;
	}

	/*
	 * @param A,B
	 * 
	 * @return RankIndex
	 */
	public static double RI(int[] A, int[] B) {

		double value = 0.0;
		Map<Integer, Set<Integer>> clusterA = clusterDistri(A);// �õ�������A����ֲ�
		Map<Integer, Set<Integer>> clusterB = clusterDistri(B);// �õ�����B����׼������ֲ�
		int[] commonNo = commNum(clusterA, clusterB);// �õ�A��ÿ�����о�����ȷ����Ŀ��
		int P = 0;
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
		Iterator<Map.Entry<Integer, Set<Integer>>> itA = clusterA.entrySet().iterator();
		int i = 0;
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();
			P = P + combination(entryA.getValue().size(), 2);
			TP = TP + combination(commonNo[i], 2);
			i++;
		}

		FP = P - TP;

		itA = clusterA.entrySet().iterator();
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();

			Iterator<Map.Entry<Integer, Set<Integer>>> itA2 = clusterA.entrySet().iterator();
			while (itA2.hasNext()) {
				Entry<Integer, Set<Integer>> entryA2 = itA2.next();
				if (entryA != entryA2) {
					Set<Integer> s1 = entryA.getValue();
					Set<Integer> s2 = entryA2.getValue();
					for (Integer i1 : s1) {
						for (Integer i2 : s2) {
							if (B[i1 - 1] != B[i2 - 1]) {
								TN++;
							} else {
								FN++;
							}
						}
					}

				}
			}
		}
		value = (TP + TN) * 1.0 / (TP + FP + FN + TN);

		return value;
	}

	/*
	 * Fֵ���ǶԾ��Ⱥ��ٻ��ʵ�ƽ�⣬
	 * 
	 * @param A:��������B��������׼��beta���������
	 * 
	 * @return Fֵ
	 */
	public static double F_score(int[] A, int[] B) {

		double beta = 1.0;
		double value = 0.0;
		Map<Integer, Set<Integer>> clusterA = clusterDistri(A);// �õ�������A����ֲ�
		Map<Integer, Set<Integer>> clusterB = clusterDistri(B);// �õ�����B����׼������ֲ�
		int[] commonNo = commNum(clusterA, clusterB);// �õ�A��ÿ�����о�����ȷ����Ŀ��
		int allP = 0;
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
		Iterator<Map.Entry<Integer, Set<Integer>>> itA = clusterA.entrySet().iterator();
		int i = 0;
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();
			allP = allP + combination(entryA.getValue().size(), 2);
			TP = TP + combination(commonNo[i], 2);
			i++;
		}

		FP = allP - TP;

		itA = clusterA.entrySet().iterator();
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();

			Iterator<Map.Entry<Integer, Set<Integer>>> itA2 = clusterA.entrySet().iterator();
			while (itA2.hasNext()) {
				Entry<Integer, Set<Integer>> entryA2 = itA2.next();
				if (entryA != entryA2) {
					Set<Integer> s1 = entryA.getValue();
					Set<Integer> s2 = entryA2.getValue();
					for (Integer i1 : s1) {
						for (Integer i2 : s2) {
							if (B[i1 - 1] != B[i2 - 1]) {
								TN++;
							} else {
								FN++;
							}
						}
					}

				}
			}
		}

		double P = TP * 1.0 / (TP + FP);
		double R = TP * 1.0 / (TP + FN);
		value = (beta * beta + 1) * P * R / (beta * beta * P + R);
		return value;
	}

	public static double Jaccard(int[] A, int[] B) {

		double value = 0.0;
		Map<Integer, Set<Integer>> clusterA = clusterDistri(A);// �õ�������A����ֲ�
		Map<Integer, Set<Integer>> clusterB = clusterDistri(B);// �õ�����B����׼������ֲ�
		int[] commonNo = commNum(clusterA, clusterB);// �õ�A��ÿ�����о�����ȷ����Ŀ��
		int allP = 0;
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
		Iterator<Map.Entry<Integer, Set<Integer>>> itA = clusterA.entrySet().iterator();
		int i = 0;
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();
			allP = allP + combination(entryA.getValue().size(), 2);
			TP = TP + combination(commonNo[i], 2);
			i++;
		}

		FP = allP - TP;

		itA = clusterA.entrySet().iterator();
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();

			Iterator<Map.Entry<Integer, Set<Integer>>> itA2 = clusterA.entrySet().iterator();
			while (itA2.hasNext()) {
				Entry<Integer, Set<Integer>> entryA2 = itA2.next();
				if (entryA != entryA2) {
					Set<Integer> s1 = entryA.getValue();
					Set<Integer> s2 = entryA2.getValue();
					for (Integer i1 : s1) {
						for (Integer i2 : s2) {
							if (B[i1 - 1] != B[i2 - 1]) {
								TN++;
							} else {
								FN++;
							}
						}
					}

				}
			}
		}

		value = TP * 1.0 / (TP + FP + FN);
		return value;
	}

	public static double NMI(int[] A, int[] B) {
		Map<Integer, Set<Integer>> clusterA = clusterDistri(A);// �õ�������A����ֲ�
		Map<Integer, Set<Integer>> clusterB = clusterDistri(B);// �õ�����B����׼������ֲ�
		Iterator<Map.Entry<Integer, Set<Integer>>> itA = clusterA.entrySet().iterator();

		Iterator<Map.Entry<Integer, Set<Integer>>> itB = clusterB.entrySet().iterator();

		Set<Set<Integer>> partitionF = new HashSet<Set<Integer>>();
		Set<Set<Integer>> partitionR = new HashSet<Set<Integer>>();
		int nodeCount = B.length;
		while (itA.hasNext()) {
			Entry<Integer, Set<Integer>> entryA = itA.next();
			Set<Integer> setA = entryA.getValue();
			partitionF.add(setA);
			setA = null;
			entryA = null;
		}

		while (itB.hasNext()) {
			Entry<Integer, Set<Integer>> entryB = itB.next();
			Set<Integer> setB = entryB.getValue();
			partitionR.add(setB);
			setB = null;
			entryB = null;
		}
		return computeNMI(partitionF, partitionR, nodeCount);
	}

	public static double computeNMI(Set<Set<Integer>> partitionF, Set<Set<Integer>> partitionR, int nodeCount) {
		int[][] XY = new int[partitionR.size()][partitionF.size()];
		int[] X = new int[partitionR.size()];
		int[] Y = new int[partitionF.size()];
		int i = 0;
		int j = 0;

		for (Set<Integer> com1 : partitionR) {
			j = 0;

			for (Set<Integer> com2 : partitionF) {

				XY[i][j] = intersect(com1, com2);// ��������i���غͱ�׼�����j���صĹ���Ԫ�ظ���
				X[i] += XY[i][j];// ��������i���������б�׼����صĹ���Ԫ�ظ������о����ǵ�i���ص�Ԫ�ظ�����
				Y[j] += XY[i][j];// ��׼����ص�j���ص�Ԫ�ظ�������

				j++;
			}
			i++;
		}
		int N = nodeCount;
		double Ixy = 0;
		double Ixy2 = 0;
		for (i = 0; i < partitionR.size(); i++) {
			for (j = 0; j < partitionF.size(); j++) {
				if (XY[i][j] > 0) {
					Ixy += ((double) XY[i][j] / N) * (Math.log((double) XY[i][j] * N / (X[i] * Y[j])) / Math.log(2.0));
					// Ixy2 = (float) (Ixy2 + -2.0D * XY[i][j]
					// * Math.log(XY[i][j] * N / X[i] * Y[j]));
				}
			}
		}
		// System.out.println(Ixy2);
		// double denom = 0.0F;
		// for (int ii = 0; ii < X.length; ++ii)
		// denom = (double) (denom + X[ii] * Math.log(X[ii] / N));
		// for (int jj = 0; jj < Y.length; ++jj) {
		// denom = (double) (denom + Y[jj] * Math.log(Y[jj] / N));
		// }
		//
		// System.out.println(denom);
		// double M = (Ixy / denom);
		//
		// return M;

		double Hx = 0;
		double Hy = 0;
		for (i = 0; i < partitionR.size(); i++) {
			if (X[i] > 0)
				Hx += h((double) X[i] / N);
		}
		for (j = 0; j < partitionF.size(); j++) {
			if (Y[j] > 0)
				Hy += h((double) Y[j] / N);
		}

		double InormXY = Ixy / Math.sqrt(Hx * Hy);
		return InormXY;
	}

	private static double h(double p) {
		return -p * (Math.log(p) / Math.log(2.0));
	}

	/*
	 * �������ϵĹ���Ԫ�ظ���
	 */
	private static int intersect(Set<Integer> com1, Set<Integer> com2) {
		int num = 0;
		for (Integer v1 : com1) {
			if (com2.contains(v1))
				num++;
		}
		return num;
	}

	/*
	 * C(m,n)=mȡn
	 */
	public static int combination(int m, int n) {
		int result = 1;
		if (m < n) {
			return -1;
		}
		result = factorial(m) / (factorial(n) * factorial(m - n));

		return result;
	}

	public static int factorial(int m) {

		if ((m == 1) || (m == 0)) {
			return 1;
		} else if (m < 0) {
			return -1;
		} else {
			return m * factorial(m - 1);
		}
	}
}
