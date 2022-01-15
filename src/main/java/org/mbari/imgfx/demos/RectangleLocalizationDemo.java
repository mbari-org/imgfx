package org.mbari.imgfx.demos;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.BuilderCoordinator;
import org.mbari.imgfx.tools.RectangleBuilder;
import org.mbari.imgfx.events.NewRectangleEvent;
import org.mbari.imgfx.ext.rx.EventBus;
import org.mbari.imgfx.ext.jfx.controls.CrossHairs;
import org.mbari.imgfx.roi.RectangleView;

import java.time.LocalTime;


public class RectangleLocalizationDemo extends Application {

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
        var rp = new RectangleBuilder(paneController, eventBus);
        rp.setDisabled(false);

        var builderCoordinator = new BuilderCoordinator();
        builderCoordinator.addBuilder(rp);
        builderCoordinator.setCurrentBuilder(rp);

        eventBus.toObserverable()
                .ofType(NewRectangleEvent.class)
                .subscribe(event -> {
                    var loc = event.localization();
                    loc.setLabel(LocalTime.now().toString());
                    loc.getDataView()
                            .getView()
                            .setFill(Paint.valueOf("#4FC3F730"));
                    builderCoordinator.addLocalization(loc);
                });

        ChangeListener<? super Boolean> editChangeListener = (obs, oldv, newv) -> {
            // If one is being edited, disable all other.
            // if any are being edited disabpel rectablePublisher.
        };

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
