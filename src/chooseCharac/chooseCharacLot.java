package chooseCharac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class chooseCharacLot {
	public static void main(String[] args) throws IOException {
		String inputFile = "D:/DataForMining/Lot/2017v5/part-r-00000";
		String outputFile = "D:/DataForMining/Lot/2017v5/filter";

		// 读文件
		File file = new File(inputFile);
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(file));
		String tempString = "";
		FileWriter fw1 = new FileWriter(outputFile);
		HashMap<String, Integer> nameMap = new HashMap<>();
		while ((tempString = reader.readLine()) != null) {
			if (tempString.startsWith("id")) {
				String[] name = tempString.split(",");
				int count = 2;
				for (int i = 2; i < name.length; i++) {
					nameMap.put(name[i], count++);
				}
			} else {
				String[] charac = tempString.split(",|\\s+");
				HashSet<Integer> chooseNum = new HashSet<>();
				chooseNum.add(1);
				chooseNum.add(2);
				chooseNum.add(3);
				chooseNum.add(4);
				chooseNum.add(getCharacRow("water11", nameMap));
				chooseNum.add(getCharacRow("handicap11", nameMap));
				chooseNum.add(getCharacRow("tou1", nameMap));
				chooseNum.add(getCharacRow("tou2", nameMap));
				chooseNum.add(getCharacRow("tou3", nameMap));
				// 调整
				if (charac.length > 27) {
//					if (!charac[getCharacRow("tou3", nameMap)].equals("0.0")) {
						String out = "";
						for (int i = 1; i < charac.length; i++) {
							if (chooseNum.contains(i)) {
								out = out + charac[i] + ",";
							}
						}
						out = out.substring(0, out.length() - 1);
						System.out.println(out);
						fw1.write(out + "\n");
//					}
				}
			}
		}

		reader.close();
		fw1.close();
	}

	public static int getCharacRow(String str, HashMap<String, Integer> nameMap) {
		if (nameMap.containsKey(str)) {
			return nameMap.get(str);
		} else {
			return 0;
		}
	}
}
