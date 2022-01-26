package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;

public record HideDataViewType(Class<DataView<? extends Data, ? extends Node>> dataViewType) implements Event {
}
