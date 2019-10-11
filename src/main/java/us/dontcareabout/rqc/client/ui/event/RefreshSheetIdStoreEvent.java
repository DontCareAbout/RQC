package us.dontcareabout.rqc.client.ui.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.rqc.client.ui.event.RefreshSheetIdStoreEvent.RefreshSheetIdStoreHandler;

public class RefreshSheetIdStoreEvent extends GwtEvent<RefreshSheetIdStoreHandler> {
	public static final Type<RefreshSheetIdStoreHandler> TYPE = new Type<RefreshSheetIdStoreHandler>();

	@Override
	public Type<RefreshSheetIdStoreHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RefreshSheetIdStoreHandler handler) {
		handler.onRefreshSheetIdStore(this);
	}

	public interface RefreshSheetIdStoreHandler extends EventHandler{
		public void onRefreshSheetIdStore(RefreshSheetIdStoreEvent event);
	}
}
