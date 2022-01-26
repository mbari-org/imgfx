package org.mbari.imgfx;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

public interface Builder<T> extends Tool {

    Class<T> getBuiltType();

}
