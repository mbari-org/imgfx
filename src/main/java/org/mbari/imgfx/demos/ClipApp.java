package org.mbari.imgfx.demos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ClipApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ImageView imageView = new ImageView("https://hips.hearstapps.com/ghk.h-cdn.co/assets/18/01/2048x1024/landscape-1515004324-boston-terrier.jpg?resize=480:*");
        StackPane stackPane = new StackPane(imageView);
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.show();

        Circle circle = new Circle(60);
        circle.centerXProperty().bind(stackPane.widthProperty().divide(2.));
        circle.centerYProperty().bind(stackPane.heightProperty().divide(2.));
        stackPane.setClip(circle);
    }
}