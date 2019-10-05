package us.dontcareabout.rqc.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Util {
	/**
	 * 僅供 {@link #keywordIndex(String, String[])} 用，所以邏輯很粗暴 XD
	 */
	private final static Comparator<int[]> indexComparator = new Comparator<int[]>() {
		@Override
		public int compare(int[] o1, int[] o2) {
			return o1[0] - o2[0];
		}
	};

	/**
	 * <b>注意：</b>並沒有針對 keywords 作任何預防或是處理。
	 * 例如傳入 {"tea", "team"} 就會導致回傳值不符合預期。
	 *
	 * @return 各 keyword 在字串中的起始與結束 index 陣列。
	 */
	public static ArrayList<int[]> keywordIndex(String string, String[] keywords) {
		if (keywords == null) { return new ArrayList<>(); }

		ArrayList<int[]> result = new ArrayList<>();
		string = string.toLowerCase();

		for (String keyword : keywords) {
			keyword = keyword.toLowerCase();
			int index = 0;
			while (index <= string.length() && (index = string.indexOf(keyword, index)) != -1) {
				result.add(new int[]{index, index + keyword.length()});
				index += keyword.length();
			}
		}

		Collections.sort(result, indexComparator);
		return result;
	}
}
