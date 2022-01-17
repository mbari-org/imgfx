package org.mbari.imgfx.tools;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.mbari.imgfx.Builder;
import org.mbari.imgfx.imageview.ImagePaneController;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.etc.jfx.JFXUtil;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.etc.rx.events.NewPolygonEvent;
import org.mbari.imgfx.roi.PolygonView;
import org.mbari.imgfx.roi.ViewEditor;

import java.util.ArrayList;
import java.util.List;

public class PolygonBuilder implements Builder {

    private final BooleanProperty disabled = new SimpleBooleanProperty(true);
    private final ImagePaneController paneController;
    private final EventBus eventBus;
    private final List<Double> selectedPoints = new ArrayList<>();
    private final Polygon polygon = new Polygon();
    private final ObjectProperty<Color> editColor = new SimpleObjectProperty<>(ViewEditor.DEFAULT_EDIT_COLOR);

    private Double currentX;
    private Double currentY;
    private boolean isBuilding = false;
    private EventHandler<MouseEvent> clickedEvent = (event) -> {
        if (!isDisabled()) {
            if (!isBuilding) {
                startPolygon(event.getX(), event.getY());
            }
            else {
                if(event.getButton() == MouseButton.SECONDARY) {
                    clearPolygon();
                }
                else {
                    addPointToPolygon(event.getX(), event.getY());
                }
            }
        }
    };

    private final EventHandler<MouseEvent> motionEvent = (event) -> {
        if (!isDisabled() && isBuilding) {
            currentX  = event.getX();
            currentY = event.getY();
            updatePolygon();
        }
    };

    private final EventHandler<KeyEvent> keyEvent = (event) -> {
        // Stop if any key is pressed
        if (!isDisabled() && isBuilding) {
            build(polygon);
            clearPolygon();
        }
    };



    public PolygonBuilder(ImagePaneController paneController, EventBus eventBus) {
        this.paneController = paneController;
        this.eventBus = eventBus;
        init();
    }

    private void init() {

        var pane = paneController.getPane();
        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickedEvent);
        pane.addEventHandler(MouseEvent.MOUSE_MOVED, motionEvent);
        pane.sceneProperty().addListener((obs, oldv, newv) -> {
            if (oldv != null) {
                oldv.removeEventHandler(KeyEvent.KEY_TYPED, keyEvent);
            }
            if (newv != null) {
                newv.addEventHandler(KeyEvent.KEY_TYPED, keyEvent);
            }
        });

        disabledProperty().addListener((obs, oldv, newv) -> {
            if (newv) {
                paneController.getPane().getChildren().remove(polygon);
            }
            else {
                paneController.getPane().getChildren().add(polygon);
            }
        });

        polygon.getPoints().addListener((ListChangeListener<? super Double>) c -> {
            if (polygon.getPoints().size() < 5) {
                var color = editColor.get();
                var solidColor = Color.color(color.getRed(), color.getGreen(), color.getBlue());
                polygon.setStroke(editColor.get());
                polygon.setStrokeWidth(3D);
            }
            else {
                polygon.setStroke(null);
                polygon.setStrokeWidth(0D);
            }
        });

    }

    private void startPolygon(Double x, Double y) {
        var points = polygon.getPoints();
        selectedPoints.clear();
        points.clear();
        isBuilding = true;
        points.addAll(x, y, x, y);
        selectedPoints.add(x);
        selectedPoints.add(y);
        currentX = x;
        currentY = y;
        polygon.setFill(editColor.get());
        polygon.setVisible(true);

    }

    private void addPointToPolygon(Double x, Double y) {
        var points=polygon.getPoints();
        var n = points.size();
        points.set(n - 2, x);
        points.set(n - 1, y);
        points.add(x);
        points.add(y);
        selectedPoints.add(x);
        selectedPoints.add(y);
    }

    private void updatePolygon() {
        var points = polygon.getPoints();
        var n = points.size();
        if (points.size() > 1) {
            points.set(n - 2, currentX);
            points.set(n - 1, currentY);
        }
    }

    private void clearPolygon() {
        isBuilding = false;
        polygon.setVisible(false);
        polygon.getPoints().clear();
        currentX = 0D;
        currentY = 0D;
    }

    private void build(Polygon polygon) {
        if (polygon.getPoints().size() > 4) {
            var points = JFXUtil.listToPoints(polygon.getPoints());
            PolygonView.fromParentCoords(points, paneController.getAutoscale())
                    .ifPresent(view -> {
                        // TODO add editor
                        var loc = new Localization<>(view, paneController);
                        eventBus.publish(new NewPolygonEvent(loc));
                    });
        }
    }

    public boolean isDisabled() {
        return disabled.get();
    }

    @Override
    public BooleanProperty disabledProperty() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled.set(disabled);
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
}
