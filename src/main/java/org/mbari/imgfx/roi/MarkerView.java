package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import org.mbari.imgfx.ImageViewDecorator;
import org.mbari.imgfx.ext.jfx.JFXUtil;
import org.mbari.imgfx.ext.jfx.MutablePoint;

import java.util.Optional;

public class MarkerView implements DataView<CircleData, Polyline> {

    private final CircleData data;
    private final Polyline view;
    private final ImageViewDecorator decorator;
    private final BooleanProperty editing = new SimpleBooleanProperty();
    private final MutablePoint labelLocationHint = new MutablePoint();

    public MarkerView(CircleData data, ImageViewDecorator decorator) {
        this.data = data;
        this.decorator = decorator;
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
        var imageXY = decorator.parentToImage(center);

        var opt = CircleData.clip(imageXY.getX(),
                imageXY.getY(),
                imageRadius,
                decorator.getImageView().getImage());
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
        var layoutXY = decorator.imageToParent(new Point2D(data.getCenterX(), data.getCenterY()));
        var layoutRadius = data.getRadius() * decorator.getScaleX();
        updatePolyline(layoutXY.getX(), layoutXY.getY(), layoutRadius);
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
    public ImageViewDecorator getImageViewDecorator() {
        return decorator;
    }

    @Override
    public MutablePoint getLabelLocationHint() {
        return labelLocationHint;
    }

    public static Optional<MarkerView> fromImageCoords(Double centerX, Double centerY, Double radius, ImageViewDecorator decorator) {
        return CircleData.clip(centerX, centerY, radius, decorator.getImageView().getImage())
                .map(data -> new MarkerView(data, decorator));
    }

    public static Optional<MarkerView> fromSceneCoords(Double centerX, Double centerY, Double radius, ImageViewDecorator decorator) {
        var sceneXY = new Point2D(centerX, centerY);
        var imagePoint = decorator.sceneToImage(sceneXY);
        var imageRadius = radius / decorator.getScaleX();
        return fromImageCoords(imagePoint.getX(), imagePoint.getY(), imageRadius, decorator);
    }

    public static Optional<MarkerView> fromParentCoords(Double centerX, Double centerY, Double radius, ImageViewDecorator decorator) {
        var parentXY = new Point2D(centerX, centerY);
        var imageXY = decorator.parentToImage(parentXY);
        var imageRadius = radius / decorator.getScaleX();
        return fromImageCoords(imageXY.getX(), imageXY.getY(), imageRadius, decorator);
    }
}
