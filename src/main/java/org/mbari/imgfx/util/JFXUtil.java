package org.mbari.imgfx.util;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class JFXUtil {

    public static Double[] pointsToArray(List<Point2D> points) {
        var array = new Double[points.size() * 2];
        for (int i = 0; i < points.size(); i++) {
            var p = points.get(i);
            array[i] = p.getX();
            array[i + 1] = p.getY();
        }
        return array;
    }

    public static List<Point2D> arrayToPoints(Double[] array) {
        List<Point2D> points = new ArrayList<>();
        for (var i = 0; i < array.length; i = i + 2) {
            var p = new Point2D(array[i], array[i + 1]);
        }
        return points;
    }

    public static List<Point2D> listToPoints(List<Double> list) {
        var array = list.toArray(Double[]::new);
        return arrayToPoints(array);
    }
}
