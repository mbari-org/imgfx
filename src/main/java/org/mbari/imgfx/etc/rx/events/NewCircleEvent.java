package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.CircleView;

public record NewCircleEvent<V extends Node>(Localization<CircleView, V> localization)
        implements NewLocalizationEvent<CircleView, V> {}
