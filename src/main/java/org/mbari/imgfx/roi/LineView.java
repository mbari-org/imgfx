package org.mbari.imgfx.roi;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.mbari.imgfx.Autoscale;
import org.mbari.imgfx.etc.javafx.MutablePoint;

import java.util.Optional;

public class LineView implements DataView<LineData, Line> {

    private final LineData data;
    private final Line view;
    private final Autoscale<?> autoscale;
    private final BooleanProperty editing = new SimpleBooleanProperty();
    private final MutablePoint labelLocationHint = new MutablePoint();

    public LineView(LineData data, Autoscale<?> autoscale) {
        this.data = data;
        this.autoscale = autoscale;
        this.view = new Line();
        init();
    }

    private void init() {
        view.setStrokeWidth(3);

        EditingDecorator.decorate(this);

        ChangeListener<? super Number> dataChangeListener = (obs, oldv, newv) -> {
            if (!editing.get()) {
                updateView();
            }
        };
        data.endXProperty().addListener(dataChangeListener);
        data.endYProperty().addListener(dataChangeListener);
        data.startXProperty().addListener(dataChangeListener);
        data.startYProperty().addListener(dataChangeListener);

        ChangeListener<? super Number> viewChangeListener = (obs, oldv, newv) -> {
            if (editing.get()) {
                updateData();
            }
        };
        view.endXProperty().addListener(viewChangeListener);
        view.endYProperty().addListener(viewChangeListener);
        view.startXProperty().addListener(viewChangeListener);
        view.startYProperty().addListener(viewChangeListener);

        // labelLocationHint is mid-point of the line
        var xBinding = new DoubleBinding() {
            {
                super.bind(view.startXProperty(), view.endXProperty());
            }

            @Override
            protected double computeValue() {
                var x0 = view.getStartX();
                var x1 = view.getEndX();
                return Math.min(x0, x1) + Math.abs(x0 - x1) / 2 ;
            }
        };

        var yBinding = new DoubleBinding() {
            {
                super.bind(view.startYProperty(), view.endYProperty());
            }

            @Override
            protected double computeValue() {
                var y0 = view.getStartY();
                var y1 = view.getEndY();
                return Math.min(y0, y1) + Math.abs(y0 - y1) / 2;
            }
        };


        labelLocationHint.xProperty().bind(xBinding);
        labelLocationHint.yProperty().bind(yBinding);
        updateView();
    }

    @Override
    public void setColor(Color color) {
        var opaque = Color.color(color.getRed(), color.getGreen(), color.getBlue());
        getView().setStroke(opaque);
    }

    @Override
    public LineData getData() {
        return data;
    }

    @Override
    public Line getView() {
        return view;
    }

    @Override
    public Autoscale<?> getAutoscale() {
        return autoscale;
    }

    @Override
    public boolean isEditing() {
        return editing.get();
    }

    @Override
    public BooleanProperty editingProperty() {
        return editing;
    }

    @Override
    public void setEditing(boolean editing) {
        this.editing.set(editing);
    }

    @Override
    public void updateView() {
        var layoutStartXY = autoscale.unscaledToParent(new Point2D(data.getStartX(), data.getStartY()));
        var layoutEndXY = autoscale.unscaledToParent(new Point2D(data.getEndX(), data.getEndY()));
        view.setStartX(layoutStartXY.getX());
        view.setStartY(layoutStartXY.getY());
        view.setEndX(layoutEndXY.getX());
        view.setEndY(layoutEndXY.getY());
    }

    @Override
    public void updateData() {
        var imageStartXY = autoscale.parentToUnscaled(new Point2D(view.getStartX(), view.getStartY()));
        var imageEndXY = autoscale.parentToUnscaled(new Point2D(view.getEndX(), view.getEndY()));
        var opt = LineData.clip(imageStartXY.getX(),
                imageStartXY.getY(),
                imageEndXY.getX(),
                imageEndXY.getY(),
                autoscale);
        if (opt.isPresent()) {
            var line = opt.get();
            data.setStartX(line.getStartX());
            data.setStartY(line.getStartY());
            data.setEndX(line.getEndX());
            data.setEndY(line.getEndY());
        }
        else {
            data.setStartX(0);
            data.setStartY(0);
            data.setEndX(0);
            data.setEndY(0);
        }
    }

    @Override
    public MutablePoint getLabelLocationHint() {
        return labelLocationHint;
    }

    public static Optional<LineView> fromImageCoords(double startX, double startY, double endX, double endY, Autoscale<?> autoscale) {
        return LineData.clip(startX, startY, endX, endY, autoscale)
                .map(data -> new LineView(data, autoscale));
    }

    public static Optional<LineView> fromSceneCoords(double startX, double startY, double endX, double endY, Autoscale<?> autoscale) {
        var sceneStart = new Point2D(startX, startY);
        var sceneEnd = new Point2D(endX, endY);
        var imageStart = autoscale.sceneToUnscaled(sceneStart);
        var imageEnd = autoscale.sceneToUnscaled(sceneEnd);
        return fromImageCoords(imageStart.getX(), imageStart.getY(), imageEnd.getX(), imageEnd.getY(), autoscale);
    }

    public static Optional<LineView> fromParentCoords(double startX, double startY, double endX, double endY, Autoscale<?> autoscale) {
        var sceneStart = new Point2D(startX, startY);
        var sceneEnd = new Point2D(endX, endY);
        var imageStart = autoscale.parentToUnscaled(sceneStart);
        var imageEnd = autoscale.parentToUnscaled(sceneEnd);
        return fromImageCoords(imageStart.getX(), imageStart.getY(), imageEnd.getX(), imageEnd.getY(), autoscale);
    }

    @Override
    public String toString() {
        return "LineView[data=" + data + ", editing=" + editing.get() + "]";
    }

    
}
