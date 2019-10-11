package us.dontcareabout.rqc.client.gf;

import com.google.common.base.Preconditions;

//Refactory GF
public class SheetUtil {
	private static final String HEADER = "https://docs.google.com/spreadsheets/d/";

	public static String parseSheetId(String url) {
		//TODO 改善 exception message
		Preconditions.checkNotNull(url);
		Preconditions.checkArgument(url.startsWith(HEADER));
		int index = url.indexOf("/", HEADER.length());
		if (index == -1) { index = url.length(); }
		return url.substring(HEADER.length(), index);
	}

	public static String sheetUrl(String sheetId) {
		return HEADER + sheetId;
	}
}
