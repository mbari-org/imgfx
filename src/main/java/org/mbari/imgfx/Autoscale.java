package org.mbari.imgfx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;

/**
 * Base class used to automatically rescale nodes based on changes to their parent node's size.
 * @param <T> The type of Node that the autoscale watches for size changes
 */
public abstract class Autoscale<T extends Node> {

    protected final T view;

    protected final DoubleProperty scaleX = new SimpleDoubleProperty(Double.NaN);
    protected final DoubleProperty scaleY = new SimpleDoubleProperty(Double.NaN);

    private final ReadOnlyDoubleProperty scaleXReadOnly = ReadOnlyDoubleProperty.readOnlyDoubleProperty(scaleX);
    private final ReadOnlyDoubleProperty scaleYReadOnly = ReadOnlyDoubleProperty.readOnlyDoubleProperty(scaleY);

    public Autoscale(T view) {
        this.view = view;
    }

    public abstract Double getUnscaledWidth();
    public abstract Double getUnscaledHeight();

    public T getView() {
        return view;
    }

    public final void recomputeScale() {
        var sx = view.getBoundsInParent().getWidth() / getUnscaledWidth();
        var sy = view.getBoundsInParent().getHeight() / getUnscaledHeight();
        scaleX.set(sx);
        scaleY.set(sy);
    }

    public double getScaleX() {
        return scaleX.get();
    }

    public ReadOnlyDoubleProperty scaleXProperty() {
        return scaleXReadOnly;
    }

    public void setScaleX(double scaleX) {
        this.scaleX.set(scaleX);
    }

    public double getScaleY() {
        return scaleY.get();
    }

    public ReadOnlyDoubleProperty scaleYProperty() {
        return scaleYReadOnly;
    }

    /**
     * Converts a point in the scene to a point into the unscaled image.
     *
     * @param scene new Point2D(event.getSceneX(), event.getSceneY())
     * @return The point in the image that corresponds to the scene point,
     * unscaled. (basically pixel coordinates into the original image)
     */
    public Point2D sceneToUnscaled(Point2D scene) {
        var p = view.sceneToLocal(scene.getX(), scene.getY());
        var x = p.getX() / getScaleX();
        var y = p.getY() / getScaleY();
        return new Point2D(x, y);
    }

    /**
     * Converts a point in the unscaled image (i.e. pixel coordiats)
     * to the correspoding point in the scene.
     *
     * @param image
     * @return
     */
    public Point2D unscaledToScene(Point2D image) {
        var p = new Point2D(image.getX() * getScaleX(), image.getY() * getScaleY());
        var p2 = view.localToScene(p);
        return new Point2D(p2.getX(), p2.getY());
    }

    public Point2D parentToUnscaled(Point2D parent) {
        var imageBounds = getView().getBoundsInParent();
        var x = (parent.getX() - imageBounds.getMinX()) / getScaleX();
        var y = (parent.getY() - imageBounds.getMinY()) / getScaleY();
        return new Point2D(x, y);
    }

    public Point2D unscaledToParent(Point2D image) {
        var imageBounds = getView().getBoundsInParent();
        var x = imageBounds.getMinX() + (image.getX() * getScaleX());
        var y = imageBounds.getMinY() + (image.getY() * getScaleY());
        return new Point2D(x, y);
    }


}
