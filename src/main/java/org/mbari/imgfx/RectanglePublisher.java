package org.mbari.imgfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.mbari.imgfx.controls.SelectionRectangle;
import org.mbari.imgfx.events.NewLocalizationEvent;
import org.mbari.imgfx.roi.RectangleData;
import org.mbari.imgfx.roi.RectangleView;
import org.mbari.imgfx.roi.RectangleViewEditor;
import org.mbari.imgfx.ext.rx.EventBus;

public class RectanglePublisher {

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
                var sceneXY = new Point2D(r.getX(), r.getY());
                var imageXY = decorator.sceneToImage(sceneXY);
                var width = r.getWidth() / decorator.getScaleX();
                var height = r.getHeight() / decorator.getScaleX();

                var opt = RectangleData.clip(imageXY.getX(),
                        imageXY.getY(),
                        width,
                        height,
                        paneController.getImageView().getImage());
                opt.ifPresent(data -> {
                    var view = new RectangleView(data, decorator);
                    var localization = new Localization<>(view, paneController);

                    var shape = view.getView();
                    shape.setFill(Paint.valueOf("#4FC3F730"));
                    shape.toFront();

                    new RectangleViewEditor(view, pane);
                    view.setEditing(true);
                    eventBus.publish(new NewLocalizationEvent<>(localization));

                });
            }
        };

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
