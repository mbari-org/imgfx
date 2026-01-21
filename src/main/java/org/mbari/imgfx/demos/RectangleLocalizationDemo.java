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
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.mbari.imgfx.imageview.ImagePaneController;
import org.mbari.imgfx.BuilderCoordinator;
import org.mbari.imgfx.roi.RectangleBuilder;
import org.mbari.imgfx.etc.rx.events.AddRectangleEvent;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.etc.javafx.controls.CrossHairs;

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
                .ofType(AddRectangleEvent.class)
                .subscribe(event -> {
                    var loc = event.localization();
                    loc.setLabel(LocalTime.now().toString());
                    loc.getDataView()
                            .getView()
                            .setFill(Paint.valueOf("#4FC3F730"));
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
