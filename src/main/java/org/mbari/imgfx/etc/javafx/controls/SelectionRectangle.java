package org.mbari.imgfx.etc.javafx.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
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
  private final ObjectProperty<EventHandler<MouseEvent>> onCompleteHandler = new SimpleObjectProperty<>();
  private Color color = (Color) rectangle.getFill();

  private final EventHandler<MouseEvent> pressedEvent = (event) -> {
    startX = event.getX();
    startY = event.getY();
    rectangle.setX(startX);
    rectangle.setY(startY);
    rectangle.setWidth(0);
    rectangle.setHeight(0);
    rectangle.setFill(color);
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
    this.onCompleteHandler.set(onCompleteHandler);
    init();
  }

  private void init() {
    rectangle.setStrokeWidth(0);
    rectangle.setFill(Color.TRANSPARENT);
    rectangle.getStrokeDashArray().addAll(5.0, 5.0);
    rectangle.parentProperty().addListener((observable, oldValue, newValue) -> {
      if (oldValue != null) {
        oldValue.removeEventHandler(MouseEvent.MOUSE_PRESSED, pressedEvent);
        oldValue.removeEventHandler(MouseEvent.MOUSE_DRAGGED, dragEvent);
        oldValue.removeEventHandler(MouseEvent.MOUSE_RELEASED, releasedEvent);
        oldValue.removeEventHandler(MouseEvent.MOUSE_RELEASED, onCompleteHandler.get());
      }

      if (newValue != null) {
        newValue.addEventHandler(MouseEvent.MOUSE_PRESSED, pressedEvent);
        newValue.addEventHandler(MouseDragEvent.MOUSE_DRAGGED, dragEvent);
        newValue.addEventHandler(MouseEvent.MOUSE_RELEASED, releasedEvent);
        newValue.addEventHandler(MouseEvent.MOUSE_RELEASED, onCompleteHandler.get());
      }
    });

    onCompleteHandler.addListener((obs, oldv, newv) -> {
      var parent = rectangle.getParent();
      if (parent != null) {
        parent.removeEventHandler(MouseEvent.MOUSE_RELEASED, oldv);
        parent.addEventHandler(MouseEvent.MOUSE_RELEASED, newv);
      }
    });
  }

  public Rectangle getRectangle() {
    return rectangle;
  }

  public EventHandler<MouseEvent> getOnCompleteHandler() {
    return onCompleteHandler.get();
  }

  public ObjectProperty<EventHandler<MouseEvent>> onCompleteHandlerProperty() {
    return onCompleteHandler;
  }

  public void setOnCompleteHandler(EventHandler<MouseEvent> onCompleteHandler) {
    this.onCompleteHandler.set(onCompleteHandler);
  }

  public void setDragStart(Point2D dragStart) {
    startX = dragStart.getX();
    startY = dragStart.getY();
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }
}
