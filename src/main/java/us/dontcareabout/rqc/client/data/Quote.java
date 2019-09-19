package us.dontcareabout.rqc.client.data;

import java.util.ArrayList;

import com.google.common.base.Strings;

import us.dontcareabout.gwt.client.google.SheetEntry;

public final class Quote extends SheetEntry {
	protected Quote() {}

	public String getType() {
		return stringField("來源類型");
	}

	public String getRefName() {
		return stringField("來源名");
	}

	public String getPage() {
		return stringField("頁數");
	}

	public String getText() {
		return stringField("引用文字");
	}

	public String getNote() {
		return stringField("備註");
	}

	public String getScore() {
		return stringField("重要性");
	}

	public ArrayList<String> getTag() {
		ArrayList<String> result = new ArrayList<>();
		String[] value = stringField("tag").split(",");

		for (String text : value) {
			text = text.trim();
			if (Strings.isNullOrEmpty(text)) { continue; }
			result.add(text);
		}

		return result;
	}
}
