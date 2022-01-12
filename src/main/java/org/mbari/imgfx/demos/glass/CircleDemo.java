package org.mbari.imgfx.demos.glass;

import org.mbari.imgfx.GlassImagePaneController;
import org.mbari.imgfx.controls.CrossHairs;
import org.mbari.imgfx.glass.GlassCircle;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CircleDemo extends Application {


  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle(getClass().getSimpleName());
    var imageUrl = getClass().getResource("/earth.jpg");
    var image = new Image(imageUrl.toExternalForm());
    ImageView imageView = new ImageView(image);
    imageView.setPreserveRatio(true);

    GlassImagePaneController rip = new GlassImagePaneController(imageView);
    var root = rip.getRoot();

    var crossHairs = new CrossHairs();
    root.getChildren().addAll(crossHairs.getNodes());


    var scene = new Scene(root, 640, 480);
    scene.setOnMouseClicked(event -> {
      var p = new Point2D(event.getSceneX(), event.getSceneY());
      var imagePoint = rip.getImageViewExt().sceneToImage(p);
      var c = new GlassCircle(imagePoint.getX(), imagePoint.getY(), 6);
      rip.glassItems().add(c);
    });



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
