package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import org.mbari.imgfx.Autoscale;
import org.mbari.imgfx.etc.jfx.MutablePoint;

import java.util.Optional;

public class CircleView implements DataView<CircleData, Circle> {

    private final CircleData data;
    private final Circle view;
    private final Autoscale<?> autoscale;
    private final BooleanProperty editing = new SimpleBooleanProperty();
    private final MutablePoint labelLocationHint = new MutablePoint();

    public CircleView(CircleData data, Autoscale<?> autoscale) {
        this.data = data;
        this.autoscale = autoscale;
        this.view = new Circle();
        init();
    }

    private void init() {
        EditingDecorator.decorate(this);

        ChangeListener<? super Number> dataChangeListener = (obs, oldv, newv) -> {
            if (!editing.get()) {
                updateView();
            }
        };
        data.centerXProperty().addListener(dataChangeListener);
        data.centerXProperty().addListener(dataChangeListener);
        data.radiusProperty().addListener(dataChangeListener);

        ChangeListener<? super Number> viewChangeListener = (obs, oldv, newv) -> {
            if (editing.get()) {
                updateData();
            }
        };
        view.centerXProperty().addListener(viewChangeListener);
        view.centerXProperty().addListener(viewChangeListener);
        view.radiusProperty().addListener(viewChangeListener);

        // Label at top of circle
        labelLocationHint.xProperty().bind(view.centerXProperty());
        labelLocationHint.yProperty().bind(view.centerYProperty().subtract(view.radiusProperty()));

    }

    public void updateData() {
        var imageXY = autoscale.parentToUnscaled(new Point2D(view.getCenterX(), view.getCenterY()));
        var imageRadius = view.getRadius() / autoscale.getScaleX();
        var opt = CircleData.clip(imageXY.getX(),
                imageXY.getY(),
                imageRadius,
                autoscale);
        if (opt.isPresent()) {
            var c = opt.get();
            data.setCenterX(c.getCenterX());
            data.setCenterY(c.getCenterY());
            data.setRadius(c.getRadius());
        }
        else {
            data.setCenterX(0);
            data.setCenterY(0);
            data.setRadius(0);
        }

        updateView();
    }

    public void updateView() {
        var layoutXY = autoscale.unscaledToParent(new Point2D(data.getCenterX(), data.getCenterY()));
        var layoutRadius = data.getRadius() * autoscale.getScaleX();
        view.setRadius(layoutRadius);
        view.setCenterX(layoutXY.getX());
        view.setCenterY(layoutXY.getY());
    }

    @Override
    public CircleData getData() {
        return data;
    }

    @Override
    public Circle getView() {
        return view;
    }

    @Override
    public boolean isEditing() {
        return editing.get();
    }

    @Override
    public BooleanProperty editingProperty() {
        return editing;
    }

    @Override
    public void setEditing(boolean editing) {
        this.editing.set(editing);
    }

    @Override
    public Autoscale<?> getAutoscale() {
        return autoscale;
    }

    @Override
    public MutablePoint getLabelLocationHint() {
        return labelLocationHint;
    }

    public static Optional<CircleView> fromImageCoords(Double centerX, Double centerY, Double radius, Autoscale<?> autoscale) {
        return CircleData.clip(centerX, centerY, radius, autoscale)
                .map(data -> new CircleView(data, autoscale));
    }

    public static Optional<CircleView> fromSceneCoords(Double centerX, Double centerY, Double radius, Autoscale<?> autoscale) {
        var scenePoint = new Point2D(centerX, centerY);
        var imagePoint = autoscale.sceneToUnscaled(scenePoint);
        var imageRadius = radius / autoscale.getScaleX();
        return fromImageCoords(imagePoint.getX(), imagePoint.getY(), imageRadius, autoscale);
    }

    public static Optional<CircleView> fromParentCoords(Double centerX, Double centerY, Double radius, Autoscale<?> autoscale) {
        var parentXY = new Point2D(centerX, centerY);
        var imageXY = autoscale.parentToUnscaled(parentXY);
        var imageRadius = radius / autoscale.getScaleX();
        return fromImageCoords(imageXY.getX(), imageXY.getY(), imageRadius, autoscale);
    }

}
