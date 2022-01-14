package org.mbari.imgfx.demos;

import javafx.scene.paint.Color;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.controls.CrossHairs;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.mbari.imgfx.roi.CircleData;
import org.mbari.imgfx.roi.CircleView;

public class CircleViewDemo extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(getClass().getSimpleName());
        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        ImagePaneController rip = new ImagePaneController(imageView);
        var pane = rip.getPane();

        var crossHairs = new CrossHairs();
        pane.getChildren().addAll(crossHairs.getNodes());


        var scene = new Scene(pane, 640, 480);
        scene.setOnMouseClicked(event -> {
            CircleView.fromSceneCoords(event.getSceneX(), event.getSceneY(), 6D, rip.getImageViewDecorator())
                            .ifPresent(view -> {
                                view.getData().setRadius(6D); // set circle radius in image coordinates
                                var shape = view.getView();
                                shape.setFill(Color.valueOf("#FF980090"));
                                pane.getChildren().add(view.getView());
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
