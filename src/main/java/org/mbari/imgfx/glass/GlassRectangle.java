package org.mbari.imgfx.glass;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mbari.imgfx.ImageViewExt;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GlassRectangle implements GlassItem {

  private final Rectangle r;
  private final List<Node> shapes;
  private final double x;
  private final double y;
  private final double width;
  private final double height;
  private UUID uuid = UUID.randomUUID();


  public GlassRectangle(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    r = new Rectangle(0, 0, width, height);
    r.setStrokeWidth(0);
    r.getStyleClass().add("mbari-bounding-box");
    r.setFill(Paint.valueOf("#FFA50080"));
    shapes = List.of(r);
  }


  public Rectangle getRectangle() {
    return r;
  }


  @Override
  public List<Node> getNodes() {
    return shapes;
  }

  @Override
  public void doLayout(ImageViewExt ext) {
    var imageBounds = ext.getImageView().getBoundsInParent();
    // var layoutX = imageBounds.getMinX() + (x * ext.getScaleX());
    // var layoutY = imageBounds.getMinY() + (y * ext.getScaleY());
    var layout = ext.imageToParent(new Point2D(x, y));
    r.setWidth(width * ext.getScaleX());
    r.setHeight(height * ext.getScaleY());

    // DO NOT use scale! The resulting transform moves the image!
    // r.setScaleX(ext.getScaleX());
    // r.setScaleY(ext.getScaleY());
    r.setLayoutX(layout.getX());
    r.setLayoutY(layout.getY());

  }

  @Override
  public void toFront() {
    r.toFront();
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public void setColor(Color color) {
    r.setFill(color);
  }


  public double getX() {
    return x;
  }


  public double getY() {
    return y;
  }


  public double getWidth() {
    return width;
  }


  public double getHeight() {
    return height;
  }


  public static Optional<GlassRectangle> clip(double x, double y, double width, double height, Image image) {
    var w = image.getWidth();
    var h = image.getHeight();
    
    if (x >= w || y >= h) {
      return Optional.empty();
    }

    if (x < 0) {
      width = width + x;
    }

    if (y < 0) {
      height = height + y;
    }
    
    var xx = Math.min(Math.max(0D, x), w - 1);
    var yy = Math.min(Math.max(0D, y), h - 1);


    var ww = Math.min(Math.max(1, width), w - xx);
    var hh = Math.min(Math.max(1, height), h - yy);

    var msg = String.format("Before: [%.1f %.1f %.1f %.1f], After: [%.1f %.1f %.1f %.1f]", x, y, width, height, xx, yy, ww, hh);
    System.out.println(msg);
    var gr = new GlassRectangle(xx, yy, ww, hh);
    return Optional.of(gr);

  }



  
  
}
