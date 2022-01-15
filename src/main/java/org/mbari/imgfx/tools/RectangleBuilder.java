package org.mbari.imgfx.tools;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.ext.jfx.controls.SelectionRectangle;
import org.mbari.imgfx.events.NewRectangleEvent;
import org.mbari.imgfx.roi.RectangleView;
import org.mbari.imgfx.roi.RectangleViewEditor;
import org.mbari.imgfx.ext.rx.EventBus;

import java.time.LocalTime;

public class RectangleBuilder implements Builder {

    /*
     Selection rectanble
     */
    private final SelectionRectangle selectionRectangle = new SelectionRectangle();
    private final ImagePaneController paneController;
    private final EventBus eventBus;
    private final BooleanProperty disabled = new SimpleBooleanProperty(true);
    private final EventHandler<MouseEvent> onCompleteHandler;

    public RectangleBuilder(ImagePaneController paneController, EventBus eventBus) {
        this.paneController = paneController;
        this.eventBus = eventBus;
        this.onCompleteHandler = buildOnCompleteHandler();
        init();
    }

    private void init() {
        selectionRectangle.setOnCompleteHandler(onCompleteHandler);
        disabledProperty().addListener((obs, oldv, newv) -> {
            if (newv) {
                paneController.getPane()
                        .getChildren()
                        .remove(selectionRectangle.getRectangle());
            }
            else {
                paneController.getPane()
                        .getChildren()
                        .add(selectionRectangle.getRectangle());
            }
        });
    }

    private EventHandler<MouseEvent> buildOnCompleteHandler() {
        return (e) -> {
            if (!disabled.get()) {
                var decorator = paneController.getImageViewDecorator();

                var r = selectionRectangle.getRectangle();
                RectangleView.fromParentCoords(r.getX(), r.getY(), r.getWidth(), r.getHeight(), decorator)
                        .ifPresent(view -> {
                            if (view.getData().getHeight() > 2 && view.getData().getWidth() > 2) {
                                addEditor(view);
                                var loc = new Localization<>(view, paneController);
                                eventBus.publish(new NewRectangleEvent(loc));
                            }
                        });
            }
        };

    }

    private void addEditor(RectangleView dataView) {
        var editor = new RectangleViewEditor(dataView, paneController.getPane());
    }

    public boolean isDisabled() {
        return disabled.get();
    }

    public BooleanProperty disabledProperty() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled.set(disabled);
    }

    Rectangle getView() {
        return selectionRectangle.getRectangle();
    }
}
