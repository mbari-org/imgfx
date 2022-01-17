package org.mbari.imgfx.demos;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.mbari.imgfx.imageview.ImagePaneController;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.roi.*;

import java.util.List;

public class LocalizationDemo extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(getClass().getSimpleName());
        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        ImagePaneController controller = new ImagePaneController(imageView);
        var pane = controller.getPane();
        var decorator = controller.getAutoscale();
        var fill = new SimpleObjectProperty<>(Color.valueOf("#FF980090"));

        CircleView.fromImageCoords(100D, 100D, 6D, decorator)
                .ifPresent(view -> {
                    view.getView().fillProperty().bind(fill);
                    new Localization<>(view, controller, "Circle");
                });

        LineView.fromImageCoords(10D, 250D, 400D, -10D, decorator)
                .ifPresent(view -> {
                    view.getView().fillProperty().bind(fill);
                    view.getView().strokeProperty().bind(fill);
                    view.getView().setStrokeWidth(3);
                    new Localization<>(view, controller, "Line");
                });

        MarkerView.fromImageCoords(400D, 100D, 6D, decorator)
                .ifPresent(view -> {
                    view.getView().strokeProperty().bind(fill);
                    view.getView().setStrokeWidth(3);
                    new Localization<>(view, controller, "Marker");
                });

        PolygonView.fromImageCoords(List.of(
                        new Point2D(130D, 210D),
                        new Point2D(150D, 215D),
                        new Point2D(143D, 220D),
                        new Point2D(117d, 350D),
                        new Point2D(110D, 218D),
                        new Point2D(120D, 211D)
                ), decorator)
                .ifPresent(view -> {
                    view.getView().fillProperty().bind(fill);
                    new Localization<>(view, controller, "Polygon");
                });

        RectangleView.fromImageCoords(200D, 200D, 100D, 50D, decorator)
                .ifPresent(view -> {
                    view.getView().fillProperty().bind(fill);
                    new Localization<>(view, controller, "Rectangle");
                });


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

}
