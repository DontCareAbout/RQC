package us.dontcareabout.rqc.client.ui.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.rqc.client.ui.event.TagConditionChangeEvent.TagConditionChangeHandler;

public class TagConditionChangeEvent extends GwtEvent<TagConditionChangeHandler> {
	public static final Type<TagConditionChangeHandler> TYPE = new Type<TagConditionChangeHandler>();

	/** true 是 and，false 是 or */
	public final boolean value;

	public TagConditionChangeEvent(boolean condition) {
		this.value = condition;
	}

	@Override
	public Type<TagConditionChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TagConditionChangeHandler handler) {
		handler.onConditionChange(this);
	}

	public interface TagConditionChangeHandler extends EventHandler{
		public void onConditionChange(TagConditionChangeEvent event);
	}
}
