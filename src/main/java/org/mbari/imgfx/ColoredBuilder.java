package org.mbari.imgfx;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

/**
 * For tools that have a disabled editor. This allows us to set the color of it
 */
public interface ColoredBuilder<T> extends Builder<T> {
    Color getEditColor();

    ObjectProperty<Color> editColorProperty();

    void setEditColor(Color editColor);
}
