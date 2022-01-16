package org.mbari.imgfx.etc.jfx;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

import java.util.Optional;

/**
 * Implementation of Cohen Sutherland line clipping algorithm
 */
public class CohenSutherland {

    private static final int INSIDE = 0;
    private static final int LEFT   = 1;
    private static final int RIGHT  = 2;
    private static final int BOTTOM = 4;
    private static final int TOP    = 8;

    private final static float MINIMUM_DELTA = 0.01f;

    /**
     * Clip a line so that it falls with in the given bounds
     * @param line A Line object
     * @param bounds The bounds to clip the line to be within
     * @return A new Line that falls with in the provided bounds
     */
    public static Optional<Line> clip(Line line, Bounds bounds) {
        var point1 = new Point(line.getStartX(), line.getStartY(), bounds);
        var point2 = new Point(line.getEndX(), line.getEndY(), bounds);
        var outsidePoint = new Point(0d, 0d, bounds);

        boolean lineIsVertical = (point1.x == point2.y);
        double lineSlope = lineIsVertical ? 0d : (point2.y-point1.y)/(point2.x-point1.x);
        while (!bounds.contains(point1.asPoint2D()) || !bounds.contains(point2.asPoint2D()))

        while (point1.region != INSIDE || point2.region != INSIDE) {
            if ((point1.region & point2.region) != 0) return Optional.empty();

            outsidePoint.region = (point1.region == INSIDE) ? point2.region : point1.region;

            if ((outsidePoint.region & LEFT) != 0) {
                outsidePoint.x = bounds.getMinX();
                outsidePoint.y = delta(outsidePoint.x, point1.x)*lineSlope + point1.y;
            }
            else if ((outsidePoint.region & RIGHT) != 0) {
                outsidePoint.x = bounds.getMaxX();
                outsidePoint.y = delta(outsidePoint.x, point1.x)*lineSlope + point1.y;
            }
            else if ((outsidePoint.region & BOTTOM) != 0) {
                outsidePoint.y = bounds.getMinY();
                outsidePoint.x = lineIsVertical
                        ? point1.x
                        : delta(outsidePoint.y, point1.y)/lineSlope + point1.x;
            }
            else if ((outsidePoint.region & TOP) != 0) {
                outsidePoint.y = bounds.getMaxY();
                outsidePoint.x = lineIsVertical
                        ? point1.x
                        : delta(outsidePoint.y, point1.y)/lineSlope + point1.x;
            }

            if (outsidePoint.isInTheSameRegionAs(point1)) {
                point1.setPositionAndRegion(outsidePoint.x, outsidePoint.y, bounds);
            }
            else {
                point2.setPositionAndRegion(outsidePoint.x, outsidePoint.y, bounds);
            }
        }
        return Optional.of(new Line(point1.x, point1.y, point2.x, point2.y));
    }

    private static double delta(double value1, double value2) {
        return (Math.abs(value1 - value2) < MINIMUM_DELTA) ? 0 : (value1 - value2);
    }

    static class Point {
        double x, y;
        int region;

        Point(double x, double y, Bounds bounds) {
            setPositionAndRegion(x, y, bounds);
        }

        void setPositionAndRegion(double x, double y, Bounds bounds) {
            this.x = x; this.y = y;
            region = (x < bounds.getMinX()) ? LEFT : (x > bounds.getMaxX()) ? RIGHT : INSIDE;
            if (y < bounds.getMinY())
                region |= BOTTOM;
            else if (y > bounds.getMaxY())
                region |= TOP;
        }

        boolean isInTheSameRegionAs(Point otherPoint) {
            return this.region == otherPoint.region;
        }

        Point2D asPoint2D() {
            return new Point2D(x, y);
        }
    }


}
