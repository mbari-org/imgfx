/*
 * Copyright © 2025 MBARI (brian@mbari.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mbari.imgfx.demos.imageview.editor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;


public class AnnotationColors {
    private final ObjectProperty<Color> editedColor = new SimpleObjectProperty(Color.valueOf("#D65109"));
    private final ObjectProperty<Color> defaultColor = new SimpleObjectProperty(Color.valueOf("#4BA3C3"));
    private final ObjectProperty<Color> selectedColor = new SimpleObjectProperty(Color.valueOf("#2C4249"));

    public Color getEditedColor() {
        return editedColor.get();
    }

    public ObjectProperty<Color> editedColorProperty() {
        return editedColor;
    }

    public void setEditedColor(Color editedColor) {
        this.editedColor.set(editedColor);
    }

    public Color getDefaultColor() {
        return defaultColor.get();
    }

    public ObjectProperty<Color> defaultColorProperty() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor.set(defaultColor);
    }

    public Color getSelectedColor() {
        return selectedColor.get();
    }

    public ObjectProperty<Color> selectedColorProperty() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor.set(selectedColor);
    }
}
