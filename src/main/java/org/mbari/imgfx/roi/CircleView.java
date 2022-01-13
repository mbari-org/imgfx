package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import org.mbari.imgfx.ImageViewDecorator;

public class CircleView implements DataView<CircleData, Circle> {

    private final CircleData data;
    private final Circle view;
    private final ImageViewDecorator decorator;
    private final BooleanProperty editing = new SimpleBooleanProperty();

    public CircleView(CircleData data, ImageViewDecorator decorator) {
        this.data = data;
        this.decorator = decorator;
        this.view = new Circle();
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

        ChangeListener<? super Number> viewChangeListener = (obs, oldv, newv) -> {
            if (editing.get()) {
                updateData();
            }
        };
        view.centerXProperty().addListener(viewChangeListener);
        view.centerXProperty().addListener(viewChangeListener);
        view.radiusProperty().addListener(viewChangeListener);

    }

    public void updateData() {
        var imageXY = decorator.parentToImage(new Point2D(view.getCenterX(), view.getCenterY()));
        var imageRadius = view.getRadius() / decorator.getScaleX();
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
    public ImageViewDecorator getImageViewDecorator() {
        return decorator;
    }

}