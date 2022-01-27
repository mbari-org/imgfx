package org.mbari.imgfx.imageview.editor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.etc.rx.events.Event;

import java.util.List;

public class App extends Application {

    private static final System.Logger log = System.getLogger(App.class.getSimpleName());

    @Override
    public void start(Stage stage) throws Exception {
        var eventBus = new EventBus();
        var paneController = new AnnotationPaneController(eventBus);
        var concepts = List.of("Apple", "Banana", "Nanomia", "Nanomia bijuga");
        paneController.setConcepts(concepts);

        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        paneController.getAutoscalePaneController()
                .getView()
                .setImage(image);

        paneController.getEventBus()
                .toObserverable()
                .ofType(Event.class)
                .subscribe(event -> log.log(System.Logger.Level.INFO, event));

        var scene = new Scene(paneController.getPane(), 640, 480);
        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
