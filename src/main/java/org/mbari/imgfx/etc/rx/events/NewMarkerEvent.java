package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.MarkerView;

public record NewMarkerEvent<V extends Node>(Localization<MarkerView, V> localization)
        implements NewLocalizationEvent<MarkerView, V> {}
