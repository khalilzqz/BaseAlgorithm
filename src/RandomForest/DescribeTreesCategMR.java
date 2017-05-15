package RandomForest;

import java.util.ArrayList;

//https://github.com/ironmanMA/Random-Forest
//为原始数据添加中括号和comma
public class DescribeTreesCategMR {
	// method to take the txt fle as input and pass those values to random
	// forests
	public ArrayList<String> CreateInputCateg(String str) {
		ArrayList<Integer> Sp = new ArrayList<Integer>();
		ArrayList<String> DataPoint = new ArrayList<String>();
		int i;
		if (str != null) {
			if (str.indexOf(",") >= 0) {
				// has comma
				str = "," + str + ",";
				char[] c = str.toCharArray();
				for (i = 0; i < str.length(); i++) {
					if (c[i] == ',')
						Sp.add(i);
				}
				for (i = 0; i < Sp.size() - 1; i++) {
					DataPoint.add(str.substring(Sp.get(i) + 1, Sp.get(i + 1)).trim());
				}
			} else if (str.indexOf(" ") >= 0) {
				// has spaces
				str = " " + str + " ";
				for (i = 0; i < str.length(); i++) {
					if (Character.isWhitespace(str.charAt(i)))
						Sp.add(i);
				}
				for (i = 0; i < Sp.size() - 1; i++) {
					DataPoint.add(str.substring(Sp.get(i), Sp.get(i + 1)).trim());
				}
			}
		}
		return DataPoint;
	}
}
