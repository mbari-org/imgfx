package org.mbari.imgfx.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import org.mbari.imgfx.ImageViewDecorator;

public class RectangleView {

    private final RectangleData data;
    private final Rectangle view;
    private final ImageViewDecorator decorator;
    private final BooleanProperty edit = new SimpleBooleanProperty();

    public RectangleView(RectangleData data, ImageViewDecorator decorator) {
        this.data = data;
        this.decorator = decorator;
        this.view = new Rectangle();
    }

    private void init() {
        updateView();
        // Dont' edit while resizing. It mucks things up
        decorator.getImageView()
                .boundsInParentProperty()
                .addListener((obs, oldv, newv) -> {
                    edit.set(false);
                    updateView();
                });

        ChangeListener<? super Number> dataChangeListener = (obs, oldv, newv) -> {
            if (!edit.get()) {
                updateView();
            }
        };
        data.xProperty().addListener(dataChangeListener);
        data.yProperty().addListener(dataChangeListener);
        data.widthProperty().addListener(dataChangeListener);
        data.heightProperty().addListener(dataChangeListener);

        ChangeListener<? super Number> viewChangeListener = (obs, oldv, newv) -> {
            if (edit.get()) {
                updateData();
            }
        };
        view.xProperty().addListener(viewChangeListener);
        view.xProperty().addListener(viewChangeListener);
        view.widthProperty().addListener(viewChangeListener);
        view.heightProperty().addListener(viewChangeListener);
    }

    private void updateData() {
        var imageXY = decorator.parentToImage(new Point2D(view.getX(), view.getY()));
        var imageWidth = view.getWidth() / decorator.getScaleX();
        var imageHeight = view.getHeight() / decorator.getScaleY();
        data.setX(imageXY.getX());
        data.setY(imageXY.getY());
        data.setWidth(imageWidth);
        data.setHeight(imageHeight);
    }

    /**
     * Called when image is resized. It will never be in editing mode so we don't have to worry
     * about view changes modifying the data
     */
    private void updateView() {
        var layoutXY = decorator.imageToParent(new Point2D(data.getX(), data.getY()));
        var layoutWidth = data.getWidth() * decorator.getScaleX();
        var layoutHeight = data.getHeight() * decorator.getScaleY();
        view.setX(layoutXY.getX());
        view.setY(layoutXY.getY());
        view.setWidth(layoutWidth);
        view.setHeight(layoutHeight);
    }

    public RectangleData getData() {
        return data;
    }

    public Rectangle getView() {
        return view;
    }

    public boolean isEdit() {
        return edit.get();
    }

    public BooleanProperty editProperty() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit.set(edit);
    }
}
