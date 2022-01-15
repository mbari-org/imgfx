package org.mbari.imgfx.tools;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.events.NewLineEvent;
import org.mbari.imgfx.ext.jfx.JFXUtil;
import org.mbari.imgfx.ext.rx.EventBus;
import org.mbari.imgfx.roi.LineView;


public class LineBuilder implements Builder {

    private final BooleanProperty disabled = new SimpleBooleanProperty(true);
    private final ImagePaneController paneController;
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


    public LineBuilder(ImagePaneController paneController, EventBus eventBus) {
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
    }

    private void build(Line line) {
        // Prevent accidents for slow clickers
        if (JFXUtil.lineLength(line) > 2) {
            LineView.fromParentCoords(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY(), paneController.getImageViewDecorator())
                    .ifPresent(view -> {
                        // TODO add editor
                        var loc = new Localization<>(view, paneController);
                        eventBus.publish(new NewLineEvent(loc));
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


}
