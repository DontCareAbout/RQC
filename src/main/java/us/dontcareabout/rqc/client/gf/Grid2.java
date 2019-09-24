package us.dontcareabout.rqc.client.gf;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;

public abstract class Grid2<M> extends Grid<M> {
	protected Grid2() {}

	protected abstract ListStore<M> genListStore();
	protected abstract ColumnModel<M> genColumnModel();

	protected GridView<M> genGridView() {
		return new GridView<>();
	}

	/**
	 * 實作邏輯等同於 {@link Grid#Grid(ListStore, ColumnModel, GridView)}，
	 * 原本透過參數傳入的值改為由 {@link #genListStore()} 等 method 提供。
	 */
	protected void init() {
		this.store = genListStore();
		this.cm = genColumnModel();
		this.view = genGridView();

		disabledStyle = null;
		setSelectionModel(new GridSelectionModel<M>());

		setAllowTextSelection(false);

		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		view.getAppearance().render(builder);

		setElement((Element) XDOM.create(builder.toSafeHtml()));
		getElement().makePositionable();

		sinkCellEvents();
	}
}
