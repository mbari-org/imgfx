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
package org.mbari.imgfx.roi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import org.mbari.imgfx.Autoscale;
import org.mbari.imgfx.etc.javafx.JFXUtil;
import org.mbari.imgfx.etc.javafx.SutherlandHodgman;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PolygonData implements Data {

//    private final List<Point2D> points;
    private final ObservableList<Point2D> points = FXCollections.observableArrayList();

    public PolygonData() {

    }

    public PolygonData(Collection<Point2D> points) {
        this.points.setAll(points);
    }

    public ObservableList<Point2D> getPoints() {
        return points;
    }

    public static Optional<PolygonData> clip(List<Point2D> points, Autoscale<?> autoscale) {
        var bounds = new BoundingBox(0, 0, autoscale.getUnscaledWidth(), autoscale.getUnscaledHeight());
        var boxed = JFXUtil.pointsToArray(points);
        var unboxed = Stream.of(boxed).mapToDouble(Double::doubleValue).toArray();
        var p = new Polygon(unboxed);
        return SutherlandHodgman.clip(p, bounds)
                        .map(clippedPolygon -> {
                            var clippedPoints = JFXUtil.listToPoints(clippedPolygon.getPoints());
                            return new PolygonData(clippedPoints);
                        });
    }

    @Override
    public String toString() {
        return "PolygonData[points=(" + points.size() + " points)]";
    }

    
}
