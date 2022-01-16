package org.mbari.imgfx.etc.jfx;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class JFXUtil {

    public static Double[] pointsToArray(List<Point2D> points) {
        var array = new Double[points.size() * 2];
        for (int i = 0; i < points.size(); i++) {
            int j = i * 2;
            var p = points.get(i);
            array[j] = p.getX();
            array[j + 1] = p.getY();
        }
        return array;
    }

    public static List<Point2D> arrayToPoints(Double[] array) {
        List<Point2D> points = new ArrayList<>();
        for (var i = 0; i < array.length - 1; i = i + 2) {
            var p = new Point2D(array[i], array[i + 1]);
            points.add(p);
        }
        return points;
    }

    public static List<Point2D> listToPoints(List<Double> list) {
        var array = list.toArray(Double[]::new);
        return arrayToPoints(array);
    }

    /**
     * https://stackoverflow.com/questions/38136408/how-to-determine-if-the-user-clicked-outside-a-particular-javafx-node
     * <pre>
     *     TextField textField = new TextField();
     *     StackPane root = new StackPane();
     *     root.getChildren().add(textField);
     *     textField.textProperty()
     *         .bind(Bindings.when(textField.focusedProperty())
     *         .then("Got the Focus!").otherwise("Please give me the focus!"));
     *
     *     Scene scene = new Scene(root, 500, 200);
     *
     *     scene.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
     *         if (!inHierarchy(evt.getPickResult().getIntersectedNode(), textField)) {
     *             root.requestFocus();
     *         }
     *     });
     * </pre>
     * @param node
     * @param potentialHierarchyElement
     * @return
     */
    public static boolean inHierarchy(Node node, Node potentialHierarchyElement) {
        if (potentialHierarchyElement == null) {
            return true;
        }
        while (node != null) {
            if (node == potentialHierarchyElement) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }

    public static double lineLength(Line line) {
        var dx = Math.abs(line.getStartX() - line.getEndX());
        var dy = Math.abs(line.getStartY() - line.getEndY());
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }


}
