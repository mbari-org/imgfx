package org.mbari.imgfx.tools;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.events.NewMarkerEvent;
import org.mbari.imgfx.ext.rx.EventBus;
import org.mbari.imgfx.roi.MarkerView;

public class MarkerBuilder implements Builder {

    private final BooleanProperty disabled = new SimpleBooleanProperty(true);
    private final ImagePaneController paneController;
    private final EventBus eventBus;
    private double radius = 6D;

    public MarkerBuilder(ImagePaneController paneController, EventBus eventBus) {
        this.paneController = paneController;
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        paneController.getPane().setOnMouseClicked(event -> {
            if (!disabled.get()) {
                MarkerView.fromSceneCoords(event.getSceneX(), event.getSceneY(), radius, paneController.getImageViewDecorator())
                        .ifPresent(view -> {
                            // Set the radius in the image data so the circle scales correctly with
                            // image resize
                            view.getData().setRadius(radius);
                            var loc = new Localization<>(view, paneController);
                            eventBus.publish(new NewMarkerEvent(loc));
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
}
