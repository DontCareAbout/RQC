package us.dontcareabout.rqc.client.component;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import us.dontcareabout.gxt.client.model.GetValueProvider;
import us.dontcareabout.rqc.client.data.SheetId;
import us.dontcareabout.rqc.client.gf.Grid2;

public class SheetIdGrid extends Grid2<SheetId> {
	private static final Properties properties = GWT.create(Properties.class);
	private static String js_name = "JS 指定值";

	public SheetIdGrid() {
		init();
		view.setForceFit(true);
		refresh();
	}

	@Override
	protected ListStore<SheetId> genListStore() {
		return new ListStore<>(properties.key());
	}

	@Override
	protected ColumnModel<SheetId> genColumnModel() {
		ColumnConfig<SheetId, Integer> defaultSelect = new ColumnConfig<>(
			new GetValueProvider<SheetId, Integer>() {
				@Override
				public Integer getValue(SheetId object) {
					if (!object.isSelect()) { return 0; }
					if (SheetId.jsValue() == null) { return 1; }
					return js_name.equals(object.getName()) ? 1 : 2;
				}
			},
			100, "預設開啟順位"
		);
		defaultSelect.setFixed(true);
		defaultSelect.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		defaultSelect.setCell(new AbstractCell<Integer>() {
			@Override
			public void render(Context context, Integer value, SafeHtmlBuilder sb) {
				if (value == 0) { return; }

				sb.appendHtmlConstant(
					"<div style='margin-left:auto;margin-right:auto;width:16px;height:24px;font-size:14px;color:red;'>"
					+ value
					+ "</div>"
				);
			}
		});

		ArrayList<ColumnConfig<SheetId, ?>> list = new ArrayList<>();
		list.add(defaultSelect);
		list.add(new ColumnConfig<>(properties.name(), 50, "名稱"));
		list.add(new ColumnConfig<>(properties.id(), 100, "Sheet ID"));
		return new ColumnModel<>(list);
	}

	private void refresh() {
		ArrayList<SheetId> data = SheetId.retrieve();
		String js = SheetId.jsValue();

		if (js != null) {
			SheetId sid = new SheetId(js, js_name, true);
			data.add(sid);
		}

		store.replaceAll(data);
	}

	interface Properties extends PropertyAccess<SheetId> {
		@Path("id") ModelKeyProvider<SheetId> key();
		ValueProvider<SheetId, String> id();
		ValueProvider<SheetId, Boolean> select();
		ValueProvider<SheetId, String> name();
	}
}
