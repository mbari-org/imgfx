package org.mbari.imgfx.imageview;


import javafx.scene.image.ImageView;
import org.mbari.imgfx.Autoscale;


/**
 * Wrapper around ImageView that adds Utility methods
 *
 * @author Brian Schlining
 * @since 2014-12-02T12:06:00
 */
public class ImageViewAutoscale extends Autoscale<ImageView> {

  public ImageViewAutoscale(ImageView imageView) {
    super(imageView);
    imageView.imageProperty().addListener(i -> recomputeScale());
    imageView.fitHeightProperty().addListener(i -> recomputeScale());
    imageView.fitWidthProperty().addListener(i -> recomputeScale());
    recomputeScale();
  }


  @Override
  public Double getUnscaledWidth() {
    var image = view.getImage();
    return image == null ? 0 : image.getWidth();
  }

  @Override
  public Double getUnscaledHeight() {
    var image = view.getImage();
    return image == null ? 0 : image.getHeight();
  }
}
