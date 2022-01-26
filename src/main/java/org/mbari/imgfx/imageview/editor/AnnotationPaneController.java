package org.mbari.imgfx.imageview.editor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.mbari.imgfx.AutoscalePaneController;
import org.mbari.imgfx.etc.javafx.controls.CrossHairs;
import org.mbari.imgfx.etc.javafx.controls.SelectionRectangle;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.etc.rx.events.AddLocalizationEvent;
import org.mbari.imgfx.imageview.ImagePaneController;
import org.mbari.imgfx.roi.Localization;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotationPaneController {

    private BorderPane pane;
    private AutoscalePaneController<ImageView> autoscalePaneController;
    private ToolsPaneController toolsPaneController;
    private AnnotationColorController annotationColorController;
    private CrossHairs crossHairs;
    private final EventBus eventBus;
    private final Localizations localizations;
    private ObservableList<String> concepts = FXCollections.observableArrayList();



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
                eventBus,
                annotationColorController.getAnnotationColors(),
                localizations);

        var autoscalePane = autoscalePaneController.getPane();
        autoscalePane.getChildren().addAll(crossHairs.getNodes());
        pane = new BorderPane(autoscalePaneController.getPane());

        var leftPane = new VBox();
        leftPane.setSpacing(5);

        var centerWrapper0 = new HBox(toolsPaneController.getPane());
        centerWrapper0.setAlignment(Pos.CENTER);
        leftPane.getChildren().add(centerWrapper0);

        var centerWrapper1 = new HBox(annotationColorController.getPane());
        centerWrapper1.setAlignment(Pos.CENTER);
        leftPane.getChildren().add(centerWrapper1);

        pane.setLeft(leftPane);


        // This is important or center pane doesn't shrink when stage is shrunk
        pane.setMinSize(0, 0);

        eventBus.toObserverable()
                .ofType(AddLocalizationEvent.class)
                .subscribe(event -> {
                    localizations.setSelectedLocalizations(Collections.emptyList());
                    var loc = event.localization();
                    loc.setLabel(LocalTime.now().toString());
//                    loc.getDataView().setColor(annotationColorController.getAnnotationColors().getDefaultColor());
                    loc.setVisible(true);
                    localizations.setSelectedLocalizations(List.of(loc));
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

    public Localizations getLocalizations() {
        return localizations;
    }

    public ObservableList<String> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<String> concepts) {
        this.concepts.setAll(concepts);
    }
}
