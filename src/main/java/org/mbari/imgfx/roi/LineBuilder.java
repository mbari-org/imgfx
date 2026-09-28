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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.mbari.imgfx.AutoscalePaneController;
import org.mbari.imgfx.ColoredBuilder;
import org.mbari.imgfx.etc.rx.events.AddLineEvent;
import org.mbari.imgfx.etc.javafx.JFXUtil;
import org.mbari.imgfx.etc.rx.EventBus;


public class LineBuilder implements ColoredBuilder<LineView> {

    private final BooleanProperty disabled = new SimpleBooleanProperty(true);
    private final AutoscalePaneController<?> paneController;
    private final EventBus eventBus;
    private final Line line = new Line();
    private boolean isBuilding = false;
    private EventHandler<MouseEvent> clickedEvent;
    private final EventHandler<MouseEvent> motionEvent = (event) -> {
        if (!isDisabled() && isBuilding) {
            line.setEndX(event.getX());
            line.setEndY(event.getY());
        }
    };
    private final ObjectProperty<Color> editColor = new SimpleObjectProperty<>();

    public LineBuilder(AutoscalePaneController<?> paneController, EventBus eventBus) {
        this.paneController = paneController;
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        clickedEvent = buildMouseClickHandler();
        paneController.getPane().addEventHandler(MouseEvent.MOUSE_CLICKED, clickedEvent);
        paneController.getPane().addEventHandler(MouseEvent.MOUSE_MOVED, motionEvent);
        disabledProperty().addListener((obs, oldv, newv) -> {
            if (newv) {
                paneController.getPane().getChildren().remove(line);
            }
            else {
                paneController.getPane().getChildren().add(line);
            }
        });
        line.strokeProperty().bind(editColor);
        line.setStrokeWidth(2);

        var pane = paneController.getPane();
        final EventHandler<KeyEvent> keyEvent = (event) -> {
            // Stop if any key is pressed
            if (!isDisabled() && isBuilding) {
                isBuilding = false;
                line.setVisible(false);
            }
        };
        pane.sceneProperty().addListener((obs, oldv, newv) -> {
            if (oldv != null) {
                oldv.removeEventHandler(KeyEvent.KEY_TYPED, keyEvent);
            }
            if (newv != null) {
                newv.addEventHandler(KeyEvent.KEY_TYPED, keyEvent);
            }
        });


    }

    private void build(Line line) {
        // Prevent accidents for slow clickers
        if (JFXUtil.lineLength(line) > 2) {
            LineView.fromParentCoords(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY(), paneController.getAutoscale())
                    .ifPresent(view -> {
                        // TODO add editor
                        var loc = new Localization<>(view, paneController);
                        eventBus.publish(new AddLineEvent(loc));
                    });
        }
    }

    public boolean isDisabled() {
        return disabled.get();
    }

    @Override
    public BooleanProperty disabledProperty() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled.set(disabled);
    }

    private EventHandler<MouseEvent> buildMouseClickHandler() {
        return (event) -> {
            if (!isDisabled()) {
                if(event.getButton() == MouseButton.SECONDARY) {
                    isBuilding = false;
                    line.setVisible(false);
                }
                else {
                    if (!isBuilding) {
                        isBuilding = true;
                        line.setStartX(event.getX());
                        line.setStartY(event.getY());
                        line.setEndX(event.getX());
                        line.setEndY(event.getY());
                        line.setVisible(true);
                    } else {
                        isBuilding = false;
                        build(line);
                        line.setVisible(false);
                    }
                }
            }
        };
    }

    @Override
    public Color getEditColor() {
        return editColor.get();
    }

    @Override
    public ObjectProperty<Color> editColorProperty() {
        return editColor;
    }

    @Override
    public void setEditColor(Color editColor) {
        this.editColor.set(editColor);
    }

    @Override
    public Class<LineView> getBuiltType() {
        return LineView.class;
    }
}
