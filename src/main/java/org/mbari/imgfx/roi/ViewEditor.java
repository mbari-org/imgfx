package org.mbari.imgfx.roi;

/*-
 * #%L
 * org.mbari.imgfx:imgfx
 * %%
 * Copyright (C) 2025 - 2026 Monterey Bay Aquarium Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
