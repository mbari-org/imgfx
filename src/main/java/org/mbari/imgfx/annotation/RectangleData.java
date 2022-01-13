package org.mbari.imgfx.annotation;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;

import java.util.Optional;

/**
 * Rectangle in image pixel coordinates
 */
public class RectangleData implements Data {

    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final DoubleProperty width = new SimpleDoubleProperty();
    private final DoubleProperty height = new SimpleDoubleProperty();

    public RectangleData() {

    }

    public RectangleData(double x, double y, double width, double height) {
        this.x.set(x);
        this.y.set(y);
        this.width.set(width);
        this.height.set(height);
    }

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public double getWidth() {
        return width.get();
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public void setWidth(double width) {
        this.width.set(width);
    }

    public double getHeight() {
        return height.get();
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    /**
     * Clip image coordinates to be within image bounds
     * @param x in pixels
     * @param y in pixels
     * @param width in pixels
     * @param height in pixels
     * @param image THe image
     * @return A Optional of a rectangle that falls within the bounds of the image. Empty otherwise.
     */
    public static Optional<RectangleData> clip(double x, double y, double width, double height, Image image) {
        var w = image.getWidth();
        var h = image.getHeight();

        if (x >= w || y >= h) {
            return Optional.empty();
        }

        if (x < 0) {
            width = width + x;
        }

        if (y < 0) {
            height = height + y;
        }

        var xx = Math.min(Math.max(0D, x), w - 1);
        var yy = Math.min(Math.max(0D, y), h - 1);


        var ww = Math.min(Math.max(1, width), w - xx);
        var hh = Math.min(Math.max(1, height), h - yy);

        var msg = String.format("Before: [%.1f %.1f %.1f %.1f], After: [%.1f %.1f %.1f %.1f]", x, y, width, height, xx, yy, ww, hh);
        System.out.println(msg);
        var data = new RectangleData(xx, yy, ww, hh);
        return Optional.of(data);
    }
}
