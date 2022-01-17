package org.mbari.imgfx.etc.jfx.controls;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class CrossHairs {

  private final Line verticalLine;
  private final Line horizontalLine;
  private final List<Node> nodes;
  private final ObjectProperty<Color> color = new SimpleObjectProperty<>();

  private double layoutX;
  private double layoutY;


  private final EventHandler<MouseEvent> movedEvent = (event) -> {
    layoutX = event.getX();
    layoutY = event.getY();
    doLayout();
  };

  
  public CrossHairs() {
    verticalLine = new Line();
    horizontalLine = new Line();
    nodes = List.of(verticalLine, horizontalLine);
    init();
  }

  private void init() {
    verticalLine.parentProperty().addListener((obs, oldv, newv) -> {
      if (oldv != null) {
        oldv.removeEventHandler(MouseEvent.MOUSE_MOVED, movedEvent);
      }
      if (newv != null) {
        newv.addEventHandler(MouseEvent.MOUSE_MOVED, movedEvent);
      }
    });
    verticalLine.getStrokeDashArray().addAll(5.0, 5.0);
    horizontalLine.getStrokeDashArray().addAll(5.0, 5.0);
    colorProperty().addListener((obs, oldv, newv) -> {
        verticalLine.setStroke(newv);
        verticalLine.setFill(newv);
        horizontalLine.setStroke(newv);
        horizontalLine.setFill(newv);
    });
    setColor(Color.valueOf("#FF666680"));
  }

  private void doLayout() {
    var parent = verticalLine.getParent();
    if (parent == null) {
      // TODO hide lines
    }
    else {
      var bounds = parent.getLayoutBounds();
      
      verticalLine.setStartX(layoutX);
      verticalLine.setEndX(layoutX);
      verticalLine.setStartY(bounds.getMinY());
      verticalLine.setEndY(bounds.getMaxY());

      horizontalLine.setStartX(bounds.getMinX());
      horizontalLine.setEndX(bounds.getMaxX());
      horizontalLine.setStartY(layoutY);
      horizontalLine.setEndY(layoutY);

    }
  }


  public List<Node> getNodes() {
    return nodes;
  }


  public void toFront() {
    verticalLine.toFront();
    horizontalLine.toFront();
  }

  public Color getColor() {
    return color.get();
  }

  public ObjectProperty<Color> colorProperty() {
    return color;
  }

  public void setColor(Color color) {
    this.color.set(color);
  }
}
