package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RectangleViewEditor implements ViewEditor {

    /** The rectableView to decorate with and editor */
    private final DataView<RectangleData, Rectangle> rectangleView;

    /** The rectangle from the rectangleView. We hold a reference to make the code cleaner */
    private final Rectangle rectangle;

    /** We need this reference to add and remove editor components */
    private final Pane parentPane;

    /** When editing we can change the color of the component */
    private final ObjectProperty<Color> editColor = new SimpleObjectProperty<>(DEFAULT_EDIT_COLOR);
    private final BooleanProperty disable = new SimpleBooleanProperty();

    /*  When editing is turned on it changes the look of the component to
       indicate that it's actively editing. Hang on to the original colors
       so we can set it back to its old look when editing id done
    */
    private Paint lastColor;
    private double lastStrokeWidth;
    private Paint lastStroke;
    private StrokeType lastStrokeType;

    /* Some stuff to layout the control points */
    private int offset = 5;
    private int square = offset * 2 + 1;

    private List<Rectangle> controlPoints = Collections.emptyList();

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

    // If a user clicks outside the editor, turn off the editor
    private EventHandler<MouseEvent> checkEditHandler = event -> {
        var clickPoint = new Point2D(event.getSceneX(), event.getSceneY());
        var doingEdits = getNodes()
                .stream()
                .anyMatch(n -> n.contains(clickPoint));
        setEditing(doingEdits);
    };

    public RectangleViewEditor(DataView<RectangleData, Rectangle> rectangleView, Pane parentPane) {
        this.rectangleView = rectangleView;
        this.rectangle = rectangleView.getView();
        this.parentPane = parentPane;
        init();
    }

    private void init() {
        rectangleView.editingProperty()
                        .addListener((obs, oldv, newv) -> {
                            if (newv) {
                                enableEditing();
                            }
                            else {
                                disableEditing();
                            }
                        });
        rectangle.sceneProperty().addListener((obs, oldv, newv) -> {
            if (oldv != null) {
                oldv.removeEventHandler(MouseEvent.MOUSE_PRESSED, checkEditHandler);
            }
            if (newv != null) {
                newv.addEventHandler(MouseEvent.MOUSE_PRESSED, checkEditHandler);
            }
        });
        disableProperty().addListener((obs, oldv, newv) -> {
            if (newv) {
                setEditing(false);
            }
        });
    }

    private void enableEditing() {
        if (!disable.get()) {
            rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
            rectangle.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);

            // Change look while editing. Store pre-edit look
            lastColor = rectangle.getFill();
            lastStrokeWidth = rectangle.getStrokeWidth();
            lastStroke = rectangle.getStroke();
            lastStrokeType = rectangle.getStrokeType();

            var c = editColor.get();
            var darkerEditColor = Color.color(c.getRed(), c.getGreen(), c.getBlue(), 0.8).darker();

            rectangle.setStrokeType(StrokeType.INSIDE);
            rectangle.setStrokeWidth(2);
            rectangle.setFill(c);
            rectangle.setStroke(darkerEditColor);

            // Build control points and add them to the pane
            var upperLeft = buildUpperLeftControlPoint(rectangle);
            var upperRight = buildUpperRightControlPoint(rectangle);
            var lowerLeft = buildLowerLeftControlPoint(rectangle);
            var lowerRight = buildLowerRightControlPoint(rectangle);
            controlPoints = List.of(upperLeft, upperRight, lowerLeft, lowerRight);
            controlPoints.forEach(cp -> cp.setFill(darkerEditColor));
            parentPane.getChildren().addAll(controlPoints);
            toFront();
        }
    }

    private void disableEditing() {
        rectangle.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        rectangle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);

        // return to original, pre-edit look
        rectangle.setStroke(lastStroke);
        rectangle.setStrokeWidth(lastStrokeWidth);
        rectangle.setFill(lastColor);
        rectangle.setStrokeType(lastStrokeType);

        // remove and get rid of contol points
        parentPane.getChildren().removeAll(controlPoints);
        controlPoints = new ArrayList<>();
    }

    public void toFront() {
        rectangle.toFront();
        controlPoints.forEach(Node::toFront);
    }

    @Override
    public void setEditing(boolean editing) {
        rectangleView.setEditing(editing);
    }

    @Override
    public boolean isEditing() {
        return rectangleView.isEditing();
    }

    @Override
    public BooleanProperty editingProperty() {
        return rectangleView.editingProperty();
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
                rectangle.getHeight() + (rectangle.getY() - centerY);
        rectangle.setY(centerY);
        rectangle.setHeight(boxHeight);
    }

    private void setLowerY(double y) {
        double centerY = y + offset;
        double boxHeight = centerY - rectangle.getY();
        rectangle.setHeight(boxHeight);
    }

    private void setLeftX(double x) {
        double centerX = x + offset;
        double boxWidth = rectangle.getWidth() + (rectangle.getX() - centerX);
        rectangle.setX(centerX);
        rectangle.setWidth(boxWidth);
    }

    private void setRightX(double x) {
        double centerX = x + offset;
        double boxWidth = centerX - rectangle.getX();
        rectangle.setWidth(boxWidth);
    }

    public Color getEditColor() {
        return editColor.get();
    }

    public ObjectProperty<Color> editColorProperty() {
        return editColor;
    }

    public void setEditColor(Color editColor) {
        this.editColor.set(editColor);
    }

    public List<Rectangle> getNodes() {
        var nodes = new ArrayList<Rectangle>(controlPoints);
        nodes.add(rectangle);
        return nodes;
    }

    public List<Rectangle> getControlPoints() {
        return controlPoints;
    }

    @Override
    public void setDisable(boolean disable) {
        this.disable.set(disable);
    }

    @Override
    public boolean isDisable() {
        return disable.get();
    }

    @Override
    public BooleanProperty disableProperty() {
        return disable;
    }
}
