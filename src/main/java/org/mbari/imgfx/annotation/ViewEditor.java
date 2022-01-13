package org.mbari.imgfx.annotation;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

public interface ViewEditor {

    Color DEFAULT_EDIT_COLOR = Color.valueOf("#FFA50030");

    void setEditing(boolean editing);
    boolean isEditing();

    Color getEditColor();
    ObjectProperty<Color> editColorProperty();
    void setEditColor(Color editColor);



}
