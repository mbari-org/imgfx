package org.mbari.imgfx.demos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.mbari.imgfx.BuilderCoordinator;
import org.mbari.imgfx.imageview.ImagePaneController;
import org.mbari.imgfx.etc.javafx.controls.CrossHairs;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.etc.rx.events.AddPolygonEvent;
import org.mbari.imgfx.roi.PolygonBuilder;

import java.time.LocalTime;

public class PolygonLocalizationDemo extends Application {

    // If any are being edited disable the publisher

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(getClass().getSimpleName());
        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        ImagePaneController paneController = new ImagePaneController(imageView);
        var pane = paneController.getPane();
        var eventBus = new EventBus();

        var builder = new PolygonBuilder(paneController, eventBus);
        builder.setEditColor(Color.valueOf("#FFA50090"));

        builder.setDisabled(false);
        var builderCoordinator = new BuilderCoordinator();
        builderCoordinator.setCurrentBuilder(builder);

        eventBus.toObserverable()
                .ofType(AddPolygonEvent.class)
                .subscribe(event -> {
                    var loc = event.localization();
                    loc.setLabel(LocalTime.now().toString());
                    var shape = loc.getDataView().getView();
                    shape.setFill(Paint.valueOf("#4FC3F780"));
                    loc.setVisible(true);
                    builderCoordinator.addLocalization(loc);
                });

        var crossHairs = new CrossHairs();
        pane.getChildren().addAll(crossHairs.getNodes());

        var scene = new Scene(pane, 640, 480);
        scene.widthProperty()
                .addListener((obs, oldv, newv) -> pane.setPrefWidth(newv.doubleValue()));
        scene.heightProperty()
                .addListener((obs, oldv, newv) -> pane.setPrefHeight(newv.doubleValue()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
