package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;

import java.awt.*;

public record DeleteLocalizationEvent(Localization<DataView<? extends Data, ? extends Shape>, ? extends Node> localization) {
}
