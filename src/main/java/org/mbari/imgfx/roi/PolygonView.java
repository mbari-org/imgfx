package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.mbari.imgfx.Autoscale;
import org.mbari.imgfx.etc.javafx.JFXUtil;
import org.mbari.imgfx.etc.javafx.MutablePoint;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PolygonView implements DataView<PolygonData, Polygon> {

    private final PolygonData data;
    private final Polygon view;
    private final Autoscale<?> autoscale;
    private final BooleanProperty editing = new SimpleBooleanProperty();
    private final MutablePoint labelLocationHint = new MutablePoint();

    public PolygonView(PolygonData data, Autoscale<?> autoscale) {
        this.data = data;
        this.autoscale = autoscale;
        this.view = new Polygon();
        init();
    }

    private void init() {

        EditingDecorator.decorate(this);
        data.getPoints().addListener((ListChangeListener<? super Point2D>) c -> {
            if (!editing.get()) {
                updateView();
            }
        });

        view.getPoints().addListener((ListChangeListener<? super Double>) c -> {
            if (editing.get()) {
                updateData();
            }
        });

        // labelLocationHint is top center (of mean of points)
        view.getPoints().addListener((ListChangeListener<? super Double>) e -> {
            // avg x
            var points = JFXUtil.listToPoints(view.getPoints());
            var avgX = points.stream()
                    .mapToDouble(Point2D::getX)
                    .average()
                    .orElse(0D);

            var minY = points.stream()
                    .mapToDouble(Point2D::getY)
                    .min()
                    .orElse(0D);
            labelLocationHint.xProperty().set(avgX);
            labelLocationHint.yProperty().set(minY);

        });

        updateView();
    }

    @Override
    public PolygonData getData() {
        return data;
    }

    @Override
    public Polygon getView() {
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
        var layoutPoints = data.getPoints()
                .stream()
                .map(autoscale::unscaledToParent)
                .collect(Collectors.toList());
        var layoutArray = JFXUtil.pointsToArray(layoutPoints);
        view.getPoints().setAll(layoutArray);
    }

    @Override
    public void updateData() {
        var layoutArray = view.getPoints().toArray(Double[]::new);
        var layoutPoints = JFXUtil.arrayToPoints(layoutArray);
        var imagePoints = layoutPoints.stream()
                .map(autoscale::parentToUnscaled)
                .collect(Collectors.toList());
        var opt = PolygonData.clip(imagePoints, autoscale);
        if (opt.isPresent()) {
            var p = opt.get();
            data.getPoints().setAll(p.getPoints());
        }
        else {
            data.getPoints().clear();
        }
    }

    @Override
    public void setColor(Color color) {
        getView().setFill(color);
    }

    @Override
    public MutablePoint getLabelLocationHint() {
        return labelLocationHint;
    }

    public static Optional<PolygonView> fromImageCoords(List<Point2D> points, Autoscale<?> decorator) {
        return PolygonData.clip(points, decorator)
                .map(data -> new PolygonView(data, decorator));
    }

    public static Optional<PolygonView> fromSceneCoords(Collection<Point2D> points, Autoscale<?> decorator) {
        var imagePoints = points.stream()
                .map(decorator::sceneToUnscaled)
                .collect(Collectors.toList());
        return fromImageCoords(imagePoints, decorator);
    }

    public static Optional<PolygonView> fromParentCoords(Collection<Point2D> points, Autoscale<?> decorator) {
        var imagePoints = points.stream()
                .map(decorator::parentToUnscaled)
                .collect(Collectors.toList());
        return fromImageCoords(imagePoints, decorator);
    }
}
