package us.dontcareabout.rqc.client.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.google.common.collect.Lists;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.rqc.client.data.Quote;
import us.dontcareabout.rqc.client.gf.VerticalLayoutLayer;

public class TagCloud extends LayerContainer {
	private static final String BY_ALPHABETICAL = "Ａ↓";
	private static final String BY_AMOUNTL = "９↓";
	private final int baseHeight = 30;
	private final SortComparator comparator = new SortComparator();

	private HashMap<String, Integer> tagMap = new HashMap<>();

	/**
	 * true 表示字母順序優先，false 表示數量優先
	 */
	private boolean type;
	private int min;
	private int diff;

	private VerticalLayoutLayer vll = new VerticalLayoutLayer();
	private SortButton sortBtn = new SortButton();

	public TagCloud() {
		addLayer(sortBtn);

		vll.setMargin(1);
		vll.setGap(1);
		vll.setLY(baseHeight + 10);
		addLayer(vll);
	}

	public void refresh(ArrayList<Quote> data) {
		tagMap.clear();

		for (Quote q : data) {
			for (String tag : q.getTag()) {
				tag = tag.substring(0, 1).toUpperCase() + tag.substring(1).toLowerCase();
				Integer amount = tagMap.get(tag);

				if (amount == null) {
					amount = 0;
				}

				amount++;
				tagMap.put(tag, amount);
			}
		}

		boolean typeTmp = type;	//為了以後重複 load 資料作準備
		type = false;
		ArrayList<String> tagList = Lists.newArrayList(tagMap.keySet());
		Collections.sort(tagList, comparator);
		min = tagMap.get(tagList.get(tagList.size() - 1));
		diff = tagMap.get(tagList.get(0)) - min;
		type = typeTmp;

		rebuildUI();
	}

	private void rebuildUI() {
		ArrayList<String> tagList = Lists.newArrayList(tagMap.keySet());
		Collections.sort(tagList, comparator);

		vll.clear();

		for (String tag : tagList) {
			vll.addChild(new TagButton(tag), ((1.0 * tagMap.get(tag) - min) / diff + 1) * baseHeight);
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

	@Override
	protected void onResize(int width, int height) {
		sortBtn.setLY(5);
		sortBtn.setLX(10);
		sortBtn.resize(width - 20, baseHeight);
		super.onResize(width, height);
	}

	private class TagButton extends TextButton {
		TagButton(String text) {
			super(text);
			setBgColor(RGB.LIGHTGRAY);
			setBgRadius(5);
		}
	}

	private class SortButton extends TextButton {
		SortButton() {
			setBgColor(RGB.YELLOW);
			setTextColor(RGB.BLUE);
			refresh();
			addSpriteSelectionHandler(new SpriteSelectionHandler() {
				@Override
				public void onSpriteSelect(SpriteSelectionEvent event) {
					changeType();
				}
			});
		}

		void refresh() {
			setText(type ? BY_AMOUNTL : BY_ALPHABETICAL);
		}
	}

	private class SortComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			//數字越大在越前面
			int amountResult = tagMap.get(o2).compareTo(tagMap.get(o1));
			int stringResult = o1.compareTo(o2);

			if (type) {
				return stringResult != 0 ? stringResult : amountResult;
			} else {
				return amountResult != 0 ? amountResult : stringResult;
			}
		}
	}
}
