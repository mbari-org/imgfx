package org.mbari.imgfx.ext.jfx;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public record ShapeColors(Paint fill, Paint stroke, Double strokeWidth, StrokeType strokeType) {
    public static ShapeColors fromShape(Shape shape) {
        return new ShapeColors(shape.getFill(), shape.getStroke(), shape.getStrokeWidth(), shape.getStrokeType());
    }

    public void applyTo(Shape shape) {
        shape.setFill(fill());
        shape.setStroke(stroke);
        shape.setStrokeWidth(strokeWidth);
        shape.setStrokeType(strokeType);
    }
}
