package org.mbari.imgfx.etc.rx.events;

import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.PolygonView;

public record NewPolygonEvent(Localization<PolygonView> localization)
        implements NewLocalizationEvent<PolygonView> {}
