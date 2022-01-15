package org.mbari.imgfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.mbari.imgfx.controls.SelectionRectangle;
import org.mbari.imgfx.events.NewLocalizationEvent;
import org.mbari.imgfx.events.NewRectangleEvent;
import org.mbari.imgfx.roi.RectangleData;
import org.mbari.imgfx.roi.RectangleView;
import org.mbari.imgfx.roi.RectangleViewEditor;
import org.mbari.imgfx.ext.rx.EventBus;

import java.time.LocalTime;

public class RectanglePublisher {

    /*
     Selection rectanble
     */
    private final SelectionRectangle selectionRectangle = new SelectionRectangle();
    private final ImagePaneController paneController;
    private final EventBus eventBus;
    private final BooleanProperty disable = new SimpleBooleanProperty(true);
    private final EventHandler<MouseEvent> onCompleteHandler;

    public RectanglePublisher(ImagePaneController paneController, EventBus eventBus) {
        this.paneController = paneController;
        this.eventBus = eventBus;
        this.onCompleteHandler = buildOnCompleteHandler();
        init();
    }

    private void init() {
        selectionRectangle.setOnCompleteHandler(onCompleteHandler);
        disableProperty().addListener((obs, oldv, newv) -> {
            if (newv) {
                paneController.getPane().getChildren().remove(selectionRectangle.getRectangle());
            }
            else {
                paneController.getPane().getChildren().add(selectionRectangle.getRectangle());
            }
        });
    }

    private EventHandler<MouseEvent> buildOnCompleteHandler() {
        var pane = paneController.getPane();
        return (e) -> {
            if (!disable.get()) {
                var decorator = paneController.getImageViewDecorator();

                var r = selectionRectangle.getRectangle();
                RectangleView.fromSceneCoords(r.getX(), r.getY(), r.getWidth(), r.getHeight(), decorator)
                        .ifPresent(view -> {
                            if (view.getData().getHeight() > 2 && view.getData().getWidth() > 2) {
                                addEditor(view);
                                var loc = new Localization<>(view, paneController, LocalTime.now().toString());
                                eventBus.publish(new NewRectangleEvent(loc));
                            }
                        });
            }
        };

    }

    private void addEditor(RectangleView dataView) {
        var editor = new RectangleViewEditor(dataView, paneController.getPane());
        paneController.getPane()
                .getScene()
                .addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                    var clickPoint = new Point2D(event.getSceneX(), event.getSceneY());
                    var doingEdits = editor.getNodes()
                            .stream()
                            .anyMatch(n -> n.contains(clickPoint));
                    editor.setEditing(doingEdits);
                    setDisable(doingEdits);
                    if (!doingEdits) {
                        selectionRectangle.setDragStart(clickPoint);
                    }
                });
    }

    public boolean isDisable() {
        return disable.get();
    }

    public BooleanProperty disableProperty() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable.set(disable);
    }

    Rectangle getView() {
        return selectionRectangle.getRectangle();
    }
}
