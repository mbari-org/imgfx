/*
 * Copyright © 2025 MBARI (brian@mbari.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mbari.imgfx.demos;

import javafx.scene.paint.Color;
import org.mbari.imgfx.imageview.ImagePaneController;
import org.mbari.imgfx.etc.javafx.controls.CrossHairs;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.mbari.imgfx.roi.MarkerView;

public class MarkerViewDemo extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(getClass().getSimpleName());
        var imageUrl = getClass().getResource("/earth.jpg");
        var image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        ImagePaneController rip = new ImagePaneController(imageView);
        var pane = rip.getPane();

        var crossHairs = new CrossHairs();
        pane.getChildren().addAll(crossHairs.getNodes());


        var scene = new Scene(pane, 640, 480);
        scene.setOnMouseClicked(event -> {
            MarkerView.fromSceneCoords(event.getSceneX(), event.getSceneY(), 6D, rip.getAutoscale())
                    .ifPresent(view -> {
                        view.getData().setRadius(6D);
                        var shape = view.getView();
                        shape.setStroke(Color.valueOf("#FF9800"));
                        pane.getChildren().add(view.getView());
                    });
//            var p = new Point2D(event.getSceneX(), event.getSceneY());
//            var imagePoint = rip.getImageViewDecorator().sceneToImage(p);
//            var data = new CircleData(imagePoint.getX(), imagePoint.getY(), 6);
//            var view = new MarkerView(data, rip.getImageViewDecorator());
//            var shape = view.getView();
//            shape.setStroke(Color.valueOf("#FF9800"));
//            pane.getChildren().add(view.getView());

        });


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
