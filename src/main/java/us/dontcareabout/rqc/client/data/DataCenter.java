package us.dontcareabout.rqc.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.gwt.client.Console;
import us.dontcareabout.gwt.client.google.Sheet;
import us.dontcareabout.gwt.client.google.SheetHappen;
import us.dontcareabout.gwt.client.google.SheetHappen.Callback;
import us.dontcareabout.rqc.client.data.QuoteReadyEvent.QuoteReadyHandler;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	private static ArrayList<Quote> quoteList;

	public static List<Quote> getQuote() {
		return Collections.unmodifiableList(quoteList);
	}

	public static void wantQuote() {
		SheetHappen.<Quote>get(SheetId.get(), 1,
			new Callback<Quote>() {
				@Override
				public void onSuccess(Sheet<Quote> gs) {
					quoteList = gs.getEntry();
					eventBus.fireEvent(new QuoteReadyEvent());
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
