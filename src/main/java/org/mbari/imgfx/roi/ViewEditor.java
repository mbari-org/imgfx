package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

public interface ViewEditor {

    Color DEFAULT_EDIT_COLOR = Color.valueOf("#FFA50030");

    // These usually delegated to the underlying DataView.

    /**
     *
     * @param editing true if the underlying view is in edit mode.
     *                false otherwise.
     */
    void setEditing(boolean editing);
    boolean isEditing();
    BooleanProperty editingProperty();

    void setDisable(boolean disable);
    boolean isDisable();
    BooleanProperty disableProperty();

    Color getEditColor();
    ObjectProperty<Color> editColorProperty();
    void setEditColor(Color editColor);



}
