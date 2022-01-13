package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import org.mbari.imgfx.ImageViewDecorator;

public interface DataView<A extends Data, B extends Node> {

    A getData();
    B getView();
    ImageViewDecorator getImageViewDecorator();

    boolean isEditing();
    BooleanProperty editingProperty();
    void setEditing(boolean editing);

    void updateView();
    void updateData();


}
