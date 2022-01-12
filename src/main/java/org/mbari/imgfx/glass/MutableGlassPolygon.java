package org.mbari.imgfx.glass;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mbari.imgfx.ImageViewExt;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

public class MutableGlassPolygon implements GlassItem {

  private final Polygon polygon;
  private final List<Node> nodes;
  private UUID uuid = UUID.randomUUID();
  private final List<Point2D> points;

  public MutableGlassPolygon(double[] points) {
    this.points = arrayToPoints(points);
    polygon = new Polygon(points);
    polygon.setStrokeWidth(4);
    polygon.setFill(Paint.valueOf("#FFA50080"));
    nodes = List.of(polygon);
  }

  @Override
  public void doLayout(ImageViewExt ext) {
    var layoutPoints = points.stream()
      .map(ext::imageToParent)
      .collect(Collectors.toList());
    var array = pointsToArray(layoutPoints);
    polygon.getPoints().setAll(array);
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

  private static List<Point2D> arrayToPoints(double[] array) {
    List<Point2D> points = new ArrayList<>();
    for (var i = 0; i < array.length; i = i + 2) {
      var p = new Point2D(array[i], array[i + 1]);
    }
    return points;
  }



  @Override
  public List<Node> getNodes() {
    return nodes;
  }

  @Override
  public UUID getUuid() {
    return uuid;
  }

  @Override
  public void setColor(Color color) {
    polygon.setFill(color);
    polygon.setStroke(color);
    
  }

  @Override
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
    
  }

  @Override
  public void toFront() {
    polygon.toFront();
  }


  
  
  
}
