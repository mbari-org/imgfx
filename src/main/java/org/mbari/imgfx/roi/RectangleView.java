package org.mbari.imgfx.roi;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import org.mbari.imgfx.ImageViewDecorator;
import org.mbari.imgfx.ext.jfx.MutablePoint;

import java.util.Optional;

public class RectangleView implements DataView<RectangleData, Rectangle> {

    private final RectangleData data;
    private final Rectangle view;
    private final ImageViewDecorator decorator;
    private final BooleanProperty editing = new SimpleBooleanProperty();
    private final MutablePoint labelLocationHint = new MutablePoint();

    public RectangleView(RectangleData data, ImageViewDecorator decorator) {
        this.data = data;
        this.decorator = decorator;
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
        var imageXY = decorator.parentToImage(new Point2D(view.getX(), view.getY()));
        var imageWidth = view.getWidth() / decorator.getScaleX();
        var imageHeight = view.getHeight() / decorator.getScaleY();
        var opt = RectangleData.clip(imageXY.getX(),
                imageXY.getY(),
                imageWidth,
                imageHeight,
                decorator.getImageView().getImage());
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
        var layoutXY = decorator.imageToParent(new Point2D(data.getX(), data.getY()));
        var layoutWidth = data.getWidth() * decorator.getScaleX();
        var layoutHeight = data.getHeight() * decorator.getScaleY();
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
    public ImageViewDecorator getImageViewDecorator() {
        return decorator;
    }

    @Override
    public MutablePoint getLabelLocationHint() {
        return labelLocationHint;
    }

    public static Optional<RectangleView> fromImageCoords(Double x, Double y, Double width, Double height, ImageViewDecorator decorator) {
        return RectangleData.clip(x, y, width, height, decorator.getImageView().getImage())
                .map(data -> new RectangleView(data, decorator));
    }

    public static Optional<RectangleView> fromSceneCoords(Double x, Double y, Double width, Double height, ImageViewDecorator decorator) {
        var sceneXY = new Point2D(x, y);
        var imageXY = decorator.sceneToImage(sceneXY);
        var imageWidth = width / decorator.getScaleX();
        var imageHeight = height / decorator.getScaleX();
        return fromImageCoords(imageXY.getX(), imageXY.getY(), imageWidth, imageHeight, decorator);
    }
}
