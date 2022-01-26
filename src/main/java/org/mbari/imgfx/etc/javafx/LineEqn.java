package org.mbari.imgfx.etc.javafx;

import javafx.geometry.Point2D;

public record LineEqn(double slope, double intercept) {

    public static LineEqn from(Double startX, Double startY, Double endX, Double endY) {
        var dx = endX - startX;
        var dy = endY - startY;
        var slope = dy / dx;
        var intercept = startY - startX * slope;
        return new LineEqn(slope, intercept);
    }

    public double y(double x) {
        return slope * x + intercept;
    }

    public double x(double y) {
        return (y - intercept) / slope;
    }

    public Point2D intersect(LineEqn that) {
        var x0 = (that.intercept() - this.intercept()) / (this.slope - that.slope());
        var y0 = y(x0);
        return new Point2D(x0, y0);
    }

    @Override
    public String toString() {
        return String.format("y = %.2f * x + %.2f", slope, intercept);
    }
}
