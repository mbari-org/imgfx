package org.mbari.imgfx.annotation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import org.mbari.imgfx.ImageViewDecorator;

public class CircleView {

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

    }

    private void updateData() {
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
    }

    private void updateView() {
        var layoutXY = decorator.imageToParent(new Point2D(data.getCenterX(), data.getCenterY()));
        var layoutRadius = data.getRadius() * decorator.getScaleX();
        view.setRadius(layoutRadius);
        view.setCenterX(layoutXY.getX());
        view.setCenterY(layoutXY.getY());
    }

    public CircleData getData() {
        return data;
    }

    public Circle getView() {
        return view;
    }

    public boolean isEditing() {
        return editing.get();
    }

    public BooleanProperty editingProperty() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing.set(editing);
    }

}
