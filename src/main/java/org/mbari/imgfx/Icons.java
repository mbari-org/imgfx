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
package org.mbari.imgfx;

import javafx.scene.text.Text;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

public enum Icons {


    ADD_LOCATION(Material2OutlinedAL.ADD_LOCATION), // Like a gmaps marker
    BOUNDING_BOX(BootstrapIcons.BOUNDING_BOX),
    CANCEL(Material2OutlinedAL.CANCEL), // circle with an x in the middle
    CHECK_BOX_OUTLINE_BLANK(Material2OutlinedAL.CHECK_BOX_OUTLINE_BLANK), // square
    CLOSE(Material2OutlinedAL.CLOSE), // an X
    CROP_LANDSCAPE(Material2OutlinedAL.CROP_LANDSCAPE), // horizontal rectangle
    DELETE(Material2OutlinedAL.DELETE), // trash can
    HEXAGON(BootstrapIcons.HEXAGON),
    HIGHLIGHT_ALT(Material2OutlinedAL.HIGHLIGHT_ALT), // dash rectangle w small arrow at lower right. Good for select
    LINEAR_SCALE(Material2OutlinedAL.LINEAR_SCALE), // line with 3 dots on it.
    PALETTE(Material2OutlinedMZ.PALETTE), //  color pallete
    PANORAMA_FISH_EYE(Material2MZ.PANORAMA_FISH_EYE), // circle
    PANORAMA_HORIZONTAL(Material2OutlinedMZ.PANORAMA_HORIZONTAL), // curved rectable, might be good for polygon
    SLASH(BootstrapIcons.SLASH),
    TRIP_ORIGIN(Material2MZ.TRIP_ORIGIN); // bolder circle

    private Ikon ikon;

    Icons(Ikon ikon) {
        this.ikon = ikon;
    }

    public Text size(int size) {
        FontIcon icon = new FontIcon(ikon);
        icon.setIconSize(size);
        icon.getStyleClass().add("glyph-icon");
        return icon;
    }

    public Text standardSize() {
        return size(30);
    }
}
