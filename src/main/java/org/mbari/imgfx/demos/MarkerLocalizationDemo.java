package org.mbari.imgfx.demos;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.ext.jfx.controls.CrossHairs;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.mbari.imgfx.roi.MarkerView;

import java.time.LocalTime;

public class MarkerLocalizationDemo extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(getClass().getSimpleName());
        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        ImagePaneController controller = new ImagePaneController(imageView);
        var pane = controller.getPane();

        var crossHairs = new CrossHairs();
        pane.getChildren().addAll(crossHairs.getNodes());
        var decorator = controller.getImageViewDecorator();
        var fill = new SimpleObjectProperty<>(Color.valueOf("#FF9800"));


        var scene = new Scene(pane, 640, 480);
        scene.setOnMouseClicked(event -> {
            MarkerView.fromSceneCoords(event.getSceneX(), event.getSceneY(), 10D, decorator)
                    .ifPresent(view -> {
                        view.getData().setRadius(6); // Set radius in image coords to keep the sizes consstent
//                        view.getView().fillProperty().bind(fill);
                        view.getView().setStroke(fill.get());
                        view.getView().setStrokeWidth(3);
                        new Localization<>(view, controller, LocalTime.now().toString());
                    });
        });


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
