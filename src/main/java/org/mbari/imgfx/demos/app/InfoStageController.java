package org.mbari.imgfx.demos.app;

import org.mbari.imgfx.GlassImagePaneController;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InfoStageController {

  private final Stage stage;
  private final Scene scene;
  private final VBox root;
  private final GlassImagePaneController rip;
  
  
  public InfoStageController(GlassImagePaneController rip) {
    this.rip = rip;
    stage = new Stage();
    root = new VBox();
    scene = new Scene(root);
    stage.setScene(scene);
  }


  


  
}
