package us.dontcareabout.rqc.client.gf;

import us.dontcareabout.gxt.client.draw.LayerSprite;

public class VerticalLayoutLayer extends WeightLayoutLayer {
	@Override
	protected final void adjustMember() {
		double fixed = 0;

		for (double w : weightList) {
			if (w > 1) { fixed += w; }
		}

		double remain = getHeight() - fixed - margins.getTop() - margins.getBottom();
		double y = margins.getTop();
		double x = margins.getLeft();
		double width = getWidth() - margins.getLeft() - margins.getRight();

		for (int i = 0; i < children.size(); i++) {
			LayerSprite child = children.get(i);
			double weight = weightList.get(i);
			double height = weight > 1 ? weight : remain * weight;
			child.setLX(x);
			child.setLY(y);
			child.resize(width, height);
			y += height + gap;
		}

		totalSize = y + margins.getBottom();
	}
}
