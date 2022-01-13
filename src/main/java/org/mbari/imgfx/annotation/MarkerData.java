package org.mbari.imgfx.annotation;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MarkerData implements Data {
    private final DoubleProperty centerX = new SimpleDoubleProperty();
    private final DoubleProperty centerY = new SimpleDoubleProperty();
    private final DoubleProperty armLength = new SimpleDoubleProperty();


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

    public double getArmLength() {
        return armLength.get();
    }

    public DoubleProperty armLengthProperty() {
        return armLength;
    }

    public void setArmLength(double armLength) {
        this.armLength.set(armLength);
    }
}
