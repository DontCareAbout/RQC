package us.dontcareabout.rqc.client.data;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;

import us.dontcareabout.gst.client.data.ApiKey;
import us.dontcareabout.gwt.client.google.sheet.Sheet;
import us.dontcareabout.gwt.client.google.sheet.SheetDto;
import us.dontcareabout.gwt.client.google.sheet.SheetDto.Callback;
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

	private static List<Quote> quoteList;

	public static List<Quote> getQuote() {
		return Collections.unmodifiableList(quoteList);
	}

	private static SheetDto<Quote> quoteSheet = new SheetDto<Quote>()
		.key(ApiKey.jsValue()).tabName("引言");

	public static void wantQuote(String sheetId) {
		UiCenter.mask("資料讀取中......");
		quoteSheet.sheetId(sheetId).fetch(
			new Callback<Quote>() {
				@Override
				public void onSuccess(Sheet<Quote> gs) {
					//優先作 unmask()，這樣可以避免取得的 sheet 欄位不合 RQC 規格
					//UI 可能會炸 exception 而導致程式完全無法操作的問題
					UiCenter.unmask();
					quoteList = gs.getRows();
					eventBus.fireEvent(new QuoteReadyEvent());
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
