package org.mbari.imgfx.roi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Polygon;
import org.mbari.imgfx.etc.jfx.JFXUtil;
import org.mbari.imgfx.etc.jfx.SutherlandHodgman;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
        var bounds = new BoundingBox(0, 0, image.getWidth(), image.getHeight());
        var boxed = JFXUtil.pointsToArray(points);
        var unboxed = Stream.of(boxed).mapToDouble(Double::doubleValue).toArray();
        var p = new Polygon(unboxed);
        return SutherlandHodgman.clip(p, bounds)
                        .map(clippedPolygon -> {
                            var clippedPoints = JFXUtil.listToPoints(clippedPolygon.getPoints());
                            return new PolygonData(clippedPoints);
                        });
    }
}
