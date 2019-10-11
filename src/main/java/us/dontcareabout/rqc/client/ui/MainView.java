package us.dontcareabout.rqc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.TabPanel;

public class MainView extends Composite {
	private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	@UiField TabPanel root;
	@UiField QuoteView quoteView;

	public MainView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void toQuoteView() {
		root.setActiveWidget(quoteView);
	}

	interface MainViewUiBinder extends UiBinder<Widget, MainView> {}
}
