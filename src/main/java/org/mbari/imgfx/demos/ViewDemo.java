package org.mbari.imgfx.demos;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.roi.*;

import java.util.List;

public class ViewDemo extends Application {

    Pane pane;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(getClass().getSimpleName());
        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        ImagePaneController controller = new ImagePaneController(imageView);
        pane = controller.getPane();
        var decorator = controller.getImageViewDecorator();


        CircleView.fromImageCoords(100D, 100D, 6D, decorator)
                .map(DataView::getView)
                .ifPresent(this::addToPane);

        LineView.fromImageCoords(10D, 250D, 400D, -10D, decorator)
                .map(DataView::getView)
                .ifPresent(this::addToPane);

        MarkerView.fromImageCoords(400D, 100D, 6D, decorator)
                .map(DataView::getView)
                .ifPresent(this::addToPane);

        PolygonView.fromImageCoords(List.of(
                        new Point2D(130D, 210D),
                        new Point2D(150D, 215D),
                        new Point2D(143D, 220D),
                        new Point2D(117d, 350D),
                        new Point2D(110D, 218D),
                        new Point2D(120D, 211D)
                ), decorator)
                .map(DataView::getView)
                .ifPresent(this::addToPane);

        RectangleView.fromImageCoords(200D, 200D, 100D, 50D, decorator)
                .map(DataView::getView)
                .ifPresent(this::addToPane);

        var scene = new Scene(pane, 640, 480);
        scene.widthProperty()
                .addListener((obs, oldv, newv) -> pane.setPrefWidth(newv.doubleValue()));
        scene.heightProperty()
                .addListener((obs, oldv, newv) -> pane.setPrefHeight(newv.doubleValue()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void addToPane(Shape shape) {
        shape.setFill(Color.valueOf("#FF980090"));
        pane.getChildren().add(shape);
        shape.toFront();
    }

}
