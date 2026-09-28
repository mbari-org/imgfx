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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ClipApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ImageView imageView = new ImageView("https://hips.hearstapps.com/ghk.h-cdn.co/assets/18/01/2048x1024/landscape-1515004324-boston-terrier.jpg?resize=480:*");
        StackPane stackPane = new StackPane(imageView);
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.show();

        Circle circle = new Circle(60);
        circle.centerXProperty().bind(stackPane.widthProperty().divide(2.));
        circle.centerYProperty().bind(stackPane.heightProperty().divide(2.));
        stackPane.setClip(circle);
    }
}
