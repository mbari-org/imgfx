package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Localization;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;

public record EditLocalizationEvent(Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node> localization)
        implements Event {}
