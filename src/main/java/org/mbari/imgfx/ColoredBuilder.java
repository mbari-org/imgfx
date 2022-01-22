package org.mbari.imgfx;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

/**
 * For tools that have a dislabed editor. This allows us to set the color of it
 */
public interface ColoredBuilder extends Builder {
    Color getEditColor();

    ObjectProperty<Color> editColorProperty();

    void setEditColor(Color editColor);
}
