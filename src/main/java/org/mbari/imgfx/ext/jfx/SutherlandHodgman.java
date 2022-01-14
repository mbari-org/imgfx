package org.mbari.imgfx.ext.jfx;

import javafx.geometry.Bounds;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Clips a polygon to be within a rectangle
 */
public class SutherlandHodgman {

    public static Optional<Polygon> clip(Polygon polygon, Bounds bounds) {
        var clipper = List.of(
                new double[]{bounds.getMinX(), bounds.getMinY()},
                new double[]{bounds.getMaxX(), bounds.getMinY()},
                new double[]{bounds.getMaxX(), bounds.getMaxY()},
                new double[]{bounds.getMinX(), bounds.getMaxY()}
                );

        var subject = JFXUtil.listToPoints(polygon.getPoints())
                .stream()
                .map(p -> new double[]{p.getX(), p.getY()})
                .collect(Collectors.toList());

        var result  = new ArrayList<>(subject);
        var len = clipper.size();

        for (int i = 0; i < len; i++) {
            int len2 = result.size();
            List<double[]> input = result;
            result = new ArrayList<>(len2);

            double[] A = clipper.get((i + len - 1) % len);
            double[] B = clipper.get(i);

            for (int j = 0; j < len2; j++) {

                double[] P = input.get((j + len2 - 1) % len2);
                double[] Q = input.get(j);

                if (isInside(A, B, Q)) {
                    if (!isInside(A, B, P))
                        result.add(intersection(A, B, P, Q));
                    result.add(Q);
                } else if (isInside(A, B, P))
                    result.add(intersection(A, B, P, Q));
            }
        }

        if (result.size() < 3) {
            return Optional.empty();
        }
        else {
            var vertices = flatten(result);
            var clippedPolygon = new Polygon(vertices);
            return Optional.of(clippedPolygon);
        }

    }

    private static boolean isInside(double[] a, double[] b, double[] c) {
        return (a[0] - c[0]) * (b[1] - c[1]) > (a[1] - c[1]) * (b[0] - c[0]);
    }

    private static double[] intersection(double[] a, double[] b, double[] p, double[] q) {
        double A1 = b[1] - a[1];
        double B1 = a[0] - b[0];
        double C1 = A1 * a[0] + B1 * a[1];

        double A2 = q[1] - p[1];
        double B2 = p[0] - q[0];
        double C2 = A2 * p[0] + B2 * p[1];

        double det = A1 * B2 - A2 * B1;
        double x = (B2 * C1 - B1 * C2) / det;
        double y = (A1 * C2 - A2 * C1) / det;

        return new double[]{x, y};
    }

    private static double[] flatten(List<double[]> matrix) {
        var out = new double[matrix.size() * 2];
        for (int i = 0; i < matrix.size(); i++) {
            var j = i * 2;
            var xs = matrix.get(i);
            out[j] = xs[0];
            out[j + 1] = xs[1];
        }
        return out;
    }
}
