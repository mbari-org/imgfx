package org.mbari.imgfx;

import javafx.beans.property.BooleanProperty;

public interface Tool {

    boolean isDisabled();

    BooleanProperty disabledProperty();

    void setDisabled(boolean disabled);
}
