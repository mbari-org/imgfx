package org.mbari.imgfx;

import javafx.beans.property.BooleanProperty;

/**
 * A tool is something that can be disabled/enabled.
 */
public interface Tool {

    boolean isDisabled();

    BooleanProperty disabledProperty();

    void setDisabled(boolean disabled);
}
