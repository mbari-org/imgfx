package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Localization;
import org.mbari.imgfx.roi.RectangleView;

public record AddRectangleEvent<V extends Node>(Localization<RectangleView, V> localization)
        implements AddLocalizationEvent<RectangleView, V> {}
