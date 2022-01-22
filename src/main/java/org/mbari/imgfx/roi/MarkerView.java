package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import org.mbari.imgfx.Autoscale;
import org.mbari.imgfx.etc.jfx.JFXUtil;
import org.mbari.imgfx.etc.jfx.MutablePoint;

import java.util.Optional;

public class MarkerView implements DataView<CircleData, Polyline> {

    private final CircleData data;
    private final Polyline view;
    private final Autoscale<?> autoscale;
    private final BooleanProperty editing = new SimpleBooleanProperty();
    private final MutablePoint labelLocationHint = new MutablePoint();

    public MarkerView(CircleData data, Autoscale<?> autoscale) {
        this.data = data;
        this.autoscale = autoscale;
        this.view = new Polyline();
        init();
    }

    private void init() {

//        view.setFill(Color.TRANSPARENT);

        EditingDecorator.decorate(this);

        ChangeListener<? super Number> dataChangeListener = (obs, oldv, newv) -> {
            if (!editing.get()) {
                updateView();
            }
        };
        data.centerXProperty().addListener(dataChangeListener);
        data.centerXProperty().addListener(dataChangeListener);
        data.radiusProperty().addListener(dataChangeListener);

        view.getPoints().addListener((ListChangeListener<? super Double>) c -> {
            if (editing.get()) {
                updateData();
            }
        });

        // labelLocationHint is at center
        view.getPoints().addListener((ListChangeListener<? super Double>) e -> {
            // avg x
            var points = JFXUtil.listToPoints(view.getPoints());
            var avgX = points.stream()
                    .mapToDouble(Point2D::getX)
                    .average()
                    .orElse(0D);

            var avgY = points.stream()
                    .mapToDouble(Point2D::getY)
                    .average()
                    .orElse(0D);
            labelLocationHint.xProperty().set(avgX);
            labelLocationHint.yProperty().set(avgY);

        });

        updateView();
    }

    public void updateData() {
        var points = view.getPoints();

        var center = new Point2D(points.get(4), points.get(5));
        var upperRight = new Point2D(points.get(2), points.get(3));
        var dx = upperRight.getX() - center.getX();
        var dy = center.getY() - upperRight.getY();
        var imageRadius = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        var imageXY = autoscale.parentToUnscaled(center);

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

    }

    public void updateView() {
        var layoutXY = autoscale.unscaledToParent(new Point2D(data.getCenterX(), data.getCenterY()));
        var layoutRadius = data.getRadius() * autoscale.getScaleX();
        updatePolyline(layoutXY.getX(), layoutXY.getY(), layoutRadius);
    }

    @Override
    public void setColor(Color color) {
        var opaque = Color.color(color.getRed(), color.getGreen(), color.getBlue());
        getView().setStroke(opaque);
    }

    private void updatePolyline(double x, double y, double radius) {
        var r = Math.cos(45 * Math.PI / 180) * radius;
        var points = new Double[]{
                x - r, y + r,  // lower left
                x + r, y - r,  // upper right
                x, y,          // center
                x - r, y - r,  // upper left
                x + r, y + r   // lower right
        };
        view.getPoints().setAll(points);
    }

    @Override
    public CircleData getData() {
        return data;
    }

    @Override
    public Polyline getView() {
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

    public static Optional<MarkerView> fromImageCoords(Double centerX, Double centerY, Double radius, Autoscale<?> autoscale) {
        return CircleData.clip(centerX, centerY, radius, autoscale)
                .map(data -> new MarkerView(data, autoscale));
    }

    public static Optional<MarkerView> fromSceneCoords(Double centerX, Double centerY, Double radius, Autoscale<?> autoscale) {
        var sceneXY = new Point2D(centerX, centerY);
        var imagePoint = autoscale.sceneToUnscaled(sceneXY);
        var imageRadius = radius / autoscale.getScaleX();
        return fromImageCoords(imagePoint.getX(), imagePoint.getY(), imageRadius, autoscale);
    }

    public static Optional<MarkerView> fromParentCoords(Double centerX, Double centerY, Double radius, Autoscale<?> autoscale) {
        var parentXY = new Point2D(centerX, centerY);
        var imageXY = autoscale.parentToUnscaled(parentXY);
        var imageRadius = radius / autoscale.getScaleX();
        return fromImageCoords(imageXY.getX(), imageXY.getY(), imageRadius, autoscale);
    }
}
