package org.mbari.imgfx.glass;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mbari.imgfx.ImageViewExt;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GlassBoundingBox2 implements GlassItem {

  private final Rectangle r;
  private final List<Node> shapes;
  private DoubleProperty xProperty = new SimpleDoubleProperty(0);
  private DoubleProperty yProperty = new SimpleDoubleProperty(0);
  private DoubleProperty widthProperty = new SimpleDoubleProperty(0);
  private DoubleProperty heightProperty = new SimpleDoubleProperty(0);
  private final ImageViewExt imageViewExt;
  private UUID uuid = UUID.randomUUID();

  public GlassBoundingBox2(ImageViewExt imageViewExt) {
    this(0, 0, 0, 0, imageViewExt);
  }


  public GlassBoundingBox2(double x, double y, double width, double height, ImageViewExt imageViewExt) {
    xProperty.set(x);
    yProperty.set(y);
    widthProperty.set(width);
    heightProperty.set(height);
    this.imageViewExt = imageViewExt;
    r = new Rectangle(0, 0, width, height);
    r.setStrokeWidth(0);
    r.getStyleClass().add("mbari-bounding-box");
    r.setFill(Paint.valueOf("#FFA50080"));
    shapes = List.of(r);
    init();
    doLayout(imageViewExt);
  }

  private void init() {
    
  }


  
}
