package org.mbari.imgfx.demos.simple;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.mbari.imgfx.AutoscalePaneController;
import org.mbari.imgfx.BuilderCoordinator;
import org.mbari.imgfx.etc.rx.events.AddLocalizationEvent;
import org.mbari.imgfx.etc.rx.events.RemoveLocalizationEvent;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.tools.CircleBuilder;

public class ToolsPaneController {

    VBox root;
    private final AutoscalePaneController<ImageView> paneController;
    private final EventBus eventBus;
    private final BuilderCoordinator builderCoordinator;

    public ToolsPaneController(AutoscalePaneController<ImageView> paneController,
                               EventBus eventBus) {
        this.paneController = paneController;
        this.eventBus = eventBus;
        this.builderCoordinator = new BuilderCoordinator();
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

        eventBus.toObserverable()
                .ofType(AddLocalizationEvent.class)
                .subscribe(event -> {
                    var loc = event.localization();
                    builderCoordinator.addLocalization(loc);
                });

        eventBus.toObserverable()
                .ofType(RemoveLocalizationEvent.class)
                .subscribe(event -> {
                    var loc = event.localization();
                    builderCoordinator.removeLocalization(loc);
                });
    }

    public VBox getRoot() {
        return root;
    }
}
