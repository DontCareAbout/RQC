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
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.grid.RowExpander;

import us.dontcareabout.gxt.client.model.GetValueProvider;
import us.dontcareabout.rqc.client.data.Quote;
import us.dontcareabout.rqc.client.gf.Grid2;
import us.dontcareabout.rqc.client.ui.KeywordChangeEvent;
import us.dontcareabout.rqc.client.ui.KeywordChangeEvent.KeywordChangeHandler;
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
		UiCenter.addKeywordChange(new KeywordChangeHandler() {
			@Override
			public void onKeywordChange(KeywordChangeEvent event) {
				//空字串即使 trim() 過，split() 還是會有一個 element，所以只好也過濾一次......
				if (event.data != null && !Strings.isNullOrEmpty(event.data.trim())) {
					filter.keywords = event.data.trim().split(" ");
				} else {
					filter.keywords = null;
				}

				adjustFilter();
			}
		});
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
		getStore().replaceAll(data);
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
		final ListStore<Quote> result = new ListStore<>(properties.key());
		result.addFilter(filter);
		result.addStoreFilterHandler(new StoreFilterHandler<Quote>() {
			@Override
			public void onFilter(StoreFilterEvent<Quote> event) {
				if (result.size() == 0) {
					mask("沒有符合條件的資料");
				} else {
					unmask();
				}
			}
		});
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
		String[] keywords;

		/** @see SelectTagChangeEvent#data */
		Set<String> tagSet;

		/** @see TagConditionChangeEvent#value */
		boolean condition;

		@Override
		public boolean select(Store<Quote> store, Quote parent, Quote item) {
			boolean result = true;

			if (keywords != null && keywords.length != 0) {
				//event handler 那裡有處理過，至少會有一個 element有正常值，所以這裡一律給 false
				result = false;

				for (String keyword : keywords) {
					if (Strings.isNullOrEmpty(keyword)) { continue; }
					result = result || (
						item.getText().toLowerCase().contains(keyword.toLowerCase()) ||
						item.getNote().toLowerCase().contains(keyword.toLowerCase())
					);
					if (result) { break; }	//因為是作 or，所以有一個中就不用繼續做了
				}
			}

			//沒有指定 tag 就直接用 keyword 比對的結果
			if (tagSet == null || tagSet.isEmpty()) { return result; }

			//用「and」處理 keyword 的結果與 tag 的結果

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

				return result;
			} else {
				for (String tag : item.getTag()) {
					if (tagSet.contains(tag.toUpperCase())) { return result; }
				}

				return false;
			}
		}
	}
}
