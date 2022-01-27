package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;
import org.mbari.imgfx.roi.Localization;

import java.util.List;

public record UpdatedLocalizationsEvent(
        List<Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node>> localizations)
implements Event {}
