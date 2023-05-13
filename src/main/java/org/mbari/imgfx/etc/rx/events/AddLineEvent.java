package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Localization;
import org.mbari.imgfx.roi.LineView;

public record AddLineEvent<V extends Node>(Localization<LineView, V> localization, boolean isNew)
        implements AddLocalizationEvent<LineView, V> {

    public AddLineEvent(Localization<LineView, V> localization) {
        this(localization, true);
    }
}
