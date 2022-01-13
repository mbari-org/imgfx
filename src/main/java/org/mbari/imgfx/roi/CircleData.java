package org.mbari.imgfx.roi;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;

import java.util.Optional;

public class CircleData implements Data {

    private final DoubleProperty centerX = new SimpleDoubleProperty();
    private final DoubleProperty centerY = new SimpleDoubleProperty();
    private final DoubleProperty radius = new SimpleDoubleProperty();

    public CircleData() {

    }

    public CircleData(double centerX, double centerY, double radius) {
        this.centerX.set(centerX);
        this.centerY.set(centerY);
        this.radius.set(radius);
    }

    public double getCenterX() {
        return centerX.get();
    }

    public DoubleProperty centerXProperty() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX.set(centerX);
    }

    public double getCenterY() {
        return centerY.get();
    }

    public DoubleProperty centerYProperty() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY.set(centerY);
    }

    public double getRadius() {
        return radius.get();
    }

    public DoubleProperty radiusProperty() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius.set(radius);
    }

    public static Optional<CircleData> clip(double centerX, double centerY, double radius, Image image) {
        if (centerX > 0 && centerY > 0 && centerX < image.getWidth() && centerY < image.getHeight()) {
            return Optional.of(new CircleData(centerX, centerY, radius));
        }
        return Optional.empty();
    }

}
