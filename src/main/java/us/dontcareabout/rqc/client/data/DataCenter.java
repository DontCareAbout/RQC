package us.dontcareabout.rqc.client.data;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.gwt.client.Console;
import us.dontcareabout.gwt.client.google.Sheet;
import us.dontcareabout.gwt.client.google.SheetHappen;
import us.dontcareabout.gwt.client.google.SheetHappen.Callback;
import us.dontcareabout.rqc.client.data.QuoteReadyEvent.QuoteReadyHandler;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	public static void wantQuote() {
		SheetHappen.<Quote>get(SheetId.get(), 1,
			new Callback<Quote>() {
				@Override
				public void onSuccess(Sheet<Quote> gs) {
					eventBus.fireEvent(new QuoteReadyEvent(gs.getEntry()));
				}

				@Override
				public void onError(Throwable exception) {
					Console.log(exception);
				}
		});
	}

	public static HandlerRegistration addQuoteReady(QuoteReadyHandler handler) {
		return eventBus.addHandler(QuoteReadyEvent.TYPE, handler);
	}
	////////////////
}
