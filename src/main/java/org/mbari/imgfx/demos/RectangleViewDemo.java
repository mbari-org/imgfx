package org.mbari.imgfx.demos;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.mbari.imgfx.imageview.ImagePaneController;
import org.mbari.imgfx.roi.RectangleViewEditor;
import org.mbari.imgfx.etc.javafx.controls.CrossHairs;
import org.mbari.imgfx.etc.javafx.controls.SelectionRectangle;
import org.mbari.imgfx.roi.RectangleView;

public class RectangleViewDemo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(getClass().getSimpleName());
        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        ImagePaneController paneController = new ImagePaneController(imageView);
        var pane = paneController.getPane();

        var crossHairs = new CrossHairs();
        pane.getChildren().addAll(crossHairs.getNodes());

        var selectionRectangle = new SelectionRectangle();

        // Add to parent before setting the onCompleteController! THis makes sure its
        // gets added to the parent when you change the onCompleteController.

        EventHandler<MouseEvent> onCompleteHandler = (e) -> {
            var decorator = paneController.getAutoscale();

            var r = selectionRectangle.getRectangle();


            RectangleView.fromSceneCoords(r.getX(), r.getY(), r.getWidth(), r.getHeight(), paneController.getAutoscale())
                            .ifPresent(view -> {
                                var shape = view.getView();
                                shape.setFill(Paint.valueOf("#4FC3F730"));
                                pane.getChildren().add(view.getView());
                                shape.toFront();

                                var editor = new RectangleViewEditor(view, pane);
                                view.setEditing(true);
                            });

        };



        selectionRectangle.setOnCompleteHandler(onCompleteHandler);

        pane.getChildren().add(selectionRectangle.getRectangle());

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
