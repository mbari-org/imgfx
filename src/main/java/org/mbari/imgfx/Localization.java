package org.mbari.imgfx;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import org.mbari.imgfx.ext.jfx.MutablePoint;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;

import java.util.UUID;

public class Localization<A extends Data, B extends Shape> {

    private UUID uuid;
    private UUID imageUuid;
    private final DataView<A, B> dataView;
    private final StringProperty label = new SimpleStringProperty();
    private final ImagePaneController paneController;
    private final Text labelView = new Text();

    public Localization(DataView<A, B> dataView, ImagePaneController paneController) {
        this(dataView, paneController, UUID.randomUUID(), null);
    }

    public Localization(DataView<A, B> dataView, ImagePaneController paneController, String labelText) {
        this(dataView, paneController, UUID.randomUUID(), null, labelText);
    }

    public Localization(DataView<A, B> dataView,
                        ImagePaneController paneController,
                        UUID uuid,
                        UUID imageUuid) {
        this(dataView, paneController, uuid, imageUuid, null);
    }

    public Localization(DataView<A, B> dataView,
                        ImagePaneController paneController,
                        UUID uuid,
                        UUID imageUuid,
                        String labelText) {
        this.dataView = dataView;
        this.paneController = paneController;
        this.uuid = uuid;
        this.imageUuid = imageUuid;
        label.set(labelText);
        init();

    }

    private void init() {
        var labelLocationHint = dataView.getLabelLocationHint();

        labelView.setTextOrigin(VPos.BOTTOM);
        labelView.layoutXProperty().bind(labelLocationHint.xProperty());
        labelView.layoutYProperty().bind(labelLocationHint.yProperty());
        labelView.textProperty().bind(label);
        paneController.getPane().getChildren().addAll(dataView.getView(), labelView);

        var colorBinding = new ObjectBinding<Color>() {
            {
                super.bind(dataView.getView().fillProperty(), dataView.getView().strokeProperty());
            }

            @Override
            protected Color computeValue() {
                var color = (Color) dataView.getView().getFill();
                if (color == null) {
                    color = (Color) dataView.getView().getStroke();
                }
                return Color.color(color.getRed(), color.getGreen(), color.getBlue());
            }
        };

        labelView.strokeProperty().bind(colorBinding);

    }


    public DataView<A, B> getDataView() {
        return dataView;
    }

    public ImagePaneController getPaneController() {
        return paneController;
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getImageUuid() {
        return imageUuid;
    }

    public void setImageUuid(UUID imageUuid) {
        this.imageUuid = imageUuid;
    }
}
