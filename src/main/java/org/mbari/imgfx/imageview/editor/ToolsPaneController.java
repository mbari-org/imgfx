package org.mbari.imgfx.imageview.editor;


import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.mbari.imgfx.*;
import org.mbari.imgfx.etc.rx.events.AddLocalizationEvent;
import org.mbari.imgfx.etc.rx.events.RemoveLocalizationEvent;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.roi.LineBuilder;
import org.mbari.imgfx.roi.MarkerBuilder;
import org.mbari.imgfx.roi.PolygonBuilder;
import org.mbari.imgfx.roi.RectangleBuilder;

public class ToolsPaneController {

    VBox pane;
    private final AutoscalePaneController<ImageView> paneController;
    private final EventBus eventBus;
    private final BuilderCoordinator builderCoordinator;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final AnnotationColors annotationColors;

    public ToolsPaneController(AutoscalePaneController<ImageView> paneController,
                               EventBus eventBus,
                               AnnotationColors annotationColors) {
        this.pane = new VBox();
        this.paneController = paneController;
        this.eventBus = eventBus;
        this.annotationColors = annotationColors;
        this.builderCoordinator = new BuilderCoordinator();
        init();
    }

    private void init() {

        addBuilderToogle(Icons.CLOSE.standardSize(),
                new MarkerBuilder(paneController, eventBus));

        addBuilderToogle(Icons.LINEAR_SCALE.standardSize(),
                new LineBuilder(paneController, eventBus));

        addBuilderToogle(Icons.CROP_LANDSCAPE.standardSize(),
                new RectangleBuilder(paneController, eventBus));

        addBuilderToogle(Icons.PANORAMA_HORIZONTAL.standardSize(),
                new PolygonBuilder(paneController, eventBus));


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

    public VBox getPane() {
        return pane;
    }

    private void addBuilderToogle(Text icon, Builder builder) {
        var hbox = new HBox();
        var checkbox = new CheckBox();
        checkbox.selectedProperty().addListener((obs, oldv, newv) -> {

        });
        var button = new ToggleButton();
        button.setGraphic(icon);
        button.setOnAction(actionEvent -> {
            builderCoordinator.setCurrentBuilder(builder);
            builder.setDisabled(false);
        });
        button.setToggleGroup(toggleGroup);
        pane.getChildren().add(button);

        if (builder instanceof ColoredBuilder) {
            ((ColoredBuilder) builder).editColorProperty().bind(annotationColors.editedColorProperty());
        }
    }
}
