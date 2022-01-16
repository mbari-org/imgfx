package org.mbari.imgfx.demos;

import javafx.scene.paint.Paint;
import org.mbari.imgfx.BuilderCoordinator;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.etc.rx.events.NewMarkerEvent;
import org.mbari.imgfx.etc.jfx.controls.CrossHairs;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.tools.MarkerBuilder;

import java.time.LocalTime;

public class MarkerLocalizationDemo extends Application {


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
        var builder = new MarkerBuilder(paneController, eventBus);
        builderCoordinator.setCurrentBuilder(builder);
        builder.setDisabled(false);

        eventBus.toObserverable()
                .ofType(NewMarkerEvent.class)
                .subscribe(event -> {
                    var loc = event.localization();
                    loc.setLabel(LocalTime.now().toString());
                    var shape = loc.getDataView().getView();
                    shape.setStroke(Paint.valueOf("#FF9800"));
                    shape.setStrokeWidth(3);
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
