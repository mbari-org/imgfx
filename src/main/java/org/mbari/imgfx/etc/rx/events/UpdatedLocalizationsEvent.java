package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import javafx.scene.shape.Shape;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;
import org.mbari.imgfx.roi.Localization;

import java.util.List;

public record UpdatedLocalizationsEvent(
        List<Localization<? extends DataView<? extends Data, ? extends Shape>, ? extends Node>> localizations)
implements Event {}
