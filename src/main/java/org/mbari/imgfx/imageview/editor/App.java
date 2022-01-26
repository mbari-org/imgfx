package org.mbari.imgfx.imageview.editor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.mbari.imgfx.etc.rx.EventBus;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        var eventBus = new EventBus();
        var paneController = new AnnotationPaneController(eventBus);

        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        paneController.getAutoscalePaneController()
                .getView()
                .setImage(image);

        var scene = new Scene(paneController.getPane(), 640, 480);
        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
