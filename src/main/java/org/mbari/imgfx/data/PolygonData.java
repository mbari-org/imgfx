package org.mbari.imgfx.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PolygonData {

    private final ObservableList<Point2D> vertices = FXCollections.observableArrayList();

    public PolygonData() {

    }

    public PolygonData(Collection<Point2D> points) {
        vertices.addAll(points);
    }

    public PolygonData(double[] array) {
        this(arrayToPoints(array));
    }


    public ObservableList<Point2D> getVertices() {
        return vertices;
    }

    public static Double[] pointsToArray(List<Point2D> points) {
        var array = new Double[points.size() * 2];
        for (int i = 0; i < points.size(); i++) {
            var p = points.get(i);
            array[i] = p.getX();
            array[i + 1] = p.getY();
        }
        return array;
    }

    public static List<Point2D> arrayToPoints(double[] array) {
        List<Point2D> points = new ArrayList<>();
        for (var i = 0; i < array.length; i = i + 2) {
            var p = new Point2D(array[i], array[i + 1]);
        }
        return points;
    }
}
