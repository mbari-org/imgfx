package org.mbari.imgfx.controls;

import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class CrossHairs {

  private final Line verticalLine;
  private final Line horizontalLine;
  private final List<Node> nodes;

  private double sceneX;
  private double sceneY;


  private final EventHandler<MouseEvent> movedEvent = (event) -> {
    sceneX = event.getSceneX();
    sceneY = event.getSceneY();
    doLayout();
  };

  
  public CrossHairs() {
    verticalLine = new Line();
    horizontalLine = new Line();
    nodes = List.of(verticalLine, horizontalLine);
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
    setColor(Color.valueOf("#FF666680"));
  }


  private void doLayout() {
    var parent = verticalLine.getParent();
    if (parent == null) {
      // TODO hide lines
    }
    else {
      var bounds = parent.getLayoutBounds();
      
      verticalLine.setStartX(sceneX);
      verticalLine.setEndX(sceneX);
      verticalLine.setStartY(bounds.getMinY());
      verticalLine.setEndY(bounds.getMaxY());

      horizontalLine.setStartX(bounds.getMinX());
      horizontalLine.setEndX(bounds.getMaxX());
      horizontalLine.setStartY(sceneY);
      horizontalLine.setEndY(sceneY);

    }
  }


  public List<Node> getNodes() {
    return nodes;
  }


  public void setColor(Color color) {
    verticalLine.setStroke(color);
    verticalLine.setFill(color);
    horizontalLine.setStroke(color);
    horizontalLine.setFill(color);
  }


  public void toFront() {
    verticalLine.toFront();
    horizontalLine.toFront();
  }

  
  
  
}
