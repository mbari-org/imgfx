package org.mbari.imgfx.old.sandbox;

import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

public class PolygonData {

  // Vertices in image (pixel) coordinates
  final ObservableList<Point2D> vertices = FXCollections.observableArrayList();


  public ObservableList<Point2D> getVertices() {
    return vertices;
  }

  public double minX(double[] points) {
    double min = points[0];
    for (var i = 2; i < points.length; i = i + 2) {
        min = Math.min(min, points[i]);
    }
    return min;
  }

  public double minY(double[] points) {
    double min = points[1];
    for (var i = 3; i < points.length; i = i + 2) {
        min = Math.min(min, points[i]);
    }
    return min;
  }

  public void layoutPolygon(Polygon polygon, ImageViewDecorator decorator) {
    var layoutPoints = vertices.stream()
      .map(decorator::imageToParent)
      .collect(Collectors.toList());
    var array = pointsToArray(layoutPoints);
    polygon.getPoints().addAll(array);
  }

  private static Double[] pointsToArray(List<Point2D> points) {
    var array = new Double[points.size() * 2];
    for (int i = 0; i < points.size(); i++) {
      var p = points.get(i);
      array[i] = p.getX();
      array[i + 1] = p.getY();
    }
    return array;
  }




  
}
