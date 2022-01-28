package org.mbari.imgfx.imageview.editor;


import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.mbari.imgfx.*;
import org.mbari.imgfx.etc.javafx.controls.SelectionRectangle;
import org.mbari.imgfx.etc.rx.events.AddLocalizationEvent;
import org.mbari.imgfx.etc.rx.events.RemoveLocalizationEvent;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.roi.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ToolsPaneController {

    GridPane pane;
    private final AutoscalePaneController<ImageView> paneController;
    private final EventBus eventBus;
    private final BuilderCoordinator builderCoordinator;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final AnnotationColors annotationColors;
    private final Localizations localizations;
    private final SelectionRectangle selectionRectangle;

    public ToolsPaneController(AutoscalePaneController<ImageView> paneController,
                               EventBus eventBus,
                               AnnotationColors annotationColors,
                               Localizations localizations) {
        this.pane = new GridPane();
        this.paneController = paneController;
        this.eventBus = eventBus;
        this.annotationColors = annotationColors;
        this.localizations = localizations;
        this.builderCoordinator = new BuilderCoordinator();
        this.selectionRectangle = new SelectionRectangle(this::handleSelection);
        init();
    }

    private void init() {

        selectionRectangle.setColor(annotationColors.getSelectedColor());
        annotationColors.selectedColorProperty()
                        .addListener((obs, oldv, newv) -> selectionRectangle.setColor(newv));

        pane.getStyleClass().add("tools-pane");

        var vizLabel = new Label("Show");
        var buildLabel = new Label("Create");

        var row = 0;
        var selectionToggle = new ToggleButton();
        selectionToggle.getStyleClass().add("glyph-icon");
        selectionToggle.setGraphic(Icons.HIGHLIGHT_ALT.standardSize());
        selectionToggle.selectedProperty()
                        .addListener((obs, oldv, newv) -> {
                            var rect = selectionRectangle.getRectangle();
                            if (newv) {
                                builderCoordinator.setCurrentBuilder(null);
                                paneController.getPane()
                                        .getChildren()
                                        .add(rect);
                            }
                            else {
                                paneController.getPane()
                                        .getChildren()
                                        .remove(rect);
                            }
                        });

        selectionToggle.setToggleGroup(toggleGroup);
        pane.add(selectionToggle, 1, row);
        row++;

        var deleteButton = new Button();
        deleteButton.getStyleClass().add("glyph-icon");
        localizations.getSelectedLocalizations()
                .addListener((ListChangeListener<? super Localization<? extends DataView<? extends Data,? extends Node>,? extends Node>>) c -> {
            deleteButton.setDisable(localizations.getSelectedLocalizations().isEmpty());
        } );
        deleteButton.setGraphic(Icons.DELETE.standardSize());
        deleteButton.setOnAction(actionEvent -> {
            var selected = new ArrayList<>(localizations.getSelectedLocalizations());
            var alert = new Alert((Alert.AlertType.CONFIRMATION));
            alert.getDialogPane().getStylesheets().add("imgfx.css");
            alert.setTitle("Delete");
            alert.setHeaderText("Delete Localizations");
            alert.setContentText("Are you sure you want to delete " + selected.size() + " localizations?");
            var result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selected.forEach(loc -> eventBus.publish(new RemoveLocalizationEvent(loc)));
            }
        });
        pane.add(deleteButton, 1, row);
        row++;



        pane.add(vizLabel, 0, row);
        pane.add(buildLabel, 1, row);
        row++;


        var mb = new MarkerBuilder(paneController, eventBus);

        addBuilderToogle(Icons.CLOSE.standardSize(),
                new MarkerBuilder(paneController, eventBus), row);
        row++;

        addBuilderToogle(Icons.SLASH.standardSize(),
                new LineBuilder(paneController, eventBus), row);
        row++;

        addBuilderToogle(Icons.BOUNDING_BOX.standardSize(),
                new RectangleBuilder(paneController, eventBus), row);
        row++;

        addBuilderToogle(Icons.HEXAGON.standardSize(),
                new PolygonBuilder(paneController, eventBus), row);
        row++;


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

    public GridPane getPane() {
        return pane;
    }

    private void addBuilderToogle(Text icon, Builder<?> builder, int row) {

        var checkbox = new CheckBox();
        checkbox.selectedProperty().addListener((obs, oldv, newv) -> {
            localizations.setVisibility(builder.getBuiltType(), newv);
        });
        checkbox.setSelected(true);
        pane.add(checkbox, 0, row);

        icon.getStyleClass().add("glyph-icon");
        var button = new ToggleButton();
        button.setGraphic(icon);
        button.setOnAction(actionEvent -> {
            builderCoordinator.setCurrentBuilder(builder);
            builder.setDisabled(false);
        });
        button.setToggleGroup(toggleGroup);
        pane.add(button, 1, row);

        if (builder instanceof ColoredBuilder) {
            ((ColoredBuilder) builder).editColorProperty().bind(annotationColors.editedColorProperty());
        }
    }

    private void handleSelection(MouseEvent event) {
        var rect = selectionRectangle.getRectangle();
        var selected = localizations.getLocalizations()
                .stream()
                .filter(Localization::isVisible)
                .filter(loc -> inSelection(rect, loc.getDataView().getView()))
                .collect(Collectors.toList());
        localizations.setSelectedLocalizations(selected);

    }

    private boolean inSelection(Rectangle selection , Node node) {
        return selection.intersects(node.getBoundsInLocal());
    }
}