package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.mbari.imgfx.AutoscalePaneController;
import org.mbari.imgfx.Builder;
import org.mbari.imgfx.etc.rx.events.AddCircleEvent;
import org.mbari.imgfx.etc.rx.EventBus;

public class CircleBuilder implements Builder<CircleView> {

    private final BooleanProperty disabled = new SimpleBooleanProperty(true);
    private final AutoscalePaneController<?> paneController;
    private final EventBus eventBus;
    private double radius = 6D;

    public CircleBuilder(AutoscalePaneController<?> paneController, EventBus eventBus) {
        this.paneController = paneController;
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        paneController.getPane().setOnMouseClicked(event -> {
            if (!disabled.get()) {
                CircleView.fromSceneCoords(event.getSceneX(), event.getSceneY(), radius, paneController.getAutoscale())
                        .ifPresent(view -> {
                            // Set the radius in the image data so the circle scales correctly with
                            // image resize
                            view.getData().setRadius(radius);
                            var loc = new Localization<>(view, paneController);
                            eventBus.publish(new AddCircleEvent(loc));
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
    public Class<CircleView> getBuiltType() {
        return CircleView.class;
    }
}
