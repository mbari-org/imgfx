package org.mbari.imgfx.demos.app;

import org.mbari.imgfx.GlassImagePaneController;
import org.mbari.imgfx.glass.GlassRectangle;
import org.mbari.imgfx.glass.GlassCircle;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle(getClass().getSimpleName());
        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);



        GlassImagePaneController rip = new GlassImagePaneController(imageView);
        var root = rip.getRoot();

        Text text = new Text("100, 100");
        text.setLayoutX(100);
        text.setLayoutY(100);


        root.setOnMouseMoved(event -> {
            // event.getX -> relative to node origin
            // event.getSceneX -> relative to scene origin
            // event.getScreenX -> relative to screen orign
            var p = imageView.sceneToLocal(event.getSceneX(), event.getSceneY());

            text.setLayoutX(event.getSceneX());
            text.setLayoutY(event.getSceneY());

            var scenePoint = new Point2D(event.getSceneX(), event.getSceneY());
            var imagePoint = rip.getImageViewExt().sceneToImage(scenePoint);
            // var scenePoint2 = ext.imageToScene(imagePoint);

            var msg = String.format("S: %.1f, %.1f | I: %.1f, %.1f (scale:%.1f)", scenePoint.getX(),
                    scenePoint.getY(), imagePoint.getX(), imagePoint.getY(), rip.getImageViewExt().getScaleX());
            text.setText(msg);

        });
        rip.getRoot().getChildren().add(text);

        var box = new GlassRectangle(20, 20, 20, 20);
        var box1 = new GlassRectangle(30, 30, 40, 20);
        var circle1 = new GlassCircle(50, 100, 8);
        rip.glassItems().addAll(box, box1, circle1);
        

        var scene = new Scene(root, 640, 480);


        scene.widthProperty()
                .addListener((obs, oldv, newv) -> root.setPrefWidth(newv.doubleValue()));
        scene.heightProperty()
                .addListener((obs, oldv, newv) -> root.setPrefHeight(newv.doubleValue()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
