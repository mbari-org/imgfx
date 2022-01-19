package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.LineView;

public record AddLineEvent<V extends Node>(Localization<LineView, V> localization)
        implements AddLocalizationEvent<LineView, V> {}
