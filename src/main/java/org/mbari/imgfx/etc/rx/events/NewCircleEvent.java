package org.mbari.imgfx.etc.rx.events;

import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.CircleView;

public record NewCircleEvent(Localization<CircleView> localization)
        implements NewLocalizationEvent<CircleView> {}
