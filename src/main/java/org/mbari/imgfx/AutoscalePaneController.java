package org.mbari.imgfx;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Some nodes are contained in a pane. This interface provices access to the parent pane
 * and child view as well as the autoscale instance that manages resizing of the view based
 * on changes in the parent pane.
 *
 * @param <T> The view that we're autoscaling too. e.g. ImageView or MediaView
 */
public interface AutoscalePaneController<T extends Node> {

    Pane getPane();
    T getView();
    Autoscale<T> getAutoscale();

}
