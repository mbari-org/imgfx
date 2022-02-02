package org.mbari.imgfx.roi;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.BoundingBox;
import javafx.scene.shape.Line;
import org.mbari.imgfx.Autoscale;
import org.mbari.imgfx.etc.javafx.CohenSutherland;

import java.util.Optional;

public class LineData implements Data {

    private final DoubleProperty endX = new SimpleDoubleProperty();
    private final DoubleProperty endY = new SimpleDoubleProperty();
    private final DoubleProperty startX = new SimpleDoubleProperty();
    private final DoubleProperty startY = new SimpleDoubleProperty();

    public LineData() {

    }

    public LineData(double startX, double startY, double endX, double endY) {
        this.startX.set(startX);
        this.startY.set(startY);
        this.endX.set(endX);
        this.endY.set(endY);
    }


    public double getEndX() {
        return endX.get();
    }

    public DoubleProperty endXProperty() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX.set(endX);
    }

    public double getEndY() {
        return endY.get();
    }

    public DoubleProperty endYProperty() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY.set(endY);
    }

    public double getStartX() {
        return startX.get();
    }

    public DoubleProperty startXProperty() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX.set(startX);
    }

    public double getStartY() {
        return startY.get();
    }

    public DoubleProperty startYProperty() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY.set(startY);
    }

    public static Optional<LineData> clip(double startX, double startY, double endX, double endY, Autoscale<?> autoscale) {
        var bounds = new BoundingBox(0, 0, autoscale.getUnscaledWidth(), autoscale.getUnscaledHeight());
        var line = new Line(startX, startY, endX, endY);
        return CohenSutherland.clip(line, bounds)
                .map(clippedLine -> new LineData(clippedLine.getStartX(),
                        clippedLine.getStartY(),
                        clippedLine.getEndX(),
                        clippedLine.getEndY()));
    }

    @Override
    public String toString() {
        return "LineData[endX=" + endX.get() + ", endY=" + endY.get() 
                + ", startX=" + startX.get() + ", startY=" + startY.get() + "]";
    }

    
}
