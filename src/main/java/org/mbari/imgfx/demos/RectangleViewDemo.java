package org.mbari.imgfx.demos;

/*-
 * #%L
 * org.mbari.imgfx:imgfx
 * %%
 * Copyright (C) 2025 - 2026 Monterey Bay Aquarium Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
