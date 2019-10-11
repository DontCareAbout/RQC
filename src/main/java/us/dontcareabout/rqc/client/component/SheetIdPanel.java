package us.dontcareabout.rqc.client.component;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.TextField;

import us.dontcareabout.rqc.client.data.SheetId;
import us.dontcareabout.rqc.client.gf.SheetUtil;
import us.dontcareabout.rqc.client.ui.UiCenter;
import us.dontcareabout.rqc.client.ui.event.SheetIdSelectEvent;
import us.dontcareabout.rqc.client.ui.event.SheetIdSelectEvent.SheetIdSelectHandler;

public class SheetIdPanel extends Composite {
	private static SheetIdPanelUiBinder uiBinder = GWT.create(SheetIdPanelUiBinder.class);

	@UiField FramedPanel form;
	@UiField TextField id;
	@UiField TextField name;
	@UiField CheckBox select;

	private SheetId nowInstance;

	public SheetIdPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		UiCenter.addSheetIdSelect(new SheetIdSelectHandler() {
			@Override
			public void onSheetIdSelect(SheetIdSelectEvent event) {
				refresh(event.data);
			}
		});
	}

	@UiHandler("newBtn")
	void selectNew(SelectEvent se) {
		refresh(new SheetId());
	}

	@UiHandler("submitBtn")
	void selectSubmit(SelectEvent se) {
		nowInstance.setId(SheetUtil.parseSheetId(id.getCurrentValue()));
		nowInstance.setName(name.getCurrentValue());
		nowInstance.setSelect(select.getValue());

		ArrayList<SheetId> list = SheetId.retrieve();

		if (list.contains(nowInstance)) {
			list.remove(nowInstance);
		}

		if (nowInstance.isSelect()) {
			for (SheetId sid : list) {
				sid.setSelect(false);
			}
		}

		list.add(nowInstance);
		storeAndDisable(list);
	}

	@UiHandler("deleteBtn")
	void selectDelete(SelectEvent se) {
		ArrayList<SheetId> list = SheetId.retrieve();
		list.remove(nowInstance);
		storeAndDisable(list);
	}

	private void refresh(SheetId sid) {
		nowInstance = sid;
		form.setEnabled(true);
		id.setValue(sid.getId() == null ? "" : SheetUtil.sheetUrl(sid.getId()));
		name.setValue(sid.getName());
		select.setValue(sid.isSelect());
	}

	private void storeAndDisable(ArrayList<SheetId> list) {
		SheetId.store(list);
		UiCenter.refreshSheetIdStore();
		form.setEnabled(false);
	}

	interface SheetIdPanelUiBinder extends UiBinder<Widget, SheetIdPanel> {}
}
