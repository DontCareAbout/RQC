package us.dontcareabout.rqc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;

import us.dontcareabout.rqc.client.component.QuoteGrid;
import us.dontcareabout.rqc.client.component.TagCloud;
import us.dontcareabout.rqc.client.data.DataCenter;
import us.dontcareabout.rqc.client.data.QuoteReadyEvent;
import us.dontcareabout.rqc.client.data.QuoteReadyEvent.QuoteReadyHandler;

public class QuoteView extends Composite {
	private static QuotaViewUiBinder uiBinder = GWT.create(QuotaViewUiBinder.class);

	@UiField QuoteGrid grid;
	@UiField TagCloud tagCloud;

	public QuoteView() {
		initWidget(uiBinder.createAndBindUi(this));

		DataCenter.addQuoteReady(new QuoteReadyHandler() {
			@Override
			public void onQuoteReady(QuoteReadyEvent event) {
				grid.refresh(DataCenter.getQuote());
				tagCloud.refresh(DataCenter.getQuote());
			}
		});
	}

	interface QuotaViewUiBinder extends UiBinder<Widget, QuoteView> {}
}
