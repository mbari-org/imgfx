package org.mbari.imgfx.mediaview;

/*-
 * #%L
 * org.mbari.imgfx:imgfx
 * %%
 * Copyright (C) 2025 - 2026 Monterey Bay Aquarium Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import org.mbari.imgfx.AutoscalePaneController;

public class MediaPaneController implements AutoscalePaneController<MediaView> {

    private final Pane pane;
    private final MediaView mediaView;
    private final MediaViewAutoscale mediaViewDecorator;

    public MediaPaneController(MediaView mediaView) {
        this.mediaView = mediaView;
        this.mediaViewDecorator = new MediaViewAutoscale(mediaView);
        this.pane = new Pane() {
            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                double width = getWidth();
                double height = getHeight();
                // Fit the image view to match this container, the aspect ratio will be preserved
                mediaView.setFitWidth(width);
                mediaView.setFitHeight(height);
                // Adjust the image view origin to center within this container
                layoutInArea(mediaView, 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
            }
        };
        init();
    }

    private void init() {
        mediaView.setPreserveRatio(true);
        pane.setPadding(Insets.EMPTY);
        pane.setBorder(null);
        pane.getChildren().add(mediaView);
    }

    @Override
    public Pane getPane() {
        return pane;
    }

    @Override
    public MediaView getView() {
        return mediaView;
    }

    @Override
    public MediaViewAutoscale getAutoscale() {
        return mediaViewDecorator;
    }

}
