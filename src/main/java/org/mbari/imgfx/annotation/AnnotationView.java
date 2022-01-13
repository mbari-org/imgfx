package org.mbari.imgfx.annotation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;

import java.util.UUID;

public class AnnotationView<A extends Data, B extends Node> {

    private UUID uuid;
    private UUID imageUuid;
    private final DataView<A, B> dataView;
    private final StringProperty label = new SimpleStringProperty();
    private final ImagePaneController paneController;

    public AnnotationView(DataView<A, B> dataView, ImagePaneController paneController) {
        this.dataView = dataView;
        this.paneController = paneController;
    }
}
