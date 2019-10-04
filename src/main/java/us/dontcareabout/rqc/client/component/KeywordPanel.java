package us.dontcareabout.rqc.client.component;

import com.google.common.base.Strings;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextField;

import us.dontcareabout.rqc.client.ui.UiCenter;

public class KeywordPanel extends FramedPanel {
	private static final VerticalLayoutData VLD = new VerticalLayoutData(1, 0.5);

	private Radio and = new Radio();
	private Radio or = new Radio();
	private TextField keyword = new TextField();
	private TextButton submit = new TextButton("篩選");
	private TextButton clear = new TextButton("清除");

	private KeywordParam param = new KeywordParam();

	public KeywordPanel() {
		setHeadingText("關鍵字");

		and.setBoxLabel("AND");
		or.setBoxLabel("OR");

		ToggleGroup radioGroup = new ToggleGroup();
		radioGroup.add(and);
		radioGroup.add(or);
		radioGroup.setValue(or);

		HorizontalLayoutContainer radioHLC = new HorizontalLayoutContainer();
		radioHLC.add(and);
		radioHLC.add(or);

		VerticalLayoutContainer mainVLC= new VerticalLayoutContainer();
		mainVLC.add(radioHLC, VLD);
		mainVLC.add(keyword, VLD);
		add(mainVLC);

		getButtonBar().add(clear);
		getButtonBar().add(submit);

		radioGroup.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>() {
			@Override
			public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
				if (Strings.isNullOrEmpty(keyword.getCurrentValue())) { return; }
				//那種只有一堆空格的就不擋了...... Zzzz

				fire();
			}
		});

		clear.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				keyword.setValue("");
				fire();
			}
		});

		submit.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				fire();
			}
		});
	}

	private void fire() {
		param.keyword = keyword.getCurrentValue();
		param.isAnd = and.getValue();
		UiCenter.keywordChange(param);
	}

	public static class KeywordParam {
		private String keyword;
		private boolean isAnd;

		public String getKeyword() {
			return keyword;
		}
		public boolean isAnd() {
			return isAnd;
		}
	}
}
