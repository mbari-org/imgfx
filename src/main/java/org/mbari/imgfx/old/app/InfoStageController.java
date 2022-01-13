package org.mbari.imgfx.old.app;

import org.mbari.imgfx.old.ImagePaneController;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InfoStageController {

  private final Stage stage;
  private final Scene scene;
  private final VBox root;
  private final ImagePaneController rip;
  
  
  public InfoStageController(ImagePaneController rip) {
    this.rip = rip;
    stage = new Stage();
    root = new VBox();
    scene = new Scene(root);
    stage.setScene(scene);
  }


  


  
}
