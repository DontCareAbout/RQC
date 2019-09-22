package us.dontcareabout.rqc.client.component;

import java.util.ArrayList;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.grid.RowExpander;

import us.dontcareabout.gxt.client.model.GetValueProvider;
import us.dontcareabout.rqc.client.data.Quote;

public class QuoteGrid extends Grid<Quote> {
	private static Properties properties = GWT.create(Properties.class);

	private RowExpander<Quote> rowExpander = new RowExpander<Quote>(
		new AbstractCell<Quote>() {
			@Override
			public void render(Context context, Quote value, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<div style='font-size:16px; height:20px; padding: 5px 0 0 0;'>引用文字：</div><div style='font-size:14px; margin:5px 5px 5px 28px'>" + value.getText() + "</div>");
				sb.appendHtmlConstant("<div style='font-size:16px; height:20px; padding: 5px 0 0 0;'>備註：</div><div style='font-size:14px; margin:5px 5px 5px 28px'>" + value.getNote() + "</div>");
			}
		}
	);
	private ColumnConfig<Quote, String> refNameCC = new ColumnConfig<>(
		new GetValueProvider<Quote, String>() {
			@Override
			public String getValue(Quote item) {
				if (Strings.isNullOrEmpty(item.getRefName())) {
					return "(unknown)";
				}

				String text = "(?)";

				switch(item.getType().trim().toUpperCase()) {
				case "B":
					text = "[書目]"; break;
				case "J":
					text = "[期刊]"; break;
				}

				return text + "　" + item.getRefName();
			}
		}
	);

	public QuoteGrid() {
		GroupingView<Quote> view = new GroupingView<>();
		view.groupBy(refNameCC);
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		setView(view);

		//這段是照抄 Gird(store, cm, view)
		disabledStyle = null;
		setSelectionModel(new GridSelectionModel<Quote>());

		setAllowTextSelection(false);

		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		view.getAppearance().render(builder);

		setElement((Element) XDOM.create(builder.toSafeHtml()));
		getElement().makePositionable();

		//因為後頭 reconfigure() 也會作，所以這裡就跳過了
		//sinkCellEvents();
		////

		reconfigure(
			new ListStore<>(properties.key()),
			genColumnModel()
		);

		rowExpander.initPlugin(this);
	}

	public void refresh(ArrayList<Quote> data) {
		getStore().clear();
		getStore().addAll(data);
	}

	private ColumnModel<Quote> genColumnModel() {
		ArrayList<ColumnConfig<Quote, ?>> list = new ArrayList<>();
		list.add(rowExpander);
		list.add(refNameCC);
		list.add(new ColumnConfig<>(properties.page(), 10, "頁數"));
		list.add(new ColumnConfig<>(properties.text(), 100, "引用文字"));
		list.add(new ColumnConfig<>(properties.note(), 100, "備註"));
		list.add(new ColumnConfig<>(properties.score(), 10, "重要性"));
		ColumnModel<Quote> result = new ColumnModel<>(list);
		return result;
	}

	interface Properties extends PropertyAccess<Quote> {
		@Path("index") ModelKeyProvider<Quote> key();
		ValueProvider<Quote, String> type();
		ValueProvider<Quote, String> refName();
		ValueProvider<Quote, String> page();
		ValueProvider<Quote, String> text();
		ValueProvider<Quote, String> note();
		ValueProvider<Quote, Integer> score();
	}
}
