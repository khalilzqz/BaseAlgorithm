package RandomForest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {
	public static void main(String[] args) {
		BufferedReader BR = null;
		String path = "D:/test1.txt";
		String sCurrentLine;
		ArrayList<ArrayList<String>> DataInput = new ArrayList<ArrayList<String>>();
		try {
			BR = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while ((sCurrentLine = BR.readLine()) != null) {
				ArrayList<Integer> Sp = new ArrayList<Integer>();
				int i;
				System.out.println(sCurrentLine);
				if (sCurrentLine != null) {
					if (sCurrentLine.indexOf(",") >= 0) {
						// has comma
						sCurrentLine = "," + sCurrentLine + ",";
						char[] c = sCurrentLine.toCharArray();
						for (i = 0; i < sCurrentLine.length(); i++) {
							if (c[i] == ',')
								Sp.add(i);
						}
						ArrayList<String> DataPoint = new ArrayList<String>();
						for (i = 0; i < Sp.size() - 1; i++) {
							DataPoint.add(sCurrentLine.substring(Sp.get(i) + 1, Sp.get(i + 1)).trim());
						}
						DataInput.add(DataPoint);
						System.out.println(DataPoint.get(DataPoint.size() - 1));
					} else if (sCurrentLine.indexOf(" ") >= 0) {
						// has spaces
						sCurrentLine = " " + sCurrentLine + " ";
						for (i = 0; i < sCurrentLine.length(); i++) {
							if (Character.isWhitespace(sCurrentLine.charAt(i)))
								Sp.add(i);
						}
						ArrayList<String> DataPoint = new ArrayList<String>();
						for (i = 0; i < Sp.size() - 1; i++) {
							DataPoint.add(sCurrentLine.substring(Sp.get(i), Sp.get(i + 1)).trim());
						}
						DataInput.add(DataPoint);
						// System.out.println(DataPoint);
					}
				}
			}
			HashMap<String, Integer> Classes = new HashMap<String, Integer>();
			for (ArrayList<String> dp : DataInput) {
				String clas = dp.get(dp.size() - 1);
				if (Classes.containsKey(clas))
					Classes.put(clas, Classes.get(clas) + 1);
				else
					Classes.put(clas, 1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
