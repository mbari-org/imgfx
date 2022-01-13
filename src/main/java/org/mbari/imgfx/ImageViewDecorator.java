package org.mbari.imgfx;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

/**
 * Wrapper around ImageView that adds Utility methods
 * 
 * @author Brian Schlining
 * @since 2014-12-02T12:06:00
 */
public class ImageViewDecorator {

  private final ImageView imageView;

  private final DoubleProperty scaleX = new SimpleDoubleProperty(Double.NaN);
  private final DoubleProperty scaleY = new SimpleDoubleProperty(Double.NaN);

  private final ReadOnlyDoubleProperty scaleXReadOnly = ReadOnlyDoubleProperty.readOnlyDoubleProperty(scaleX);
  private final ReadOnlyDoubleProperty scaleYReadOnly = ReadOnlyDoubleProperty.readOnlyDoubleProperty(scaleY);

  public ImageViewDecorator(ImageView imageView) {
    this.imageView = imageView;
    imageView.imageProperty().addListener(i -> recomputeScale());
    imageView.fitHeightProperty().addListener(i -> recomputeScale());
    imageView.fitWidthProperty().addListener(i -> recomputeScale());
    recomputeScale();
  }

  public ImageView getImageView() {
    return imageView;
  }

  public final void recomputeScale() {
      var sx = imageView.getBoundsInParent().getWidth() / imageView.getImage().getWidth();
      var sy = imageView.getBoundsInParent().getHeight() / imageView.getImage().getHeight();
      scaleX.set(sx);
      scaleY.set(sy);
  }

  /**
   * The scale factor of the image in the X direction. If preserveRatio is true,
   * this will be the same as scaleY.
   */
  public double getScaleX() {
    return scaleX.get();
  }

  public ReadOnlyDoubleProperty scaleXProperty() {
    return scaleXReadOnly;
  }

  public void setScaleX(double scaleX) {
    this.scaleX.set(scaleX);
  }

  public double getScaleY() {
    return scaleY.get();
  }

  public ReadOnlyDoubleProperty scaleYProperty() {
    return scaleYReadOnly;
  }

  /**
   * Converts a point in the scene to a point into the unscaled image.
   * 
   * @param scene new Point2D(event.getSceneX(), event.getSceneY())
   * @return The point in the image that corresponds to the scene point,
   *        unscaled. (basically pixel coordinates into the original image)
   */
  public Point2D sceneToImage(Point2D scene) {
    var p = imageView.sceneToLocal(scene.getX(), scene.getY());
    var x = p.getX() / getScaleX();
    var y = p.getY() / getScaleY();
    return new Point2D(x, y);
  }

  /**
   * Converts a point in the unscaled image (i.e. pixel coordiats) 
   * to the correspoding point in the scene.    
   * @param image
   * @return
   */
  public Point2D imageToScene(Point2D image) {
    var p = new Point2D(image.getX() * getScaleX(), image.getY() * getScaleY());
    var p2 = imageView.localToScene(p);
    return new Point2D(p2.getX(), p2.getY());
  }

  public Point2D parentToImage(Point2D parent) {
    var imageBounds = getImageView().getBoundsInParent();
    var x = (parent.getX()  - imageBounds.getMinX()) / getScaleX();
    var y = (parent.getY() - imageBounds.getMinY()) / getScaleY();
    return new Point2D(x, y);
  }

  public Point2D imageToParent(Point2D image) {
    var imageBounds = getImageView().getBoundsInParent();
    var x = imageBounds.getMinX() + (image.getX() * getScaleX());
    var y = imageBounds.getMinY() + (image.getY() * getScaleY());
    return new Point2D(x, y);
  }



}

