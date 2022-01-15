package org.mbari.imgfx.events;

import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.LineView;
import org.mbari.imgfx.roi.LineView;

public record NewLineEvent(Localization<LineView> localization)
        implements NewLocalizationEvent<LineView> {}
