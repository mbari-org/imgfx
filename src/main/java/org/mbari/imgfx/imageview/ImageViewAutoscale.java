/*
 * Copyright © 2025 MBARI (brian@mbari.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
