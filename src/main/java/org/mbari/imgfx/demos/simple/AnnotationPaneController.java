package org.mbari.imgfx.demos.simple;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.mbari.imgfx.AutoscalePaneController;
import org.mbari.imgfx.etc.jfx.controls.CrossHairs;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.imageview.ImagePaneController;

public class AnnotationPaneController {

    private BorderPane root;
    private AutoscalePaneController<ImageView> paneController;
    private CrossHairs crossHairs;
    private final EventBus eventBus;

    public AnnotationPaneController(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    private void init() {
        crossHairs = new CrossHairs();
        paneController = new ImagePaneController(new ImageView());
        var pane = paneController.getPane();
        pane.getChildren().addAll(crossHairs.getNodes());
        root = new BorderPane(paneController.getPane());
    }

    public AutoscalePaneController<ImageView> getAutoscalePaneController() {
        return paneController;
    }


    public BorderPane getRoot() {
        return root;
    }

    public CrossHairs getCrossHairs() {
        return crossHairs;
    }


}
