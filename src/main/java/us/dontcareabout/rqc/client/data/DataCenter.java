package us.dontcareabout.rqc.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;

import us.dontcareabout.gwt.client.google.Sheet;
import us.dontcareabout.gwt.client.google.SheetHappen;
import us.dontcareabout.gwt.client.google.SheetHappen.Callback;
import us.dontcareabout.rqc.client.data.QuoteReadyEvent.QuoteReadyHandler;
import us.dontcareabout.rqc.client.ui.UiCenter;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	private static void loadError() {
		Window.alert("資料載入失敗。\n"
			+ "請檢查試算表網址是否正確、是否已作「發布到網路」動作。"
		);
		UiCenter.unmask();
	}

	////////////////

	private static ArrayList<Quote> quoteList;

	public static List<Quote> getQuote() {
		return Collections.unmodifiableList(quoteList);
	}

	public static void wantQuote(String sheetId) {
		UiCenter.mask("資料讀取中......");
		SheetHappen.<Quote>get(sheetId, 1,
			new Callback<Quote>() {
				@Override
				public void onSuccess(Sheet<Quote> gs) {
					quoteList = gs.getEntry();
					eventBus.fireEvent(new QuoteReadyEvent());
					UiCenter.unmask();
				}

				@Override
				public void onError(Throwable exception) {
					loadError();
				}
		});
	}

	public static HandlerRegistration addQuoteReady(QuoteReadyHandler handler) {
		return eventBus.addHandler(QuoteReadyEvent.TYPE, handler);
	}

	////////////////
}
