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
package org.mbari.imgfx.etc.javafx;

import javafx.geometry.Point2D;

public record LineEqn(double slope, double intercept) {

    public static LineEqn from(Double startX, Double startY, Double endX, Double endY) {
        var dx = endX - startX;
        var dy = endY - startY;
        var slope = dy / dx;
        var intercept = startY - startX * slope;
        return new LineEqn(slope, intercept);
    }

    public double y(double x) {
        return slope * x + intercept;
    }

    public double x(double y) {
        return (y - intercept) / slope;
    }

    public Point2D intersect(LineEqn that) {
        var x0 = (that.intercept() - this.intercept()) / (this.slope - that.slope());
        var y0 = y(x0);
        return new Point2D(x0, y0);
    }

    @Override
    public String toString() {
        return String.format("y = %.2f * x + %.2f", slope, intercept);
    }
}
