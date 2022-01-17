package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.PolygonView;

public record NewPolygonEvent<V extends Node>(Localization<PolygonView, V> localization)
        implements NewLocalizationEvent<PolygonView, V> {}
