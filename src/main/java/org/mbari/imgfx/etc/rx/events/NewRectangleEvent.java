package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.RectangleView;

public record NewRectangleEvent<V extends Node>(Localization<RectangleView, V> localization)
        implements NewLocalizationEvent<RectangleView, V> {}
