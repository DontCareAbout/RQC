package us.dontcareabout.rqc.client.component;

import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

import us.dontcareabout.rqc.client.ui.UiCenter;

public class KeywordPanel extends FramedPanel {
	private TextField keyword = new TextField();
	private TextButton submit = new TextButton("篩選");
	private TextButton clear = new TextButton("清除");

	private KeywordParam param = new KeywordParam();

	public KeywordPanel() {
		setHeadingText("關鍵字");

		add(keyword);
		getButtonBar().add(clear);
		getButtonBar().add(submit);

		clear.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				keyword.setValue("");
				param.keyword = keyword.getCurrentValue();
				UiCenter.keywordChange(param);
			}
		});

		submit.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				param.keyword = keyword.getCurrentValue();
				UiCenter.keywordChange(param);
			}
		});
	}

	public static class KeywordParam {
		String keyword;
		boolean isAnd;

		public String getKeyword() {
			return keyword;
		}
		public boolean isAnd() {
			return isAnd;
		}
	}
}
