package org.mbari.imgfx.old.bb;

import org.mbari.imgfx.old.ImagePaneController;
import org.mbari.imgfx.controls.CrossHairs;
import org.mbari.imgfx.controls.SelectionRectangle;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.mbari.imgfx.old.glass.GlassRectangle;

public class PolygonDemo extends Application {


  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle(getClass().getSimpleName());
    var imageUrl = getClass().getResource("/earth.jpg");
    var image = new Image(imageUrl.toExternalForm());
    ImageView imageView = new ImageView(image);
    imageView.setPreserveRatio(true);

    ImagePaneController rip = new ImagePaneController(imageView);
    var root = rip.getRoot();

    var crossHairs = new CrossHairs();
    root.getChildren().addAll(crossHairs.getNodes());


    var scene = new Scene(root, 640, 480);

    var selectionRectangle = new SelectionRectangle();
    
    // Add to parent before setting the onCompleteController! THis makes sure its
    // gets added to the parent when you change the onCompleteController.
    rip.getRoot().getChildren().add(selectionRectangle.getRectangle());


    EventHandler<MouseEvent> onCompleteHandler = (e) -> {
      var ext = rip.getImageViewExt();

      var r = selectionRectangle.getRectangle();
      var sceneXY = new Point2D(r.getX(), r.getY());
      var imageXY = ext.sceneToImage(sceneXY);
      var width = r.getWidth() / ext.getScaleX();
      var height = r.getHeight() / ext.getScaleX();

      System.out.println(imageXY + "");
      var box = GlassRectangle.clip(imageXY.getX(), imageXY.getY(), width, height, rip.getImageView().getImage());
      box.ifPresent(b  -> rip.glassItems().add(b));

      

    };
    selectionRectangle.setOnCompleteHandler(onCompleteHandler);
    



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
