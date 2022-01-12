package org.mbari.imgfx.glass;

import java.util.List;
import java.util.UUID;
import org.mbari.imgfx.ImageViewExt;
import org.mbari.imgfx.controls.BoundingBox;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * TODO
 * - Adding it adds GlassRectangle
 * - On mouseover
 *  - remove GlassRectangle
 *  - add BoundingBox in parent coords
 * - on mouse exit
 *  - read bounding box coords
 *  - convert image coords
 *  - set x, y, width, height
 *  - create new GlassRectangle and add it.
 */

public class GlassBoundingBox implements GlassItem {

  private UUID uuid = UUID.randomUUID();

  // In image coordinates
  private double x;
  private double y;
  private double width;
  private double height;

  private final ImageViewExt imageViewExt;

  // In parent coordinates
  private final BoundingBox boundingBox;
  private final MutableGlassRectangle glassRectangle;

  private EventHandler<MouseEvent> enteredEvent = (event) -> handleMouseEnter(event);

  private EventHandler<MouseEvent> exitedEvent = (event) -> handleMouseExit(event);;



  public GlassBoundingBox(ImageViewExt imageViewExt) {
    this(0, 0, 10, 10, imageViewExt);
  }

  public GlassBoundingBox(double x, double y, double width, double height, ImageViewExt imageViewExt) {
    this.imageViewExt = imageViewExt;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    boundingBox = new BoundingBox(x, y, width, height);
    glassRectangle =  new MutableGlassRectangle(x, y, width, height, imageViewExt);
    init();
    
    
    
    // r.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> r.toFront());
  }

  private void init() {
    var r = glassRectangle.getRectangle();
    r.addEventHandler(MouseEvent.MOUSE_ENTERED, enteredEvent);
    r.addEventHandler(MouseEvent.MOUSE_EXITED, exitedEvent);
  }

  private void handleMouseEnter(MouseEvent event) {
    glassRectangle.getRectangle().setVisible(false);
    // 
  }

  private void handleMouseExit(MouseEvent event) {

  }

  private void recalculateImageCoords() {
    System.out.println("" + boundingBox.getBoundingBoxRectangle());
    var r = boundingBox.getBoundingBoxRectangle();

  }

  @Override
  public void doLayout(ImageViewExt ext) {
    var r = boundingBox.getBoundingBoxRectangle();
  }

  @Override
  public List<Node> getNodes() {
    return boundingBox.getNodes();
  }

  @Override
  public UUID getUuid() {
    return uuid;
  }

  @Override
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public void toFront() {
    boundingBox.toFront();
  }

  @Override
  public void setColor(Color color) {
    boundingBox.setColor(color);
  }

  
  


  
}
