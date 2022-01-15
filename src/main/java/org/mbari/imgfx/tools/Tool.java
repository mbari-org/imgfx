package org.mbari.imgfx.tools;

import javafx.beans.property.BooleanProperty;

public interface Tool {

    boolean isDisabled();

    BooleanProperty disabledProperty();

    void setDisabled(boolean disabled);
}
