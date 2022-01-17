package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.scene.shape.Shape;
import org.mbari.imgfx.Autoscale;
import org.mbari.imgfx.etc.jfx.MutablePoint;

public interface DataView<A extends Data, B extends Shape> {

    A getData();
    B getView();
    Autoscale<?> getAutoscale();

    boolean isEditing();
    BooleanProperty editingProperty();
    void setEditing(boolean editing);

    void updateView();
    void updateData();

    MutablePoint getLabelLocationHint();


}
