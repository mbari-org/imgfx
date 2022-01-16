package org.mbari.imgfx.etc.rx.events;

import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.MarkerView;

public record NewMarkerEvent(Localization<MarkerView> localization)
        implements NewLocalizationEvent<MarkerView> {}
