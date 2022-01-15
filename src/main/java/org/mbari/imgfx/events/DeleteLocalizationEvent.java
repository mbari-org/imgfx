package org.mbari.imgfx.events;

import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;

import java.awt.*;

public record DeleteLocalizationEvent(Localization<DataView<? extends Data, ? extends Shape>> localization) {
}
