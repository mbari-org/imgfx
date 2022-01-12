package org.mbari.imgfx.glass;

import org.mbari.imgfx.ImageViewExt;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class RectangleDragHandler {


  // Drag variables
  private final MutableGlassRectangle glassRectangle;
  private final Rectangle rectangle;
  private final ImageViewExt imageViewExt;
  private double origMouseX;
  private double origMouseY;
  private double origX;
  private double origY;
  private EventHandler<MouseEvent> mousePressedHandler = this::mousePressed;
  private EventHandler<MouseEvent> mouseDraggedHandler = this::mouseDragged;
  private EventHandler<MouseEvent> mouseReleasedHandler = this::mouseReleased;

  public RectangleDragHandler(MutableGlassRectangle glassRectangle) {
    this.glassRectangle = glassRectangle;
    this.rectangle = glassRectangle.getRectangle();
    this.imageViewExt = glassRectangle.getImageViewExt();
    init();
  }

  private void init() {
    rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
    rectangle.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
    rectangle.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler);
  }

  private void mousePressed(MouseEvent evt) {
    System.out.println(evt);
    if (evt.getSource() == rectangle) {
      origMouseX = evt.getSceneX();
      origMouseY = evt.getSceneY();
      origX = rectangle.getLayoutX();
      origY = rectangle.getLayoutY();
    }
  }

  private void mouseDragged(MouseEvent evt) {
    if (evt.getSource() == rectangle) {

      // Scene coords
      double dx = evt.getSceneX() - origMouseX;
      double dy = evt.getSceneY() - origMouseY;
      double newX = origX + dx;
      double newY = origY + dy;

      rectangle.setLayoutX(newX);
      rectangle.setLayoutY(newY); 
    }
  }

  private void mouseReleased(MouseEvent evt) {
    if (evt.getSource() == rectangle) {
      double layoutX = rectangle.getLayoutX();
      double layoutY = rectangle.getLayoutY();
      var layoutP = new Point2D(layoutX, layoutY);
      var imageP = imageViewExt.parentToImage(layoutP);
      glassRectangle.setX(imageP.getX());
      glassRectangle.setY(imageP.getY());
    }


  }

  


  
}
