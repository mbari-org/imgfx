package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.mbari.imgfx.AutoscalePaneController;
import org.mbari.imgfx.ColoredBuilder;
import org.mbari.imgfx.etc.javafx.controls.SelectionRectangle;
import org.mbari.imgfx.etc.rx.events.AddRectangleEvent;
import org.mbari.imgfx.etc.rx.EventBus;

public class RectangleBuilder implements ColoredBuilder<RectangleView> {

    /*
     Selection rectanble
     */
    private final SelectionRectangle selectionRectangle = new SelectionRectangle();
    private final AutoscalePaneController<?> paneController;
    private final EventBus eventBus;
    private final BooleanProperty disabled = new SimpleBooleanProperty(true);
    private final EventHandler<MouseEvent> onCompleteHandler;
    private final ObjectProperty<Color> editColor = new SimpleObjectProperty<>();

    public RectangleBuilder(AutoscalePaneController<?> paneController, EventBus eventBus) {
        this.paneController = paneController;
        this.eventBus = eventBus;
        this.onCompleteHandler = buildOnCompleteHandler();
        init();
    }

    private void init() {
        selectionRectangle.setColor(editColor.get());
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

        editColor.addListener((obs, oldv, newv) -> {
            selectionRectangle.setColor(newv);
        });
    }

    private EventHandler<MouseEvent> buildOnCompleteHandler() {
        return (e) -> {
            if (!disabled.get()) {
                var decorator = paneController.getAutoscale();

                var r = selectionRectangle.getRectangle();
                RectangleView.fromParentCoords(r.getX(), r.getY(), r.getWidth(), r.getHeight(), decorator)
                        .ifPresent(view -> {
                            if (view.getData().getHeight() > 2 && view.getData().getWidth() > 2) {
                                addEditor(view);
                                var loc = new Localization<>(view, paneController);
                                eventBus.publish(new AddRectangleEvent(loc));
                            }
                        });
            }
        };

    }

    private void addEditor(RectangleView dataView) {
        var editor =  new RectangleViewEditor(dataView, paneController.getPane());
        editor.editColorProperty().bind(editColor);
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
    public Class<RectangleView> getBuiltType() {
        return RectangleView.class;
    }

}
