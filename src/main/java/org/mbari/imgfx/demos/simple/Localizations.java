package org.mbari.imgfx.demos.simple;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.etc.rx.events.AddLocalizationEvent;
import org.mbari.imgfx.etc.rx.events.ClearLocalizations;
import org.mbari.imgfx.etc.rx.events.EditLocalizationEvent;
import org.mbari.imgfx.etc.rx.events.RemoveLocalizationEvent;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;
import org.mbari.imgfx.util.ListUtil;

import java.util.List;


public class Localizations {

    private ObservableList<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> localizations = FXCollections.observableArrayList();
    private ObservableList<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> selectedLocalizations = FXCollections.observableArrayList();
    private ObjectProperty<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> editedLocalization = new SimpleObjectProperty<>();


    public Localizations(EventBus eventBus) {
        init(eventBus);
    }

    private void init(EventBus eventBus) {

        //
        editedLocalization.addListener((obs, oldv, newv) -> {
            if (oldv != null) {
                oldv.getDataView().setEditing(false);
            }
            if (newv != null) {
                newv.getDataView().setEditing(true);
            }
        });

        eventBus.toObserverable()
                .ofType(AddLocalizationEvent.class)
                .subscribe(a -> localizations.add(a.localization()));

        eventBus.toObserverable()
                .ofType(RemoveLocalizationEvent.class)
                .subscribe(a -> {
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
                    editedLocalization.set(null);
                    selectedLocalizations.clear();
                    localizations.clear();
                });
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
}
