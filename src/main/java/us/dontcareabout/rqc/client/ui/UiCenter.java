package us.dontcareabout.rqc.client.ui;

import java.util.HashSet;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.rqc.client.ui.SelectTagChangeEvent.SelectTagChangeHandler;
import us.dontcareabout.rqc.client.ui.TagConditionChangeEvent.TagConditionChangeHandler;

public class UiCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	private static HashSet<String> tagSet = new HashSet<>();

	public static void selectTagChange(String tag, boolean isSelect) {
		//雖然不太可能發生，不過還是文字意義上讓 fireEvent 的動作必然合理
		boolean result;

		if (isSelect) {
			result = tagSet.add(tag.toUpperCase());
		} else {
			result = tagSet.remove(tag.toUpperCase());
		}

		if (result) { eventBus.fireEvent(new SelectTagChangeEvent(tagSet)); }
	}

	public static HandlerRegistration addSelectTagChange(SelectTagChangeHandler handler) {
		return eventBus.addHandler(SelectTagChangeEvent.TYPE, handler);
	}

	////////////////

	public static void tagConditionChange(boolean condition) {
		if (tagSet.isEmpty()) { return; }
		eventBus.fireEvent(new TagConditionChangeEvent(condition));
	}

	public static HandlerRegistration addTagConditionChange(TagConditionChangeHandler handler) {
		return eventBus.addHandler(TagConditionChangeEvent.TYPE, handler);
	}
}
