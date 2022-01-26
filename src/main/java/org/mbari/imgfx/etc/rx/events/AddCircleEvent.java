package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Localization;
import org.mbari.imgfx.roi.CircleView;

public record AddCircleEvent<V extends Node>(Localization<CircleView, V> localization)
        implements AddLocalizationEvent<CircleView, V> {}
