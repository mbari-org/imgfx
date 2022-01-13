package org.mbari.imgfx.controls;

import javafx.event.EventHandler;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * A draggable rectangle that can be used to select a region of interest.
 * 
 * When added or removed from anode it will automatically wire itself up.
 */
public class SelectionRectangle {

  Rectangle rectangle = new Rectangle();
  private double startX;
  private double startY;
  private EventHandler<MouseEvent> onCompleteHandler;

  private final EventHandler<MouseEvent> pressedEvent = (event) -> {
    startX = event.getX();
    startY = event.getY();
    rectangle.setX(startX);
    rectangle.setY(startY);
    rectangle.setWidth(0);
    rectangle.setHeight(0);
    rectangle.setFill(Paint.valueOf("#00FF0040"));
  };

  private final EventHandler<MouseEvent> dragEvent = (event) -> {
    rectangle.setX(Math.min(event.getX(), startX));
    rectangle.setWidth(Math.abs(event.getX() - startX));
    rectangle.setY(Math.min(event.getY(), startY));
    rectangle.setHeight(Math.abs(event.getY() - startY));
  };

  private final EventHandler<MouseEvent> releasedEvent = (event) -> {
    rectangle.setFill(Color.TRANSPARENT);
  };

  public SelectionRectangle() {
    this((event) -> {});
  }

  /**
   * 
   * @param onCompleteHandler When a drag is complete, this handler is called.
   */
  public SelectionRectangle(EventHandler<MouseEvent> onCompleteHandler) {
    this.onCompleteHandler = onCompleteHandler;
    rectangle.setStrokeWidth(0);
    rectangle.setFill(Color.TRANSPARENT);
    rectangle.getStrokeDashArray().addAll(5.0, 5.0);
    rectangle.parentProperty().addListener((observable, oldValue, newValue) -> {
      if (oldValue != null) {
        oldValue.removeEventHandler(MouseEvent.MOUSE_PRESSED, pressedEvent);
        oldValue.removeEventHandler(MouseEvent.MOUSE_DRAGGED, dragEvent);
        oldValue.removeEventHandler(MouseEvent.MOUSE_RELEASED, releasedEvent);
        oldValue.removeEventHandler(MouseEvent.MOUSE_RELEASED, onCompleteHandler);
      }
      
      if (newValue != null) {
        newValue.addEventHandler(MouseEvent.MOUSE_PRESSED, pressedEvent);
        newValue.addEventHandler(MouseDragEvent.MOUSE_DRAGGED, dragEvent);
        newValue.addEventHandler(MouseEvent.MOUSE_RELEASED, releasedEvent);
        newValue.addEventHandler(MouseEvent.MOUSE_RELEASED, onCompleteHandler);
      }
    });
  }

  public Rectangle getRectangle() {
    return rectangle;
  }

  public EventHandler<MouseEvent> getOnCompleteHandler() {
    return onCompleteHandler;
  }

  public void setOnCompleteHandler(EventHandler<MouseEvent> onCompleteHandler) {
    
    var parent = rectangle.parentProperty().get();
    if (parent != null) {
      parent.removeEventHandler(MouseEvent.MOUSE_RELEASED, this.onCompleteHandler);
      parent.addEventHandler(MouseEvent.MOUSE_RELEASED, onCompleteHandler);
    }
    this.onCompleteHandler = onCompleteHandler;
  }


  

  
}
