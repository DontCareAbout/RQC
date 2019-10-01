package us.dontcareabout.rqc.client.ui;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.rqc.client.ui.SelectTagChangeEvent.SelectTagChangeHandler;

public class SelectTagChangeEvent extends GwtEvent<SelectTagChangeHandler> {
	public static final Type<SelectTagChangeHandler> TYPE = new Type<SelectTagChangeHandler>();

	/**
	 * 每個 element 都得作過 {@link String#toUpperCase()}
	 */
	public final Set<String> data;

	public SelectTagChangeEvent(HashSet<String> tagSet) {
		this.data = Collections.unmodifiableSet(tagSet);
	}

	@Override
	public Type<SelectTagChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelectTagChangeHandler handler) {
		handler.onSelectTag(this);
	}

	public interface SelectTagChangeHandler extends EventHandler{
		public void onSelectTag(SelectTagChangeEvent event);
	}
}
