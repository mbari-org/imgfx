package org.mbari.imgfx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.shape.Shape;
import org.mbari.imgfx.events.DeleteLocalizationEvent;
import org.mbari.imgfx.events.NewLocalizationEvent;
import org.mbari.imgfx.ext.rx.EventBus;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;
import org.mbari.imgfx.tools.Builder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocalizationController {

    private final EventBus eventBus;
    private final List<Localization<? extends DataView<? extends Data, ? extends Shape>>> localizations = new CopyOnWriteArrayList<>();
    private final List<Builder> builders = new CopyOnWriteArrayList<>();
    private final ObjectProperty<DataView<? extends Data, ? extends Shape>> currentlyEdited =
            new SimpleObjectProperty<>();

    public LocalizationController(EventBus eventBus) {
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        eventBus.toObserverable()
                .ofType(NewLocalizationEvent.class)
                .map(NewLocalizationEvent::localization)
                .subscribe(localizations::add);

        eventBus.toObserverable()
                .ofType(DeleteLocalizationEvent.class)
                .map(DeleteLocalizationEvent::localization)
                .subscribe(localizations::remove);

        currentlyEdited.addListener((obs, oldv, newv) -> {
            if (newv != null) {
                builders.forEach(b -> b.setDisabled(true));
            }
        });
    }

    public void addLocalization(Localization<? extends DataView<? extends Data, ? extends Shape>> loc) {
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

    public void removeLocalization(Localization<? extends DataView<? extends Data, ? extends Shape>> loc) {
        localizations.remove(loc);
    }

    public void addBuilder(Builder builder) {
        builders.add(builder);
    }

}
