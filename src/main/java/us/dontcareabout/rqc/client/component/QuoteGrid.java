package us.dontcareabout.rqc.client.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.grid.RowExpander;

import us.dontcareabout.gxt.client.model.GetValueProvider;
import us.dontcareabout.rqc.client.data.Quote;
import us.dontcareabout.rqc.client.gf.Grid2;
import us.dontcareabout.rqc.client.ui.SelectTagChangeEvent;
import us.dontcareabout.rqc.client.ui.SelectTagChangeEvent.SelectTagChangeHandler;
import us.dontcareabout.rqc.client.ui.TagConditionChangeEvent;
import us.dontcareabout.rqc.client.ui.TagConditionChangeEvent.TagConditionChangeHandler;
import us.dontcareabout.rqc.client.ui.UiCenter;

public class QuoteGrid extends Grid2<Quote> {
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

	private Filter filter = new Filter();

	public QuoteGrid() {
		init();
		rowExpander.initPlugin(this);
		UiCenter.addSelectTagChange(new SelectTagChangeHandler() {
			@Override
			public void onSelectTag(SelectTagChangeEvent event) {
				filter.tagSet = event.data;
				adjustFilter();
			}
		});
		UiCenter.addTagConditionChange(new TagConditionChangeHandler() {
			@Override
			public void onConditionChange(TagConditionChangeEvent event) {
				filter.condition = event.value;
				adjustFilter();
			}
		});
	}

	public void refresh(List<Quote> data) {
		getStore().clear();
		getStore().addAll(data);
	}

	@Override
	protected ColumnModel<Quote> genColumnModel() {
		ColumnConfig<Quote, Double> score = new ColumnConfig<>(properties.score(), 20, "重要性");
		score.setCell(new AbstractCell<Double>() {
			@Override
			public void render(Context context, Double value, SafeHtmlBuilder sb) {
				String color = value > 0 ? "red" : "green";
				String ratio = Math.abs(value) * 100 / 5 + "%";
				sb.appendHtmlConstant("<div style='height:16px;width:" + ratio + ";background-color:" + color + ";float:right'></div>");
			}
		});

		ArrayList<ColumnConfig<Quote, ?>> list = new ArrayList<>();
		list.add(refNameCC);
		list.add(score);
		list.add(new ColumnConfig<>(properties.page(), 10, "頁數"));
		list.add(new ColumnConfig<>(properties.text(), 120, "引用文字"));
		list.add(new ColumnConfig<>(properties.note(), 100, "備註"));
		list.add(rowExpander);
		ColumnModel<Quote> result = new ColumnModel<>(list);
		return result;
	}

	@Override
	protected ListStore<Quote> genListStore() {
		ListStore<Quote> result = new ListStore<>(properties.key());
		result.addFilter(filter);
		return result;
	}

	@Override
	protected GridView<Quote> genGridView() {
		GroupingView<Quote> view = new GroupingView<>();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.groupBy(refNameCC);
		return view;
	}

	private void adjustFilter() {
		getStore().setEnableFilters(false);
		getStore().setEnableFilters(true);
	}

	interface Properties extends PropertyAccess<Quote> {
		@Path("index") ModelKeyProvider<Quote> key();
		ValueProvider<Quote, String> type();
		ValueProvider<Quote, String> refName();
		ValueProvider<Quote, String> page();
		ValueProvider<Quote, String> text();
		ValueProvider<Quote, String> note();
		ValueProvider<Quote, Double> score();
	}

	private class Filter implements StoreFilter<Quote> {
		/** @see SelectTagChangeEvent#data */
		Set<String> tagSet;

		/** @see TagConditionChangeEvent#value */
		boolean condition;

		@Override
		public boolean select(Store<Quote> store, Quote parent, Quote item) {
			if (tagSet.isEmpty()) { return true; }

			if (condition) {
				//直接用空間換取程式行數 XD
				ArrayList<String> upperCaseTag = new ArrayList<>();

				for (String tag : item.getTag()) {
					upperCaseTag.add(tag.toUpperCase());
				}

				for (String conditionTag : tagSet) {
					if (!upperCaseTag.contains(conditionTag)) {
						return false;
					}
				}

				return true;
			} else {
				for (String tag : item.getTag()) {
					if (tagSet.contains(tag.toUpperCase())) { return true; }
				}

				return false;
			}
		}
	}
}
