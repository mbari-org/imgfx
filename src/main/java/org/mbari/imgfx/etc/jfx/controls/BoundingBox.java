package org.mbari.imgfx.etc.jfx.controls;


import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class BoundingBox {

    private int offset = 5;
    private int square = offset * 2 + 1;

    private final Rectangle boundingBoxRectangle;
    private List<Rectangle> controlPoints;
    private final Rectangle upperLeft;
    private final Rectangle upperRight;
    private final Rectangle lowerLeft;
    private final Rectangle lowerRight;


    // Drag variables
    private Rectangle dragSource;
    private double origMouseX;
    private double origMouseY;
    private double origX;
    private double origY;
    private EventHandler<MouseEvent> mousePressedHandler = evt -> {
        dragSource = (Rectangle) evt.getSource();
        origMouseX = evt.getSceneX();
        origMouseY = evt.getSceneY();
        origX = ((Rectangle) evt.getSource()).getX();
        origY = ((Rectangle) evt.getSource()).getY();
    };
    private EventHandler<MouseEvent> mouseDraggedHandler = evt -> {
        if (dragSource == null || dragSource != evt.getSource()) {
            mousePressedHandler.handle(evt);
        }
        double dx = evt.getSceneX() - origMouseX;
        double dy = evt.getSceneY() - origMouseY;
        double newX = origX + dx;
        double newY = origY + dy;
        ((Rectangle) evt.getSource()).setX(newX);
        ((Rectangle) evt.getSource()).setY(newY);
    };


    public BoundingBox(double x, double y, double width, double height) {
        boundingBoxRectangle = new Rectangle(x, y, width, height);
        boundingBoxRectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        boundingBoxRectangle.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        boundingBoxRectangle.getStyleClass().add("mbari-bounding-box");
        boundingBoxRectangle.setStrokeWidth(2);
        upperLeft = buildUpperLeftControlPoint(boundingBoxRectangle);
        upperRight = buildUpperRightControlPoint(boundingBoxRectangle);
        lowerLeft = buildLowerLeftControlPoint(boundingBoxRectangle);
        lowerRight = buildLowerRightControlPoint(boundingBoxRectangle);
        controlPoints = List.of(upperLeft, upperRight, lowerLeft, lowerRight);
    }

    private void init() {

    }

    public void setColor(Color color) {
        Color fillColor = Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.1);
        getNodes().forEach(r -> {
            var rr = (Rectangle) r;
            rr.setStroke(color);
            rr.setFill(fillColor);
        });
    }

    public List<Node> getNodes() {
        List<Node> shapes = new ArrayList<>();
        shapes.add(getBoundingBoxRectangle());
        shapes.addAll(getControlPoints());
        return shapes;
    }

    public Rectangle getBoundingBoxRectangle() {
        return boundingBoxRectangle;
    }

    public List<Rectangle> getControlPoints() {
        return controlPoints;
    }

    private Rectangle buildUpperLeftControlPoint(Rectangle b) {
        // x, y
        Rectangle r = new Rectangle(b.getX() - offset, b.getY() - offset, square, square);
        b.xProperty().addListener((obs, oldv, newv) -> r.setX(newv.doubleValue() - offset));
        b.yProperty().addListener((obs, oldv, newv) -> r.setY(newv.doubleValue() - offset));
        r.getStyleClass().add("mbari-bounding-box-control");
        r.setStrokeWidth(1);
        r.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        r.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        r.xProperty().addListener((obs, oldX, newX) -> {
            if (r == dragSource) {
                setLeftX(newX.doubleValue());
            }
        });
        r.yProperty().addListener((obs, oldY, newY) -> {
            if (r == dragSource) {
                setUpperY(newY.doubleValue());
            }
        });

        return r; // -- WORKING
    }

    private Rectangle buildUpperRightControlPoint(Rectangle b) {
        // x, y , width
        Rectangle r =
                new Rectangle(b.getX() + b.getWidth() - offset, b.getY() - offset, square, square);
        r.getStyleClass().add("mbari-bounding-box-control");
        r.setStrokeWidth(1);
        b.xProperty().addListener(
                (obs, oldv, newv) -> r.setX(newv.doubleValue() + b.getWidth() - offset));
        b.yProperty().addListener((obs, oldv, newv) -> r.setY(newv.doubleValue() - offset));
        b.widthProperty()
                .addListener((obs, oldv, newv) -> r.setX(newv.doubleValue() + b.getX() - offset));
        r.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        r.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        r.xProperty().addListener((obs, oldX, newX) -> {
            if (r == dragSource) {
                setRightX(newX.doubleValue());
            }
        });
        r.yProperty().addListener((obs, oldY, newY) -> {
            if (r == dragSource) {
                setUpperY(newY.doubleValue());
            }
        });
        return r; // -- WORKING
    }

    private Rectangle buildLowerLeftControlPoint(Rectangle b) {
        // x, y, height
        Rectangle r =
                new Rectangle(b.getX() - offset, b.getY() + b.getHeight() - offset, square, square);
        r.getStyleClass().add("mbari-bounding-box-control");
        r.setStrokeWidth(1);
        b.xProperty().addListener((obs, oldv, newv) -> r.setX(newv.doubleValue() - offset));
        b.yProperty().addListener(
                (obs, oldv, newv) -> r.setY(newv.doubleValue() + b.getHeight() - offset));
        b.heightProperty()
                .addListener((obs, oldv, newv) -> r.setY(b.getY() + newv.doubleValue() - offset));
        r.addEventHandler(MouseEvent.MOUSE_ENTERED, evt -> r.toFront());
        r.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        r.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        r.xProperty().addListener((obs, oldX, newX) -> {
            if (r == dragSource) {
                setLeftX(newX.doubleValue());
            }
        });
        r.yProperty().addListener((obs, oldY, newY) -> {
            if (r == dragSource) {
                setLowerY(newY.doubleValue());
            }
        });
        return r; // -- WORKING
    }

    private Rectangle buildLowerRightControlPoint(Rectangle b) {
        // x, y, width, height
        Rectangle r = new Rectangle(b.getX() + b.getWidth() - offset,
                b.getY() + b.getHeight() - offset, square, square);
        r.getStyleClass().add("mbari-bounding-box-control");
        r.setStrokeWidth(1);
        b.xProperty().addListener(
                (obs, oldv, newv) -> r.setX(newv.doubleValue() + b.getWidth() - offset));
        b.yProperty().addListener(
                (obs, oldv, newv) -> r.setY(newv.doubleValue() + b.getHeight() - offset));
        b.widthProperty()
                .addListener((obs, oldv, newv) -> r.setX(newv.doubleValue() + b.getX() - offset));
        b.heightProperty()
                .addListener((obs, oldv, newv) -> r.setY(b.getY() + newv.doubleValue() - offset));
        r.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        r.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        r.xProperty().addListener((obs, oldX, newX) -> {
            if (r == dragSource) {
                setRightX(newX.doubleValue());
            }
        });
        r.yProperty().addListener((obs, oldY, newY) -> {
            if (r == dragSource) {
                setLowerY(newY.doubleValue());
            }
        });
        return r;

    }

    private void setUpperY(double y) {
        double centerY = y + offset;
        double boxHeight =
                boundingBoxRectangle.getHeight() + (boundingBoxRectangle.getY() - centerY);
        boundingBoxRectangle.setY(centerY);
        boundingBoxRectangle.setHeight(boxHeight);
    }

    private void setLowerY(double y) {
        double centerY = y + offset;
        double boxHeight = centerY - boundingBoxRectangle.getY();
        boundingBoxRectangle.setHeight(boxHeight);
    }

    private void setLeftX(double x) {
        double centerX = x + offset;
        double boxWidth = boundingBoxRectangle.getWidth() + (boundingBoxRectangle.getX() - centerX);
        boundingBoxRectangle.setX(centerX);
        boundingBoxRectangle.setWidth(boxWidth);
    }

    private void setRightX(double x) {
        double centerX = x + offset;
        double boxWidth = centerX - boundingBoxRectangle.getX();
        boundingBoxRectangle.setWidth(boxWidth);
    }

    public void toFront() {
        boundingBoxRectangle.toFront();
        for (Rectangle r : controlPoints) {
            r.toFront();
        }
    }




}
