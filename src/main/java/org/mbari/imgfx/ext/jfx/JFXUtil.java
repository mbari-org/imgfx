package org.mbari.imgfx.ext.jfx;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JFXUtil {

    public static Double[] pointsToArray(List<Point2D> points) {
        var array = new Double[points.size() * 2];
        for (int i = 0; i < points.size(); i++) {
            int j = i * 2;
            var p = points.get(i);
            array[j] = p.getX();
            array[j + 1] = p.getY();
        }
        return array;
    }

    public static List<Point2D> arrayToPoints(Double[] array) {
        List<Point2D> points = new ArrayList<>();
        for (var i = 0; i < array.length - 1; i = i + 2) {
            var p = new Point2D(array[i], array[i + 1]);
            points.add(p);
        }
        return points;
    }

    public static List<Point2D> listToPoints(List<Double> list) {
        var array = list.toArray(Double[]::new);
        return arrayToPoints(array);
    }


}
