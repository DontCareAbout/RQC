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

import us.dontcareabout.gxt.client.component.Grid2;
import us.dontcareabout.gxt.client.model.GetValueProvider;
import us.dontcareabout.rqc.client.Util;
import us.dontcareabout.rqc.client.component.KeywordPanel.KeywordParam;
import us.dontcareabout.rqc.client.data.Quote;
import us.dontcareabout.rqc.client.ui.UiCenter;
import us.dontcareabout.rqc.client.ui.event.KeywordChangeEvent;
import us.dontcareabout.rqc.client.ui.event.KeywordChangeEvent.KeywordChangeHandler;
import us.dontcareabout.rqc.client.ui.event.SelectTagChangeEvent;
import us.dontcareabout.rqc.client.ui.event.SelectTagChangeEvent.SelectTagChangeHandler;
import us.dontcareabout.rqc.client.ui.event.TagConditionChangeEvent;
import us.dontcareabout.rqc.client.ui.event.TagConditionChangeEvent.TagConditionChangeHandler;

public class QuoteGrid extends Grid2<Quote> {
	private static final int firstWidth = 100;
	private static final Properties properties = GWT.create(Properties.class);

	private RowExpander<Quote> rowExpander = new RowExpander<Quote>(
		new AbstractCell<Quote>() {
			final String headerStyle = "font-size:16px;margin: 2px 50px 0px " + (firstWidth + 20) + "px;padding: 8px;background-color: #B3729F;color: white;font-weight: bold;";
			final String textStyle = 	"font-size:14px;margin: 0px 50px 4px " + (firstWidth + 20) + "px;padding: 4px 8px;border: #B3729F 3px solid;line-height: 1.5;";

			@Override
			public void render(Context context, Quote value, SafeHtmlBuilder sb) {
				process("引用文字", value.getText(), sb);
				process("備註", value.getNote(), sb);
				sb.appendHtmlConstant("<div style='height:8px'></div>");
			}

			private void process(String title, String string, SafeHtmlBuilder sb) {
				ArrayList<int[]> indexList = Util.keywordIndex(string, filter.keywords);

				sb.appendHtmlConstant("<div style='" + headerStyle + "'>" + title + "：</div>");
				sb.appendHtmlConstant("<div style='" + textStyle + "'>");

				int start = 0;
				for (int[] index : indexList) {
					sb.appendHtmlConstant(string.substring(start, index[0]));
					sb.appendHtmlConstant("<span style='background-color:yellow; padding:1px 4px; border-radius:4px'>" + string.substring(index[0], index[1]) + "</span>");
					start = index[1];
				}
				if (start != string.length()) {
					sb.appendHtmlConstant(string.substring(start, string.length()));
				}
				sb.appendHtmlConstant("</div>");
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
				filter.param = event.data;
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
		checkEmpty();
	}

	@Override
	protected ColumnModel<Quote> genColumnModel() {
		ColumnConfig<Quote, Double> score = new ColumnConfig<>(properties.score(), firstWidth, "重要性");
		score.setCell(new AbstractCell<Double>() {
			@Override
			public void render(Context context, Double value, SafeHtmlBuilder sb) {
				String color = value > 0 ? "red" : "green";
				String ratio = Math.abs(value) * 100 / 5 + "%";
				sb.appendHtmlConstant("<div style='height:16px;width:" + ratio + ";background-color:" + color + ";float:right'></div>");
			}
		});
		score.setFixed(true);

		ColumnConfig<Quote, String> page = new ColumnConfig<>(properties.page(), 80, "頁數");
		page.setFixed(true);

		ArrayList<ColumnConfig<Quote, ?>> list = new ArrayList<>();
		list.add(refNameCC);
		list.add(score);
		list.add(page);
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
				checkEmpty();
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

		if (filter.keywords == null) { return; }

		for (int i = 0; i < store.size(); i++) {
			rowExpander.expandRow(i);
		}
	}

	private void checkEmpty() {
		if (store.size() == 0) {
			mask("沒有符合條件的資料");
		} else {
			unmask();
		}
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
		KeywordParam param = new KeywordParam();	//徹底預防 NPE...... XD
		String[] keywords;	//TODO 補強 keywords 之間可能造成的矛盾，例如「tea team」

		/** @see SelectTagChangeEvent#data */
		Set<String> tagSet;

		/** @see TagConditionChangeEvent#value */
		boolean condition;

		@Override
		public boolean select(Store<Quote> store, Quote parent, Quote item) {
			boolean result = true;

			keywords = param.getKeyword() == null ?
				null : param.getKeyword().trim().split(" ");

			if (keywords != null && keywords.length != 0) {
				for (String keyword : keywords) {
					if (Strings.isNullOrEmpty(keyword)) { continue; }
					if (param.isAnd()) {
						result =
							item.getText().toLowerCase().contains(keyword.toLowerCase()) ||
							item.getNote().toLowerCase().contains(keyword.toLowerCase())
						;
						if (!result) { break; }	//因為是作 and，所以有一個沒中就不用繼續做了
					} else {
						result =
							item.getText().toLowerCase().contains(keyword.toLowerCase()) ||
							item.getNote().toLowerCase().contains(keyword.toLowerCase())
						;
						if (result) { break; }	//因為是作 or，所以有一個中就不用繼續做了
					}
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
