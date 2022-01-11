package org.mbari.vars.imgfx.ui;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class GlassBox implements GlassItem {

  private final Rectangle r;
  private final List<Node> shapes;

  public GlassBox(double x, double y, double width, double height) {
    r = new Rectangle(x, y, width, height);
    r.setStrokeWidth(2);
    r.getStyleClass().add("mbari-bounding-box");
    shapes = List.of(r);
  }

  @Override
  public List<Node> getShapes() {
    return shapes;
  }

  public void doLayout(ImageViewExt ext) {
    ext.transform(r);
  }

  
}
