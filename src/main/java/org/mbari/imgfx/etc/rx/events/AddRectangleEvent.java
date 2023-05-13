package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Localization;
import org.mbari.imgfx.roi.RectangleView;

public record AddRectangleEvent<V extends Node>(Localization<RectangleView, V> localization, boolean isNew)
        implements AddLocalizationEvent<RectangleView, V> {

    public AddRectangleEvent(Localization<RectangleView, V> localization) {
        this(localization, true);
    }


}
