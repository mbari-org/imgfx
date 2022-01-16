package org.mbari.imgfx.etc.rx.events;

import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.RectangleView;

public record NewRectangleEvent(Localization<RectangleView> localization)
        implements NewLocalizationEvent<RectangleView> {}
