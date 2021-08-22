package us.dontcareabout.rqc.client.data;

import java.util.ArrayList;

import com.google.common.base.Strings;

import us.dontcareabout.gwt.client.google.sheet.Row;

public final class Quote extends Row {
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

	public Double getScore() {
		return doubleField("重要性");
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
