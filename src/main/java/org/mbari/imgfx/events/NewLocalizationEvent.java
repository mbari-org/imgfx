package org.mbari.imgfx.events;

import javafx.scene.shape.Shape;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.Data;

public record NewLocalizationEvent<A extends Data, B extends Shape>(Localization<A, B> localization)
        implements Event {}
