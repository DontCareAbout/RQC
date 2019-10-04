package us.dontcareabout.rqc.client.ui;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.rqc.client.ui.KeywordChangeEvent.KeywordChangeHandler;

public class KeywordChangeEvent extends GwtEvent<KeywordChangeHandler> {
	public static final Type<KeywordChangeHandler> TYPE = new Type<KeywordChangeHandler>();
	public final String data;

	public KeywordChangeEvent(String keyword) {
		data = keyword;
	}

	@Override
	public Type<KeywordChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(KeywordChangeHandler handler) {
		handler.onKeywordChange(this);
	}

	public interface KeywordChangeHandler extends EventHandler{
		public void onKeywordChange(KeywordChangeEvent event);
	}
}