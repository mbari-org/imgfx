package org.mbari.imgfx;

import javafx.scene.layout.HBox;

public class LocalizationPaneController {

    private HBox pane;
    private ImagePaneController imagePaneController;

    public LocalizationPaneController() {
        init();
    }

    private void init() {
        pane = new HBox();
    }

}
