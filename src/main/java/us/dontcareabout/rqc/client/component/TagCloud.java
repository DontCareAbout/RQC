package us.dontcareabout.rqc.client.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.google.common.collect.Lists;
import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.rqc.client.data.Quote;
import us.dontcareabout.rqc.client.gf.VerticalLayoutLayer;

public class TagCloud extends LayerContainer {
	private final int baseHeight = 30;
	private final Comparator<String> comparator = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			//數字越大在越前面
			int amountResult = tagMap.get(o2).compareTo(tagMap.get(o1));
			if (amountResult != 0) { return amountResult; }
			return o1.compareTo(o2);
		}
	};

	private HashMap<String, Integer> tagMap = new HashMap<>();
	private VerticalLayoutLayer vll = new VerticalLayoutLayer();

	public void refresh(ArrayList<Quote> data) {
		clear();
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

		ArrayList<String> tagList = Lists.newArrayList(tagMap.keySet());
		Collections.sort(tagList, comparator);

		vll = new VerticalLayoutLayer();
		vll.setMargin(1);
		vll.setGap(1);

		int min = tagMap.get(tagList.get(tagList.size() - 1));
		int diff = tagMap.get(tagList.get(0)) - min;

		for (String tag : tagList) {
			vll.addChild(new TagButton(tag), ((1.0 * tagMap.get(tag) - min) / diff + 1) * baseHeight);
		}

		addLayer(vll);
		vll.resize(this.getOffsetWidth(), 1);	//高度不重要 XD

		setPixelSize(this.getOffsetWidth(), (int)vll.getTotalSize());
	}

	private class TagButton extends TextButton {
		TagButton(String text) {
			super(text);
			setBgColor(RGB.LIGHTGRAY);
			setBgRadius(5);
		}
	}
}
