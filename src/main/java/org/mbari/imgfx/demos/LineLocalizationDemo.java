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
import org.mbari.imgfx.BuilderCoordinator;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.events.NewLineEvent;
import org.mbari.imgfx.events.NewRectangleEvent;
import org.mbari.imgfx.ext.jfx.controls.CrossHairs;
import org.mbari.imgfx.ext.rx.EventBus;
import org.mbari.imgfx.roi.RectangleView;
import org.mbari.imgfx.tools.LineBuilder;
import org.mbari.imgfx.tools.RectangleBuilder;

import java.time.LocalTime;

public class LineLocalizationDemo extends Application {

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

        var builder = new LineBuilder(paneController, eventBus);
        builder.setDisabled(false);
        var localizationController = new BuilderCoordinator(eventBus);
        localizationController.addBuilder(builder);
        localizationController.setCurrentBuilder(builder);

        eventBus.toObserverable()
                .ofType(NewLineEvent.class)
                .subscribe(loc -> {
                    loc.localization().setLabel(LocalTime.now().toString());
                    var shape = loc.localization().getDataView().getView();
                    shape.setStroke(Paint.valueOf("#4FC3F7"));
                    shape.setStrokeWidth(3);
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
