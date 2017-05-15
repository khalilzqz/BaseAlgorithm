package libsvm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class formatTolibsvm {

	static BufferedReader br = null; // read the file to bufferedreader
	static int classification = 0; // classification number
	static FileWriter fw = null; // put the result to file

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sourceFileName = "D:/DataForMining/Titanic/Lot14";
		String destFileName = "D:/DataForMining/Titanic/LotRFout.txt";
		uci2Libsvm(sourceFileName, destFileName);
	}

	public static void uci2Libsvm(String sourceFileName, String destFileName) {
		String strline = null;

		// whether the file is exists
		File file = new File(sourceFileName);
		if (!file.exists()) {
			System.out.println("file not exists!");
			return;
		}

		try {
			br = new BufferedReader(new FileReader(sourceFileName));
			fw = new FileWriter(destFileName);
			int i = 1; // the index of the libsvm format file
			while ((strline = br.readLine()) != null) {
				String[] elements = strline.split(",");

				int l = elements.length;

				if (elements[0].contains("1")) {
					classification = 1;
				} else if (elements[0].contains("0")) {
					classification = 0;
				}

				String result = classification + " ";
				while (i < l) {
					result = result + " " + i + ":" + elements[i++];
				}
				i = 1;
				System.out.println(result);
				fw.write(result.trim() + "\n");
			}
			fw.close();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}

		System.out.println("succeed!");
	}
}
