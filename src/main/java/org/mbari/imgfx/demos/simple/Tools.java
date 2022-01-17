package org.mbari.imgfx.demos.simple;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.mbari.imgfx.BuilderCoordinator;
import org.mbari.imgfx.imageview.ImagePaneController;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.tools.CircleBuilder;

public class Tools {

    VBox root;
    private final ImagePaneController paneController;
    private final EventBus eventBus;
    private final BuilderCoordinator builderCoordinator;

    public Tools(ImagePaneController paneController,
                 EventBus eventBus,
                 BuilderCoordinator builderCoordinator) {
        this.paneController = paneController;
        this.eventBus = eventBus;
        this.builderCoordinator = builderCoordinator;
        init();
    }

    private void init() {
        var circleButton = new Button("Circle");
        var circleBuilder = new CircleBuilder(paneController, eventBus);
        circleButton.setOnAction(actionEvent -> {
            builderCoordinator.setCurrentBuilder(circleBuilder);
            circleBuilder.setDisabled(false);
        });
        root = new VBox(circleButton);
    }

    public VBox getRoot() {
        return root;
    }
}
