package org.mbari.imgfx.demos.imageview.editor;

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
import javafx.stage.Stage;
import org.mbari.imgfx.etc.rx.EventBus;
import org.mbari.imgfx.etc.rx.events.Event;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class App extends Application {

    private static final System.Logger log = System.getLogger(App.class.getSimpleName());

    @Override
    public void start(Stage stage) throws Exception {
        var eventBus = new EventBus();
        var paneController = new AnnotationPaneController(eventBus);
        var concepts = loadDefaultConcepts();
        paneController.setConcepts(concepts);

        var imageUrl = getClass().getResource("/20220828T160015Z--2efffc23-efd3-4fe7-af45-ce2076bb33ca.png");
        var image = new Image(imageUrl.toExternalForm());
        paneController.getAutoscalePaneController()
                .getView()
                .setImage(image);

        paneController.getEventBus()
                .toObserverable()
                .ofType(Event.class)
                .subscribe(event -> log.log(System.Logger.Level.INFO, event));

        var scene = new Scene(paneController.getPane(), 640, 480);
        stage.setScene(scene);
        stage.show();

    }

    private List<String> loadDefaultConcepts() {
        var url = getClass().getResource("/default-concepts.txt");
        System.out.println(url);
        try {
            return Files.readAllLines(Path.of(url.toURI()),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("Apple", "Banana", "Nanomia", "Nanomia bijuga");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
