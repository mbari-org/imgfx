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

public class MutableGlassRectangle implements GlassItem {

  private final Rectangle r;
  private final List<Node> shapes;
  private DoubleProperty xProperty = new SimpleDoubleProperty(0);
  private DoubleProperty yProperty = new SimpleDoubleProperty(0);
  private DoubleProperty widthProperty = new SimpleDoubleProperty(0);
  private DoubleProperty heightProperty = new SimpleDoubleProperty(0);

  private final ImageViewExt imageViewExt;
  private UUID uuid = UUID.randomUUID();


  public MutableGlassRectangle(ImageViewExt imageViewExt) {
    this(0, 0, 0, 0, imageViewExt);
  }


  public MutableGlassRectangle(double x, double y, double width, double height, ImageViewExt imageViewExt) {
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
    xProperty.addListener((obs, oldv, newv) -> doLayout(imageViewExt));
    yProperty.addListener((obs, oldv, newv) -> doLayout(imageViewExt));
    widthProperty.addListener((obs, oldv, newv) -> doLayout(imageViewExt));
    heightProperty.addListener((obs, oldv, newv) -> doLayout(imageViewExt));
    new RectangleDragHandler(this);
  }

  public ImageViewExt getImageViewExt() {
    return imageViewExt;
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
    var layout = ext.imageToParent(new Point2D(xProperty.get(), yProperty.get()));

    r.setWidth(widthProperty.get() * ext.getScaleX());
    r.setHeight(heightProperty.get() * ext.getScaleY());
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
    return xProperty.get();
  }


  public void setX(double x) {
    xProperty.set(x);
  }


  public DoubleProperty xProperty() {
    return xProperty;
  }


  public double getY() {
    return yProperty.get();
  }


  public void setY(double y) {
    yProperty.set(y);
  }

  public DoubleProperty yProperty() {
    return yProperty;
  }

  public double getWidth() {
    return widthProperty.get();
  }


  public void setWidth(double width) {
    widthProperty.set(width);
  }

  public DoubleProperty widthProperty() {
    return widthProperty;
  }


  public double getHeight() {
    return heightProperty.get();
  }


  public void setHeight(double height) {
    heightProperty.set(height);
  }

  public DoubleProperty heightProperty() {
    return heightProperty;
  }


  public static Optional<MutableGlassRectangle> clip(double x, double y, double width, double height, ImageViewExt imageViewExt) {
    var image = imageViewExt.getImageView().getImage();
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
    var gr = new MutableGlassRectangle(xx, yy, ww, hh, imageViewExt);
    return Optional.of(gr);

  }



  
  
}

