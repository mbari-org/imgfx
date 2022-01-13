package org.mbari.imgfx.roi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.mbari.imgfx.util.JFXUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PolygonData implements Data {

//    private final List<Point2D> points;
    private final ObservableList<Point2D> points = FXCollections.observableArrayList();

    public PolygonData() {

    }

    public PolygonData(Collection<Point2D> points) {
        this.points.setAll(points);
    }

    public ObservableList<Point2D> getPoints() {
        return points;
    }

    public static Optional<PolygonData> clip(List<Point2D> points, Image image) {
        var r = new Rectangle(image.getWidth(), image.getHeight());
        var a = JFXUtil.pointsToArray(points);
        var p = new Polygon();
        p.getPoints().setAll(a);
        p.setClip(r);
        var clippedPoints = JFXUtil.listToPoints(p.getPoints());
        var data = new PolygonData(clippedPoints);
        return Optional.of(data);
    }
}
