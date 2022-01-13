package org.mbari.imgfx.old.glass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mbari.imgfx.ImageViewDecorator;
import org.mbari.imgfx.controls.BoundingBox;
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

public class GlassBoundingBox1 implements GlassItem {

  private UUID uuid = UUID.randomUUID();

  // In image coordinates

  private final List<Node> nodes;

  private final ImageViewDecorator imageViewExt;

  // In parent coordinates
  private final BoundingBox boundingBox;
  private final MutableGlassRectangle glassRectangle;

  private EventHandler<MouseEvent> enteredEvent = (event) -> handleMouseEnter(event);

  private EventHandler<MouseEvent> exitedEvent = (event) -> handleMouseExit(event);;


  public GlassBoundingBox1(ImageViewDecorator imageViewExt) {
    this(0, 0, 10, 10, imageViewExt);
  }

  public GlassBoundingBox1(double x, double y, double width, double height, ImageViewDecorator imageViewExt) {
    this.imageViewExt = imageViewExt;
    boundingBox = new BoundingBox(x, y, width, height);
    glassRectangle =  new MutableGlassRectangle(x, y, width, height, imageViewExt);
    nodes = new ArrayList<>();
    nodes.addAll(boundingBox.getNodes());
    nodes.addAll(glassRectangle.getNodes());
    init();
    
    // r.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> r.toFront());
  }

  private void init() {
    var r = glassRectangle.getRectangle();
    r.addEventHandler(MouseEvent.MOUSE_ENTERED, enteredEvent);
    r.addEventHandler(MouseEvent.MOUSE_EXITED, exitedEvent);
    var bb = boundingBox.getBoundingBoxRectangle();
    r.xProperty().bindBidirectional(bb.xProperty());
    r.yProperty().bindBidirectional(bb.yProperty());
    r.widthProperty().bindBidirectional(bb.widthProperty());
    r.heightProperty().bindBidirectional(bb.heightProperty());
    boundingBox.getNodes().forEach(node -> {
      node.setVisible(false);
      node.setDisable(true);
    });
  }

  private void handleMouseEnter(MouseEvent event) {
    var r = glassRectangle.getRectangle();
    r.setVisible(false);
    r.setDisable(true);
    
    boundingBox.getNodes().forEach(n -> {
      n.setVisible(true);
      n.setDisable(false);
    });
    boundingBox.toFront();
    
  }

  private void handleMouseExit(MouseEvent event) {
    boundingBox.getNodes().forEach(n -> {
      n.setVisible(false);
      // n.setDisable(true);
    });

    var r = glassRectangle.getRectangle();
    r.setVisible(true);
    // r.setDisable(false);

  }


  @Override
  public void doLayout(ImageViewDecorator ext) {
    // var r = boundingBox.getBoundingBoxRectangle();
    glassRectangle.doLayout(ext);
  }

  @Override
  public List<Node> getNodes() {

    return glassRectangle.getNodes();
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

  
  public static Optional<GlassBoundingBox1> clip(double x, double y, double width, double height, ImageViewDecorator imageViewExt) {
    var image = imageViewExt.getImageView().getImage();
    var w = image.getWidth();
    var h = image.getHeight();
    
    if (x >= w || y >= h) {
      return Optional.empty();
    }

    if (x < 0) {
      width = width + x;
    }

    if (y < 0) {
      height = height + y;
    }
    
    var xx = Math.min(Math.max(0D, x), w - 1);
    var yy = Math.min(Math.max(0D, y), h - 1);


    var ww = Math.min(Math.max(1, width), w - xx);
    var hh = Math.min(Math.max(1, height), h - yy);

    var msg = String.format("Before: [%.1f %.1f %.1f %.1f], After: [%.1f %.1f %.1f %.1f]", x, y, width, height, xx, yy, ww, hh);
    System.out.println(msg);
    var gr = new GlassBoundingBox1(xx, yy, ww, hh, imageViewExt);
    return Optional.of(gr);

  }


  
}
