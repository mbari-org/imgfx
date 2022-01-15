package org.mbari.imgfx.demos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.mbari.imgfx.ImagePaneController;
import org.mbari.imgfx.Localization;
import org.mbari.imgfx.RectanglePublisher;
import org.mbari.imgfx.events.NewLocalizationEvent;
import org.mbari.imgfx.events.NewRectangleEvent;
import org.mbari.imgfx.ext.rx.EventBus;
import org.mbari.imgfx.controls.CrossHairs;
import org.mbari.imgfx.roi.RectangleData;
import org.mbari.imgfx.roi.RectangleView;
import org.mbari.imgfx.roi.RectangleViewEditor;

public class RectangleLocalizationDemo extends Application {

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
        var rp = new RectanglePublisher(paneController, eventBus);
        rp.setDisable(false);

        eventBus.toObserverable()
                .ofType(NewRectangleEvent.class)
                .subscribe(loc -> addEditing(loc.localization(), paneController, rp));



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

    private void addEditing(Localization<RectangleView> loc,
                            ImagePaneController controller,
                            RectanglePublisher rectanglePublisher) {
        var dataView = loc.getDataView();
        var editor = new RectangleViewEditor(dataView, controller.getPane());
        var view = dataView.getView();
        view.setFill(Paint.valueOf("#4FC3F730"));
        view.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            editor.setEditing(true);
            rectanglePublisher.setDisable(true);
        });
    }
}