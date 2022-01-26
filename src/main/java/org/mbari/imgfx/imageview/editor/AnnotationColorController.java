package org.mbari.imgfx.imageview.editor;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class AnnotationColorController {
    private final HBox pane = new HBox();
    private final AnnotationColors annotationColors = new AnnotationColors();

    private final String SELECTED_KEY = "selectedColor";
    private final String EDITED_KEY = "editedColor";
    private final String DEFAULT_KEY = "defaultColor";

    public AnnotationColorController(Localizations annotations) {
        init(annotations);
    }

    private void init(Localizations annotations) {


        annotationColors.editedColorProperty().addListener((obs, oldv, newv) -> {
            var editedAnno = annotations.getEditedLocalization();
            if (editedAnno != null) {
                editedAnno.getDataView().setColor(newv);
            }
        });

        annotationColors.selectedColorProperty().addListener((obs, oldv, newv) -> {
            annotations.getSelectedLocalizations()
                    .stream()
                    .forEach(a -> a.getDataView().setColor(newv));
        });

        annotationColors.defaultColorProperty().addListener((obs, oldv, newv) -> {
            // Only change annotation colors that aren't selected
            var selected = annotations.getSelectedLocalizations();
            var collect = annotations.getLocalizations()
                    .stream()
                    .collect(Collectors.partitioningBy(selected::contains));
            collect.get(false)
                    .stream()
                    .forEach(a -> a.getDataView().setColor(newv));
        });

        var selectedPicker = new ColorPicker();
        selectedPicker.setStyle("-fx-color-label-visible: false ;");
        selectedPicker.setTooltip(new Tooltip("Selected"));
        var editedPicker = new ColorPicker();
        editedPicker.setStyle("-fx-color-label-visible: false ;");
        editedPicker.setTooltip(new Tooltip("Editing"));
        var defaultPicker = new ColorPicker();
        defaultPicker.setStyle("-fx-color-label-visible: false ;");
        defaultPicker.setTooltip(new Tooltip("Default"));

        // When a picker is set pass that color on to the AnnotationColors object
        selectedPicker.setOnAction(actionEvent ->
                annotationColors.setSelectedColor(selectedPicker.getValue()));
        editedPicker.setOnAction(actionEvent ->
                annotationColors.setEditedColor(editedPicker.getValue()));
        defaultPicker.setOnAction(actionEvent ->
                annotationColors.setDefaultColor(defaultPicker.getValue()));

        // If an annotationColor is changed update the picker
        annotationColors.selectedColorProperty()
                        .addListener((obs, oldv, newv) -> selectedPicker.setValue(newv));
        annotationColors.editedColorProperty()
                .addListener((obs, oldv, newv) -> editedPicker.setValue(newv));
        annotationColors.defaultColorProperty()
                .addListener((obs, oldv, newv) -> defaultPicker.setValue(newv));

        load();
        Runtime.getRuntime().addShutdownHook(new Thread(this::save));
        pane.getChildren().addAll(selectedPicker, editedPicker, defaultPicker);

    }

    private void load() {
        var prefs = Preferences.userNodeForPackage(getClass());
        var edited = prefs.get(EDITED_KEY, "#D65109");
        var selected = prefs.get(SELECTED_KEY, "#4BA3C3");
        var regular = prefs.get(DEFAULT_KEY, "#2C4249");

        annotationColors.setEditedColor(Color.valueOf(edited));
        annotationColors.setSelectedColor(Color.valueOf(selected));
        annotationColors.setDefaultColor(Color.valueOf(regular));
    }

    private void save() {
        var prefs = Preferences.userNodeForPackage(getClass());
        prefs.put(EDITED_KEY, annotationColors.getEditedColor().toString());
        prefs.put(SELECTED_KEY, annotationColors.getSelectedColor().toString());
        prefs.put(DEFAULT_KEY, annotationColors.getDefaultColor().toString());
    }

    public HBox getPane() {
        return pane;
    }

    public AnnotationColors getAnnotationColors() {
        return annotationColors;
    }
}
