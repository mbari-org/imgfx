package org.mbari.imgfx.old.glass;

import java.util.List;
import java.util.UUID;
import org.mbari.imgfx.ImageViewDecorator;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class GlassCircle implements GlassItem {

  private final Circle circle;
  private final List<Node> nodes;
  private UUID uuid = UUID.randomUUID();
  private final double x;
  private final double y;
  private final double radius;

  public GlassCircle(double x, double y, double radius) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    circle = new Circle(0, 0, radius);
    circle.setStrokeWidth(0);
    circle.setFill(Paint.valueOf("#FFA50080"));
    nodes = List.of(circle);
  }


  @Override
  public void doLayout(ImageViewDecorator ext) {
    var layout = ext.imageToParent(new Point2D(x, y));
    circle.setRadius(radius * ext.getScaleX());
    circle.setLayoutX(layout.getX());
    circle.setLayoutY(layout.getY());
  }

  @Override
  public List<Node> getNodes() {
    return nodes;
  }

  @Override
  public UUID getUuid() {
    return uuid;
  }

  @Override
  public void setColor(Color color) {
    circle.setFill(color);
  }

  @Override
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public void toFront() {
    circle.toFront();
  }

  public Circle getCircle() {
    return circle;
  }
  
}
