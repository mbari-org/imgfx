package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import org.mbari.imgfx.ImageViewDecorator;
import org.mbari.imgfx.util.JFXUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PolygonView implements DataView<PolygonData, Polygon> {

    private final PolygonData data;
    private final Polygon view;
    private final ImageViewDecorator decorator;
    private final BooleanProperty editing = new SimpleBooleanProperty();

    public PolygonView(PolygonData data, ImageViewDecorator decorator) {
        this.data = data;
        this.decorator = decorator;
        this.view = new Polygon();
        init();
    }

    private void init() {
        updateView();
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
    public ImageViewDecorator getImageViewDecorator() {
        return decorator;
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
                .map(decorator::imageToParent)
                .collect(Collectors.toList());
        var layoutArray = JFXUtil.pointsToArray(layoutPoints);
        view.getPoints().setAll(layoutArray);
    }

    @Override
    public void updateData() {
        var layoutArray = view.getPoints().toArray(Double[]::new);
        var layoutPoints = JFXUtil.arrayToPoints(layoutArray);
        var imagePoints = layoutPoints.stream()
                .map(decorator::parentToImage)
                .collect(Collectors.toList());
        var opt = PolygonData.clip(imagePoints, decorator.getImageView().getImage());
        if (opt.isPresent()) {
            var p = opt.get();
            data.getPoints().setAll(p.getPoints());
        }
        else {
            data.getPoints().clear();
        }
    }

}
