package org.mbari.imgfx;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 *
 * @param <T> The view that we're autoscaling too. e.g. ImageView or MediaView
 */
public interface AutoscalePaneController<T extends Node> {

    Pane getPane();
    T getView();
    Autoscale<T> getAutoscale();

}
