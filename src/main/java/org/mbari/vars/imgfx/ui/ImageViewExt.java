package org.mbari.vars.imgfx.ui;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Transform;


/**
 * Wrapper around ImageView that adds Utility methods
 * 
 * @author Brian Schlining
 * @since 2014-12-02T12:06:00
 */
public class ImageViewExt {

  private final ImageView imageView;
  private double scaleX = Double.NaN;
  private double scaleY = Double.NaN;


  public ImageViewExt(ImageView imageView) {
    this.imageView = imageView;
    imageView.imageProperty().addListener(i -> recomputeScale());
    imageView.fitHeightProperty().addListener(i -> recomputeScale());
    imageView.fitWidthProperty().addListener(i -> recomputeScale());
    recomputeScale();
  }

  public ImageView getImageView() {
    return imageView;
  }

  protected final void recomputeScale() {
      scaleX = imageView.getBoundsInParent().getWidth() / imageView.getImage().getWidth();
      scaleY = imageView.getBoundsInParent().getHeight() / imageView.getImage().getHeight();
  }

  /**
   * The scale factor of the image in the X direction. If preserveRatio is true,
   * this will be the same as scaleY.
   */
  public double getScaleX() {
    return scaleX;
  }

  /**
   * The scale factor of the image in the Y direction. If preserveRatio is true,
   * this will be the same as scaleX.
   */
  public double getScaleY() {
    return scaleY;
  }

  public void transform(Node node) {
    // var bounds = node.getLayoutBounds();
    // var layoutX = imageView.getLayoutX() + bounds.getMinX() * scaleX;
    // var layoutY = imageView.getLayoutY() + bounds.getMinY() * scaleY;
    // System.out.println(String.format("""
    //   layoutX: %s
    //   layoutY: %s
    //   scaleX: %s
    //   scaleY: %s
    //   """, layoutX, layoutY, scaleX, scaleY));

    // System.out.println("imageView");
    // node.setLayoutX(layoutX);
    // node.setLayoutY(layoutY);
    // node.setScaleX(scaleX);
    // node.setScaleY(scaleY);

    var boundsInParent = imageView.getBoundsInParent();
    var layoutX = node.getLayoutBounds().getMinX() * scaleX + boundsInParent.getMinX();
    var layoutY = node.getLayoutBounds().getMinY() * scaleY + boundsInParent.getMinY();
    node.setLayoutX(layoutX);
    node.setLayoutY(layoutY);
    node.setScaleX(scaleX);
    node.setScaleY(scaleY);
  }

  /**
   * Converts a point in the scene to a point into the unscaled image.
   * 
   * @param scenePoint new Point2D(event.getSceneX(), event.getSceneY())
   * @return The point in the image that corresponds to the scene point,
   *        unscaled. (basically pixel coordinates into the original image)
   */
  public Point2D sceneToImage(Point2D scene) {
    var p = imageView.sceneToLocal(scene.getX(), scene.getY());
    var x = p.getX() / scaleX;
    var y = p.getY() / scaleY;
    return new Point2D(x, y);
  }

  /**
   * Converts a point in the unscaled image (i.e. pixel coordiats) 
   * to the correspoding point in the scene.    
   * @param image
   * @return
   */
  public Point2D imageToScene(Point2D image) {
    var p = new Point2D(image.getX() * scaleX, image.getY() * scaleY);
    var p2 = imageView.localToScene(p);
    return new Point2D(p2.getX(), p2.getY());
  }
}

