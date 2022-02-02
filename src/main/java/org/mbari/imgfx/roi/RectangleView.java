package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.mbari.imgfx.Autoscale;
import org.mbari.imgfx.etc.javafx.MutablePoint;

import java.util.Optional;

public class RectangleView implements DataView<RectangleData, Rectangle> {

    private final RectangleData data;
    private final Rectangle view;
    private final Autoscale<?> autoscale;
    private final BooleanProperty editing = new SimpleBooleanProperty();
    private final MutablePoint labelLocationHint = new MutablePoint();

    public RectangleView(RectangleData data, Autoscale<?> autoscale) {
        this.data = data;
        this.autoscale = autoscale;
        this.view = new Rectangle();
        init();
    }

    private void init() {

        EditingDecorator.decorate(this);

        ChangeListener<? super Number> dataChangeListener = (obs, oldv, newv) -> {
            if (!editing.get()) {
                updateView();
            }
        };
        data.xProperty().addListener(dataChangeListener);
        data.yProperty().addListener(dataChangeListener);
        data.widthProperty().addListener(dataChangeListener);
        data.heightProperty().addListener(dataChangeListener);

        ChangeListener<? super Number> viewChangeListener = (obs, oldv, newv) -> {
            if (editing.get()) {
                updateData();
            }
        };
        view.xProperty().addListener(viewChangeListener);
        view.xProperty().addListener(viewChangeListener);
        view.widthProperty().addListener(viewChangeListener);
        view.heightProperty().addListener(viewChangeListener);
        view.parentProperty().addListener((obs, oldv, newv) -> updateView());

        labelLocationHint.xProperty().bind(view.xProperty());
        labelLocationHint.yProperty().bind(view.yProperty());

        updateView();

    }

    public void updateData() {
        var imageXY = autoscale.parentToUnscaled(new Point2D(view.getX(), view.getY()));
        var imageWidth = view.getWidth() / autoscale.getScaleX();
        var imageHeight = view.getHeight() / autoscale.getScaleY();
        var opt = RectangleData.clip(imageXY.getX(),
                imageXY.getY(),
                imageWidth,
                imageHeight,
                autoscale);
        if (opt.isPresent()) {
            var r = opt.get();
            data.setX(r.getX());
            data.setY(r.getY());
            data.setWidth(r.getWidth());
            data.setHeight(r.getHeight());
        }
        else {
            data.setY(0);
            data.setY(0);
            data.setWidth(0);
            data.setHeight(0);
        }
    }

    /**
     * Called when image is resized. It will never be in editing mode so we don't have to worry
     * about view changes modifying the data
     */
    public void updateView() {
        var layoutXY = autoscale.unscaledToParent(new Point2D(data.getX(), data.getY()));
        var layoutWidth = data.getWidth() * autoscale.getScaleX();
        var layoutHeight = data.getHeight() * autoscale.getScaleY();
        view.setX(layoutXY.getX());
        view.setY(layoutXY.getY());
        view.setWidth(layoutWidth);
        view.setHeight(layoutHeight);
    }

    @Override
    public RectangleData getData() {
        return data;
    }

    @Override
    public Rectangle getView() {
        return view;
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
    public Autoscale<?> getAutoscale() {
        return autoscale;
    }

    @Override
    public MutablePoint getLabelLocationHint() {
        return labelLocationHint;
    }

    @Override
    public void setColor(Color color) {
        getView().setFill(color);
    }

    public static Optional<RectangleView> fromImageCoords(Double x, Double y, Double width, Double height, Autoscale<?> autoscale) {
        return RectangleData.clip(x, y, width, height, autoscale)
                .map(data -> new RectangleView(data, autoscale));
    }

    public static Optional<RectangleView> fromSceneCoords(Double x, Double y, Double width, Double height, Autoscale<?> autoscale) {
        var sceneXY = new Point2D(x, y);
        var imageXY = autoscale.sceneToUnscaled(sceneXY);
        var imageWidth = width / autoscale.getScaleX();
        var imageHeight = height / autoscale.getScaleX();
        return fromImageCoords(imageXY.getX(), imageXY.getY(), imageWidth, imageHeight, autoscale);
    }

    public static Optional<RectangleView> fromParentCoords(Double x, Double y, Double width, Double height, Autoscale<?> autoscale) {
        var parentXY = new Point2D(x, y);
        var imageXY = autoscale.parentToUnscaled(parentXY);
        var imageWidth = width / autoscale.getScaleX();
        var imageHeight = height / autoscale.getScaleX();
        return fromImageCoords(imageXY.getX(), imageXY.getY(), imageWidth, imageHeight, autoscale);
    }

    @Override
    public String toString() {
        return "RectangleView[data=" + data + ", editing=" + editing.get() + "]";
    }

    
}
