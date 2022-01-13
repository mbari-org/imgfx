package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import org.mbari.imgfx.ImageViewDecorator;

public class MarkerView implements DataView<CircleData, Polyline> {

    private final CircleData data;
    private final Polyline view;
    private final ImageViewDecorator decorator;
    private final BooleanProperty editing = new SimpleBooleanProperty();

    public MarkerView(CircleData data, ImageViewDecorator decorator) {
        this.data = data;
        this.decorator = decorator;
        this.view = new Polyline();
        init();
    }

    private void init() {
        updateView();
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
}
