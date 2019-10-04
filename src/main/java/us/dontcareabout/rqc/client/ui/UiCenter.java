package us.dontcareabout.rqc.client.ui;

import java.util.HashSet;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.Viewport;

import us.dontcareabout.rqc.client.component.KeywordPanel.KeywordParam;
import us.dontcareabout.rqc.client.ui.KeywordChangeEvent.KeywordChangeHandler;
import us.dontcareabout.rqc.client.ui.SelectTagChangeEvent.SelectTagChangeHandler;
import us.dontcareabout.rqc.client.ui.TagConditionChangeEvent.TagConditionChangeHandler;

public class UiCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	public static void keywordChange(KeywordParam param) {
		eventBus.fireEvent(new KeywordChangeEvent(param));
	}

	public static HandlerRegistration addKeywordChange(KeywordChangeHandler handler) {
		return eventBus.addHandler(KeywordChangeEvent.TYPE, handler);
	}

	////////////////

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
		eventBus.fireEvent(new TagConditionChangeEvent(condition));
	}

	public static HandlerRegistration addTagConditionChange(TagConditionChangeHandler handler) {
		return eventBus.addHandler(TagConditionChangeEvent.TYPE, handler);
	}

	////////////////////////////////

	public static void start() {
		viewport.add(new QuoteView());
	}

	private final static Viewport viewport = new Viewport();
	static {
		RootPanel.get().add(viewport);
	}

	public static void mask(String message) {
		viewport.mask(message);
	}

	public static void unmask() {
		viewport.unmask();
	}

	private final static Window dialog = new Window();
	static {
		dialog.setModal(true);
		dialog.setResizable(false);
	}

	public static void closeDialog() {
		dialog.hide();
	}

	private static void dialog(Widget widget, int width, int height) {
		dialog.clear();
		dialog.add(widget);
		dialog.show();
		dialog.setPixelSize(width, height);
		dialog.center();
	}
}
