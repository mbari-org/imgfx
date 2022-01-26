package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Localization;
import org.mbari.imgfx.roi.MarkerView;

public record AddMarkerEvent<V extends Node>(Localization<MarkerView, V> localization)
        implements AddLocalizationEvent<MarkerView, V> {}
