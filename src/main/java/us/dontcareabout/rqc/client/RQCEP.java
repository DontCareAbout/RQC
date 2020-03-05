package us.dontcareabout.rqc.client;

import com.google.gwt.user.client.Window;

import us.dontcareabout.gwt.client.GFEP;
import us.dontcareabout.gwt.client.iCanUse.Feature;
import us.dontcareabout.rqc.client.data.DataCenter;
import us.dontcareabout.rqc.client.data.SheetId;
import us.dontcareabout.rqc.client.ui.UiCenter;

public class RQCEP extends GFEP {
	public RQCEP() {
		needFeature(Feature.Storage);
	}

	@Override
	protected String version() { return "0.0.1"; }

	@Override
	protected String defaultLocale() { return "zh_TW"; }

	@Override
	protected void featureFail() {
		Window.alert("這個瀏覽器我不尬意，不給用..... \\囧/");
	}

	@Override
	protected void start() {
		UiCenter.start();
		DataCenter.wantQuote(SheetId.get());
	}
}
