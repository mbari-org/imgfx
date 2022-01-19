package org.mbari.imgfx.demos.simple;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.mbari.imgfx.BuilderCoordinator;
import org.mbari.imgfx.imageview.ImagePaneController;
import org.mbari.imgfx.etc.jfx.controls.CrossHairs;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.etc.rx.events.AddLocalizationEvent;

import java.time.LocalTime;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        var borderPane = new BorderPane();

        // THis is important or center pane doesn't shrink when stage is shrunk
        borderPane.setMinSize(0, 0);

        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        var imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        var controller = new ImagePaneController(imageView);
        var pane = controller.getPane();
        var crossHairs = new CrossHairs();
        pane.getChildren().addAll(crossHairs.getNodes());
        var eventBus = new EventBus();
        var builderCoordinator = new BuilderCoordinator();
        eventBus.toObserverable()
                .ofType(AddLocalizationEvent.class)
                .subscribe(event -> {
                    var loc = event.localization();
                    loc.setLabel(LocalTime.now().toString());
                    loc.getDataView()
                            .getView()
                            .setFill(Paint.valueOf("#4FC3F730"));
                    builderCoordinator.addLocalization(loc);
                });


        borderPane.setCenter(pane);
        borderPane.setLeft(new ToolsPaneController(controller, eventBus).getRoot());
        var scene = new Scene(borderPane, 640, 480);
//        scene.widthProperty()
//                .addListener((obs, oldv, newv) -> pane.setPrefWidth(newv.doubleValue()));
//        scene.heightProperty()
//                .addListener((obs, oldv, newv) -> pane.setPrefHeight(newv.doubleValue()));
        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
