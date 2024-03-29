package org.mbari.imgfx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;
import org.mbari.imgfx.roi.Localization;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BuilderCoordinator {

//    private final EventBus eventBus;
    private final List<Localization<? extends DataView<? extends Data, ? extends Shape>, ? extends Node>> localizations = new CopyOnWriteArrayList<>();
    private final List<Builder<?>> builders = new CopyOnWriteArrayList<>();
    private final ObjectProperty<DataView<? extends Data, ? extends Shape>> currentlyEdited =
            new SimpleObjectProperty<>();
    private final ObjectProperty<Builder<?>> currentBuilder = new SimpleObjectProperty<>();
    private static final System.Logger log = System.getLogger(BuilderCoordinator.class.getName());


    public BuilderCoordinator() {
        init();
    }

    private void init() {

        currentlyEdited.addListener((obs, oldv, newv) -> {

            // If nothing is being edited.  reenable the current editor
            if (newv == null) {
                var builder = currentBuilder.get();
                if (builder != null) {
                    builder.setDisabled(false);
                }
            }

            // If editing a dataview, disable all builders. Dont' want to create new annotations
            // while dragging or editing an exiting dataview
            if (newv != null) {
                builders.forEach(b -> b.setDisabled(true));
            }
        });

        // If the current builder isn't in the collection of builders add it.
        currentBuilder.addListener((obs, oldv, newv) -> {
            log.log(System.Logger.Level.DEBUG, "Current builder: " + newv);
//            System.out.println("BUILDER: " + newv);

            if (newv != null && !builders.contains(newv)) {
                builders.add(newv);
            }

            for (var b : builders) {
                var disable = b != newv;
                b.setDisabled(disable);
            }

        });
    }

    public void addLocalization(Localization<? extends DataView<? extends Data, ? extends Shape>, ? extends Node> loc) {
        localizations.add(loc);
        loc.getDataView()
                .editingProperty()
                .addListener((ovs, oldv, newv) -> {
                    var current = currentlyEdited.get();
                    if (current == loc.getDataView()) {
                        if (!newv) {
                            currentlyEdited.set(null);
                        }
                    }
                    else {
                        if (newv) {
                            if (current != null) {
                                current.setEditing(false);
                            }
                            currentlyEdited.set(loc.getDataView());
                        }
                    }
                });
    }

    public void removeLocalization(Localization<? extends DataView<? extends Data, ? extends Shape>, ? extends Node> loc) {
        localizations.remove(loc);
    }

    public void clearLocalizations() {
        localizations.clear();
    }

    public void addBuilder(Builder builder) {
        builders.add(builder);
    }

    public Builder<?> getCurrentBuilder() {
        return currentBuilder.get();
    }

    public ObjectProperty<Builder<?>> currentBuilderProperty() {
        return currentBuilder;
    }

    public void setCurrentBuilder(Builder<?> currentBuilder) {
        this.currentBuilder.set(currentBuilder);
    }

    public DataView<? extends Data, ? extends Shape> getCurrentlyEdited() {
        return currentlyEdited.get();
    }

    public ObjectProperty<DataView<? extends Data, ? extends Shape>> currentlyEditedProperty() {
        return currentlyEdited;
    }

    public void setCurrentlyEdited(DataView<? extends Data, ? extends Shape> currentlyEdited) {
        this.currentlyEdited.set(currentlyEdited);
    }

    public List<Builder> getBuilders() {
        return new ArrayList<>(builders);
    }
}
