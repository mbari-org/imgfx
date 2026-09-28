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
package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.mbari.imgfx.AutoscalePaneController;
import org.mbari.imgfx.Builder;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.etc.rx.events.AddMarkerEvent;

public class MarkerBuilder implements Builder<MarkerView> {

    private final BooleanProperty disabled = new SimpleBooleanProperty(true);
    private final AutoscalePaneController<?> paneController;
    private final EventBus eventBus;
    private double radius = 6D;

    public MarkerBuilder(
        AutoscalePaneController<?> paneController,
        EventBus eventBus
    ) {
        this.paneController = paneController;
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        paneController
            .getPane()
            .setOnMouseClicked(event -> {
                if (!disabled.get()) {
                    MarkerView.fromSceneCoords(
                        event.getSceneX(),
                        event.getSceneY(),
                        radius,
                        paneController.getAutoscale()
                    ).ifPresent(view -> {
                        // Set the radius in the image data so the circle scales correctly with
                        // image resize
                        view.getData().setRadius(radius);
                        var loc = new Localization<>(view, paneController);
                        eventBus.publish(new AddMarkerEvent<>(loc));
                    });
                }
            });
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public boolean isDisabled() {
        return disabled.get();
    }

    @Override
    public BooleanProperty disabledProperty() {
        return disabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled.set(disabled);
    }

    @Override
    public Class<MarkerView> getBuiltType() {
        return MarkerView.class;
    }
}
