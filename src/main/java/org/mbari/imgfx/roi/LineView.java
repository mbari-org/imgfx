package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import org.mbari.imgfx.ImageViewDecorator;

public class LineView implements DataView<LineData, Line> {

    private final LineData data;
    private final Line view;
    private final ImageViewDecorator decorator;
    private final BooleanProperty editing = new SimpleBooleanProperty();

    public LineView(LineData data, ImageViewDecorator decorator) {
        this.data = data;
        this.decorator = decorator;
        this.view = new Line();
        init();
    }

    private void init() {
        updateView();
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
        var layoutStartXY = decorator.parentToImage(new Point2D(data.getStartX(), data.getStartY()));
        var layoutEndXY = decorator.parentToImage(new Point2D(data.getEndX(), data.getEndY()));
        view.setStartX(layoutStartXY.getX());
        view.setStartY(layoutStartXY.getY());
        view.setEndX(layoutEndXY.getX());
        view.setEndY(layoutEndXY.getY());
    }

    @Override
    public void updateData() {
        var imageStartXY = decorator.parentToImage(new Point2D(view.getStartX(), view.getStartY()));
        var imageEndXY = decorator.parentToImage(new Point2D(view.getEndX(), view.getEndY()));
        var opt = LineData.clip(imageStartXY.getX(),
                imageStartXY.getY(),
                imageEndXY.getX(),
                imageEndXY.getY(),
                decorator.getImageView().getImage());
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
}
