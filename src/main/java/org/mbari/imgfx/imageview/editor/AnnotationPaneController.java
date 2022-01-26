package org.mbari.imgfx.imageview.editor;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.mbari.imgfx.AutoscalePaneController;
import org.mbari.imgfx.etc.javafx.controls.CrossHairs;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.etc.rx.events.AddLocalizationEvent;
import org.mbari.imgfx.imageview.ImagePaneController;

import java.time.LocalTime;

public class AnnotationPaneController {

    private BorderPane pane;
    private AutoscalePaneController<ImageView> autoscalePaneController;
    private ToolsPaneController toolsPaneController;
    private AnnotationColorController annotationColorController;
    private CrossHairs crossHairs;
    private final EventBus eventBus;
    private final Localizations localizations;


    public AnnotationPaneController(EventBus eventBus) {
        this.eventBus = eventBus;
        this.localizations = new Localizations(eventBus);
        init();
    }

    private void init() {
        annotationColorController = new AnnotationColorController(localizations);
        crossHairs = new CrossHairs();
        autoscalePaneController = new ImagePaneController(new ImageView());
        toolsPaneController = new ToolsPaneController(autoscalePaneController,
                eventBus, annotationColorController.getAnnotationColors());

        var autoscalePane = autoscalePaneController.getPane();
        autoscalePane.getChildren().addAll(crossHairs.getNodes());
        pane = new BorderPane(autoscalePaneController.getPane());
        pane.setLeft(toolsPaneController.getPane());
        pane.setBottom(annotationColorController.getPane());

        // This is important or center pane doesn't shrink when stage is shrunk
        pane.setMinSize(0, 0);

        eventBus.toObserverable()
                .ofType(AddLocalizationEvent.class)
                .subscribe(event -> {
                    var loc = event.localization();
                    loc.setLabel(LocalTime.now().toString());
                    loc.getDataView().setColor(annotationColorController.getAnnotationColors().getDefaultColor());
                    loc.setVisible(true);

                });

    }

    public AutoscalePaneController<ImageView> getAutoscalePaneController() {
        return autoscalePaneController;
    }


    public BorderPane getPane() {
        return pane;
    }

    public CrossHairs getCrossHairs() {
        return crossHairs;
    }


}
