package us.dontcareabout.rqc.client.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.rqc.client.data.Quote;
import us.dontcareabout.rqc.client.gf.HorizontalLayoutLayer;
import us.dontcareabout.rqc.client.gf.VerticalLayoutLayer;
import us.dontcareabout.rqc.client.ui.UiCenter;
import us.dontcareabout.rqc.client.ui.event.TagConditionChangeEvent;

public class TagCloud extends LayerContainer {
	private static final String BY_ALPHABETICAL = "Ａ↓";
	private static final String BY_AMOUNTL = "９↓";
	private static final String BY_AND = "AND";
	private static final String BY_OR = "O R";	//TODO 等 GF 修正完 TextButton.setText() 的重新置中問題

	private final int baseHeight = 30;
	private final SortComparator comparator = new SortComparator();

	private HashMap<String, TagStatus> tagMap = new HashMap<>();

	/**
	 * true 表示字母順序優先，false 表示數量優先
	 */
	private boolean type;
	private int min;
	private int diff;

	private VerticalLayoutLayer vll = new VerticalLayoutLayer();
	private HorizontalLayoutLayer hll = new HorizontalLayoutLayer();
	private SortButton sortBtn = new SortButton();
	private ConditionButton conditionBtn = new ConditionButton();

	public TagCloud() {
		hll.addChild(sortBtn, 0.5);;
		hll.addChild(conditionBtn, 0.5);
		hll.setMargin(1);
		hll.setGap(4);
		addLayer(hll);

		vll.setMargin(1);
		vll.setGap(1);
		vll.setLY(baseHeight + 4);
		addLayer(vll);
	}

	public void refresh(List<Quote> data) {
		tagMap.clear();

		for (Quote q : data) {
			for (String tag : q.getTag()) {
				tag = tag.substring(0, 1).toUpperCase() + tag.substring(1).toLowerCase();
				TagStatus status = tagMap.get(tag);

				if (status == null) {
					status = new TagStatus();;
					tagMap.put(tag, status);
				}

				status.amount++;
			}
		}

		boolean typeTmp = type;	//為了以後重複 load 資料作準備
		type = false;
		ArrayList<String> tagList = Lists.newArrayList(tagMap.keySet());
		Collections.sort(tagList, comparator);
		min = tagMap.get(tagList.get(tagList.size() - 1)).amount;
		diff = tagMap.get(tagList.get(0)).amount - min;
		type = typeTmp;

		rebuildUI();
	}

	@Override
	protected void onResize(int width, int height) {
		hll.resize(width, baseHeight);
		super.onResize(width, height);
	}

	private void rebuildUI() {
		ArrayList<String> tagList = Lists.newArrayList(tagMap.keySet());
		Collections.sort(tagList, comparator);

		vll.clear();

		for (String tag : tagList) {
			TagStatus status = tagMap.get(tag);
			vll.addChild(new TagButton(tag, status.select), ((1.0 * status.amount - min) / diff + 1) * baseHeight);
		}

		vll.redeploy();
		vll.resize(this.getOffsetWidth(), 1);	//高度不重要 XD

		setPixelSize(this.getOffsetWidth(), (int)vll.getTotalSize() + baseHeight + 10);
	}

	private void changeType() {
		type = !type;
		sortBtn.refresh();
		rebuildUI();
	}

	private class TagButton extends TextButton {
		boolean select;

		TagButton(String text, boolean isSelect) {
			super(text);
			select = isSelect;
			setBgRadius(5);
			refresh();
			addSpriteSelectionHandler(new SpriteSelectionHandler() {
				@Override
				public void onSpriteSelect(SpriteSelectionEvent event) {
					select = !select;
					refresh();
					tagMap.get(getText()).select = select;
					UiCenter.selectTagChange(getText(), select);
				}
			});
		}

		void refresh() {
			setBgColor(select ? RGB.RED : RGB.LIGHTGRAY);
			setTextColor(select ? RGB.WHITE : RGB.BLACK);
		}
	}

	private class SortButton extends TextButton {
		SortButton() {
			setBgColor(RGB.BLACK);
			setTextColor(RGB.WHITE);
			refresh();
			addSpriteSelectionHandler(new SpriteSelectionHandler() {
				@Override
				public void onSpriteSelect(SpriteSelectionEvent event) {
					changeType();
				}
			});
		}

		void refresh() {
			setText(type ? BY_ALPHABETICAL : BY_AMOUNTL);
		}
	}

	private class ConditionButton extends TextButton {
		/** @see TagConditionChangeEvent#value */
		boolean condition;

		ConditionButton() {
			setBgColor(RGB.BLACK);
			setTextColor(RGB.WHITE);
			refresh();
			addSpriteSelectionHandler(new SpriteSelectionHandler() {
				@Override
				public void onSpriteSelect(SpriteSelectionEvent event) {
					condition = !condition;
					refresh();
					UiCenter.tagConditionChange(condition);
				}
			});
		}

		void refresh() {
			setText(condition ? BY_AND : BY_OR);
		}
	}

	private class SortComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			//數字越大在越前面
			int amountResult = tagMap.get(o2).amount.compareTo(tagMap.get(o1).amount);
			int stringResult = o1.compareTo(o2);

			if (type) {
				return stringResult != 0 ? stringResult : amountResult;
			} else {
				return amountResult != 0 ? amountResult : stringResult;
			}
		}
	}

	private class TagStatus {
		Integer amount = 0;
		boolean select;
	}
}
