package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.LineView;

public record NewLineEvent<V extends Node>(Localization<LineView, V> localization)
        implements NewLocalizationEvent<LineView, V> {}
