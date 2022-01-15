package org.mbari.imgfx.events;

import javafx.scene.shape.Shape;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;

public interface NewLocalizationEvent<T extends DataView<? extends Data, ? extends Shape>> extends Event {

    Localization<T> localization();
}
