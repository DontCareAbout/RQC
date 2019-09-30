package us.dontcareabout.rqc.client.gf;

import us.dontcareabout.gxt.client.draw.LayerSprite;

public class HorizontalLayoutLayer extends WeightLayoutLayer {
	@Override
	protected final void adjustMember() {
		double fixed = 0;

		for (double w : weightList) {
			if (w > 1) { fixed += w; }
		}

		double remainWidth = getWidth() - fixed - margins.getLeft() - margins.getRight();
		double y = margins.getTop();
		double x = margins.getLeft();
		double height = getHeight() - margins.getTop() - margins.getBottom();

		for (int i = 0; i < children.size(); i++) {
			LayerSprite child = children.get(i);
			double weight = weightList.get(i);
			double width = weight > 1 ? weight : remainWidth * weight;
			child.setLX(x);
			child.setLY(y);
			child.resize(width, height);
			x += width;
		}
	}
}
