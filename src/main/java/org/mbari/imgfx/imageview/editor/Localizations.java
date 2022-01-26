package org.mbari.imgfx.imageview.editor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import org.mbari.imgfx.etc.rx.events.*;
import org.mbari.imgfx.roi.Localization;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;
import org.mbari.imgfx.util.ListUtil;

import java.util.List;


public class Localizations {

    private ObservableList<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> localizations = FXCollections.observableArrayList();
    private ObservableList<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> selectedLocalizations = FXCollections.observableArrayList();
    private ObjectProperty<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> editedLocalization = new SimpleObjectProperty<>();
    private ObservableMap<Class<? extends DataView<? extends Data, ? extends Node>>, Boolean> visibleDataViewTypes =
            FXCollections.observableHashMap();

    public Localizations(EventBus eventBus) {
        init(eventBus);
    }

    private void init(EventBus eventBus) {

        // Edited localizations are always visible
        editedLocalization.addListener((obs, oldv, newv) -> {
            if (oldv != null) {
                oldv.getDataView().setEditing(false);
                var visible = visibleDataViewTypes.getOrDefault(oldv.getDataView().getClass(), true);
                oldv.setVisible(visible);
            }
            if (newv != null) {
                newv.getDataView().setEditing(true);
                newv.setVisible(true);
            }
        });

        visibleDataViewTypes.addListener((MapChangeListener<? super Class<? extends DataView<? extends Data,? extends Node>>, ? super Boolean>) c -> {
            updateVisibility();
        });

        eventBus.toObserverable()
                .ofType(AddLocalizationEvent.class)
                .subscribe(a -> {
                    localizations.add(a.localization());
                    var visible = visibleDataViewTypes.getOrDefault(a.localization().getDataView().getClass(), true);
                    a.localization().setVisible(visible);
                });

        eventBus.toObserverable()
                .ofType(RemoveLocalizationEvent.class)
                .subscribe(a -> {
                    a.localization().setVisible(false);
                    localizations.remove(a.localization());
                    selectedLocalizations.remove(a.localization());

                    var edited = editedLocalization.get();
                    if (edited != null && edited == a.localization()) {
                        editedLocalization.set(null);
                    }

                });

        eventBus.toObserverable()
                .ofType(EditLocalizationEvent.class)
                .subscribe(a -> editedLocalization.set(a.localization()));

        eventBus.toObserverable()
                .ofType(ClearLocalizations.class)
                .subscribe(a -> {
                    localizations.forEach(loc -> loc.setVisible(false));
                    editedLocalization.set(null);
                    selectedLocalizations.clear();
                    localizations.clear();
                });

        eventBus.toObserverable()
                .ofType(ShowDataViewType.class)
                .subscribe(show -> setVisibility(show.dataViewType(), true));

        eventBus.toObserverable()
                .ofType(HideDataViewType.class)
                .subscribe(hide -> setVisibility(hide.dataViewType(), false));
    }

    public ObservableList<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> getLocalizations() {
        return localizations;
    }

    public void setLocalizations(List<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> localizations) {
        this.localizations.setAll(localizations);
    }

    public ObservableList<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> getSelectedLocalizations() {
        return selectedLocalizations;
    }

    public void setSelectedLocalizations(List<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> selectedLocalizations) {
        // Get intersection with current annotations.
        var existingAnnotations = ListUtil.intersection(localizations, selectedLocalizations);
        this.selectedLocalizations.setAll(existingAnnotations);
    }

    public Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node> getEditedLocalization() {
        return editedLocalization.get();
    }

    public ObjectProperty<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> editedLocalizationProperty() {
        return editedLocalization;
    }

    public void setEditedLocalization(Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node> editedLocalization) {
        this.editedLocalization.set(editedLocalization);
    }

    public ObservableMap<Class<? extends DataView<? extends Data, ? extends Node>>, Boolean> getVisibleDataViewTypes() {
        return visibleDataViewTypes;
    }

    public void setVisibility(Class<DataView<? extends Data, ? extends Node>> dataViewType, Boolean visible) {
        visibleDataViewTypes.put(dataViewType, visible);
    }

    private void updateVisibility() {
        for (var viz : visibleDataViewTypes.entrySet()) {
            var clazz = viz.getKey();
            var visible = viz.getValue();
            localizations.stream()
                    .filter(loc -> loc.getDataView().getClass() == clazz)
                    .forEach(loc -> loc.setVisible(visible));
        }
    }
}
