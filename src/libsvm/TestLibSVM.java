package libsvm;

import java.io.IOException;

public class TestLibSVM {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String[] arg = { "D:/DataForMining/SVM/xgboosttrain", // ���SVMѵ��ģ���õ����ݵ�·��
				"D:/DataForMining/SVM/LotHandicapmodel.model" }; // ���SVMͨ��ѵ������ѵ/ //��������ģ�͵�·��

		String[] parg = { "D:/DataForMining/SVM/xgboosttest", // ����Ǵ�Ų�������
				"D:/DataForMining/SVM/LotHandicapmodel.model", // ���õ���ѵ���Ժ��ģ��
				"D:/DataForMining/SVM/result.txt" }; // ���ɵĽ�����ļ���·��
		System.out.println("........SVM���п�ʼ..........");
		// ����һ��ѵ������
		svm_train t = new svm_train();
		// ����һ��Ԥ����߷���Ķ���
		svm_predict p = new svm_predict();
		t.main(arg); // ����
		p.main(parg); // ����
	}
}
