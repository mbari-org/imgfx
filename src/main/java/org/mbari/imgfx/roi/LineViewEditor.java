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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.mbari.imgfx.ext.jfx.ShapeColors;

import java.util.Collections;
import java.util.List;

public class LineViewEditor implements ViewEditor {

    private final ObjectProperty<Color> editColor = new SimpleObjectProperty<>(DEFAULT_EDIT_COLOR);
    private final BooleanProperty disable = new SimpleBooleanProperty();
    private final BooleanProperty editing = new SimpleBooleanProperty();

    /* Some stuff to layout the control points */
    private int offset = 5;
    private int square = offset * 2 + 1;

    /** The line from the LineView */
    private final Line line;

    private final Pane parentPane;

    /*  When editing is turned on it changes the look of the component to
       indicate that it's actively editing. Hang on to the original colors
       so we can set it back to its old look when editing id done
    */
    private ShapeColors lastShapeColors;
    private final Circle startCircle = new Circle();
    private final Circle endCircle = new Circle();

    private List<Circle> controlPoints = Collections.emptyList();
    private Shape dragSource;

    private double origMouseX;
    private double origMouseY;
    private double origX;
    private double origY;

    private EventHandler<MouseEvent> mousePressedHandler = evt -> {
        dragSource = (Circle) evt.getSource();
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
        if (dragSource instanceof Rectangle) {
            ((Rectangle) evt.getSource()).setX(newX);
            ((Rectangle) evt.getSource()).setY(newY);
        }
    };

    // If a user clicks outside the editor, turn off the editor
    private EventHandler<MouseEvent> checkEditHandler = event -> {
        var clickPoint = new Point2D(event.getSceneX(), event.getSceneY());
        var doingEdits = getNodes()
                .stream()
                .anyMatch(n -> n.contains(clickPoint));
        setEditing(doingEdits);
    };


    public LineViewEditor(Line line, Pane parentPane) {
        this.line = line;
        this.parentPane = parentPane;
        init();
    }

    private void init() {
        startCircle.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> dragSource = startCircle);
        endCircle.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> dragSource = endCircle);

    }


    private void enableEditing() {
        if (!disable.get()) {


        }
    }

    private void disableEditing() {

    }

    private Rectangle buildStartControlPoint(Line line) {
        var r = new Rectangle(line.getStartX() - square / 2D,
                line.getStartY() - square / 2D, square, square);
        r.setStrokeWidth(1);
        r.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        r.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        line.startXProperty().addListener((obs, oldv, newv) ->
            r.setX(newv.doubleValue() - square / 2D));
        line.startYProperty().addListener((obs, oldv, newv) ->
                r.setY(newv.doubleValue() - square / 2D));
        r.xProperty().addListener((obs, oldv, newv) -> {
            if (r == dragSource) {
                var x = newv.doubleValue() + square / 2D;
                line.setStartX(x);
            }
        });
        r.yProperty().addListener((obs, oldv, newv) -> {
            if (r == dragSource) {
                var y = newv.doubleValue() + square / 2D;
                line.setStartY(y);
            }
        });
        return r;
    }

    private Rectangle buildEndControlPoint(Line line) {
        var r = new Rectangle(line.getEndX() - square / 2D,
                line.getEndY() - square / 2D, square, square);
        r.setStrokeWidth(1);
        r.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        r.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        line.endXProperty().addListener((obs, oldv, newv) ->
                r.setX(newv.doubleValue() - square / 2D));
        line.endYProperty().addListener((obs, oldv, newv) ->
                r.setY(newv.doubleValue() - square / 2D));
        r.xProperty().addListener((obs, oldv, newv) -> {
            if (r == dragSource) {
                var x = newv.doubleValue() + square / 2D;
                line.setEndX(x);
            }
        });
        r.yProperty().addListener((obs, oldv, newv) -> {
            if (r == dragSource) {
                var y = newv.doubleValue() + square / 2D;
                line.setEndY(y);
            }
        });
        return r;
    }

    public List<Node> getNodes() {
        return List.of(line, startCircle, endCircle);
    }

    @Override
    public Color getEditColor() {
        return editColor.get();
    }

    @Override
    public ObjectProperty<Color> editColorProperty() {
        return editColor;
    }

    public void setEditColor(Color editColor) {
        this.editColor.set(editColor);
    }

    public boolean isDisable() {
        return disable.get();
    }

    @Override
    public BooleanProperty disableProperty() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable.set(disable);
    }

    public boolean isEditing() {
        return editing.get();
    }

    @Override
    public BooleanProperty editingProperty() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing.set(editing);
    }
}
