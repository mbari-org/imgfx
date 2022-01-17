package org.mbari.imgfx.demos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.mbari.imgfx.BuilderCoordinator;
import org.mbari.imgfx.imageview.ImagePaneController;
import org.mbari.imgfx.etc.rx.events.NewCircleEvent;
import org.mbari.imgfx.etc.jfx.controls.CrossHairs;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.tools.CircleBuilder;

import java.time.LocalTime;

public class CircleLocalizationDemo extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(getClass().getSimpleName());
        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        ImagePaneController paneController = new ImagePaneController(imageView);
        var pane = paneController.getPane();
        var eventBus = new EventBus();

        var crossHairs = new CrossHairs();
        pane.getChildren().addAll(crossHairs.getNodes());
        var builderCoordinator = new BuilderCoordinator();
        var builder = new CircleBuilder(paneController, eventBus);
        builderCoordinator.setCurrentBuilder(builder);
        builder.setDisabled(false);

        eventBus.toObserverable()
                .ofType(NewCircleEvent.class)
                .subscribe(event -> {
                    var loc = event.localization();
                    loc.setLabel(LocalTime.now().toString());
                    var shape = loc.getDataView().getView();
                    shape.setFill(Paint.valueOf("#FF980090"));
                    builderCoordinator.addLocalization(loc);
                });


        var scene = new Scene(pane, 640, 480);
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