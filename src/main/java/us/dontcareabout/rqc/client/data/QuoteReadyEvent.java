package us.dontcareabout.rqc.client.data;

import java.util.ArrayList;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.rqc.client.data.QuoteReadyEvent.QuoteReadyHandler;

public class QuoteReadyEvent extends GwtEvent<QuoteReadyHandler> {
	public static final Type<QuoteReadyHandler> TYPE = new Type<QuoteReadyHandler>();
	public final ArrayList<Quote> data;

	public QuoteReadyEvent(ArrayList<Quote> entry) {
		this.data = entry;
	}

	@Override
	public Type<QuoteReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(QuoteReadyHandler handler) {
		handler.onQuoteReady(this);
	}

	public interface QuoteReadyHandler extends EventHandler{
		public void onQuoteReady(QuoteReadyEvent event);
	}
}
