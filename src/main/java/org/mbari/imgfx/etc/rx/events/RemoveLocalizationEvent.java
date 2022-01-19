package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import javafx.scene.shape.Shape;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;



public record RemoveLocalizationEvent(Localization<? extends DataView<? extends Data, ? extends Shape>, ? extends Node> localization) {
}
