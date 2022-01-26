package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Localization;
import org.mbari.imgfx.roi.PolygonView;

public record AddPolygonEvent<V extends Node>(Localization<PolygonView, V> localization)
        implements AddLocalizationEvent<PolygonView, V> {}
