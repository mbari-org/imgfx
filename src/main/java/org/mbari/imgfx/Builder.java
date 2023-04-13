package org.mbari.imgfx;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

/**
 * A builder is a type of tool that creates an instance of a given class type in the UI
 *
 * @param <T> The type that
 */
public interface Builder<T> extends Tool {

    Class<T> getBuiltType();

}
