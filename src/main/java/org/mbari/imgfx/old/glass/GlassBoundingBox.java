package org.mbari.imgfx.old.glass;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mbari.imgfx.old.ImagePaneController;
import org.mbari.imgfx.ImageViewDecorator;
import org.mbari.imgfx.controls.BoundingBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * TODO
 * - Adding it adds GlassRectangle
 * - On mouseover
 *  - remove GlassRectangle
 *  - add BoundingBox in parent coords
 * - on mouse exit
 *  - read bounding box coords
 *  - convert image coords
 *  - set x, y, width, height
 *  - create new GlassRectangle and add it.
 */

public class GlassBoundingBox implements GlassItem {

  // x, y, width and height are in image coordinates
  private DoubleProperty xProperty = new SimpleDoubleProperty(0);
  private DoubleProperty yProperty = new SimpleDoubleProperty(0);
  private DoubleProperty widthProperty = new SimpleDoubleProperty(0);
  private DoubleProperty heightProperty = new SimpleDoubleProperty(0);
  private BooleanProperty editProperty = new SimpleBooleanProperty(false);
  private UUID uuid = UUID.randomUUID();

  // In parent coordinates
  private final BoundingBox boundingBox;

  private final ImagePaneController pane;

  public GlassBoundingBox(ImagePaneController pane) {
    this(0, 0, 10, 10, pane);
  }

  public GlassBoundingBox(double x, double y, double width, double height, ImagePaneController pane) {
    xProperty.set(x);
    yProperty.set(y);
    widthProperty.set(width);
    heightProperty.set(height);
    this.pane = pane;
    boundingBox = new BoundingBox(0, 0, width, height);

    init();
    
  }

  private void init() {
    var imageViewExt = pane.getImageViewExt();
    xProperty.addListener((obs, oldv, newv) -> doLayout(imageViewExt));
    yProperty.addListener((obs, oldv, newv) -> doLayout(imageViewExt));
    widthProperty.addListener((obs, oldv, newv) -> doLayout(imageViewExt));
    heightProperty.addListener((obs, oldv, newv) -> doLayout(imageViewExt));
  }



  @Override
  public void doLayout(ImageViewDecorator ext) {
    var layout = ext.imageToParent(new Point2D(xProperty.get(), yProperty.get()));
    var r = boundingBox.getBoundingBoxRectangle();
    r.setWidth(widthProperty.get() * ext.getScaleX());
    r.setHeight(heightProperty.get() * ext.getScaleY());
    r.setLayoutX(layout.getX());
    r.setLayoutY(layout.getY());

  }

  @Override
  public List<Node> getNodes() {
    return boundingBox.getNodes();
  }

  @Override
  public UUID getUuid() {
    return uuid;
  }

  @Override
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public void toFront() {
    boundingBox.toFront();
  }

  @Override
  public void setColor(Color color) {
    boundingBox.setColor(color);
  }

  
  public static Optional<GlassBoundingBox> clip(double x, double y, double width, double height, ImagePaneController pane) {
    var imageViewExt = pane.getImageViewExt();
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
    var gr = new GlassBoundingBox(xx, yy, ww, hh, pane);
    return Optional.of(gr);

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

  public boolean getEdit() {
    return editProperty.get();
  }

  public void setEdit(boolean edit) {
    editProperty.set(edit);
  }

  public BooleanProperty editProperty() {
    return editProperty;
  }

  

  


  
}
