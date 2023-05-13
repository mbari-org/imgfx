package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Localization;
import org.mbari.imgfx.roi.MarkerView;

public record AddMarkerEvent<V extends Node>(Localization<MarkerView, V> localization, boolean isNew)
        implements AddLocalizationEvent<MarkerView, V> {

    public AddMarkerEvent(Localization<MarkerView, V> localization) {
        this(localization, true);
    }
}
