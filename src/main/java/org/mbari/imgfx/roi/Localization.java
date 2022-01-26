package org.mbari.imgfx.roi;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import org.mbari.imgfx.AutoscalePaneController;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;

import java.util.UUID;

/**
 * This is a localized and rendered annotation.
 * @param <C>
 * @param <V>
 */
public class Localization<C extends DataView<? extends Data, ? extends Shape>, V extends Node> {

    private UUID uuid;
    private final C dataView;
    private final StringProperty label = new SimpleStringProperty();
    private final AutoscalePaneController<V> paneController;
    private final Text labelView = new Text();
    private BooleanProperty visible = new SimpleBooleanProperty();

    public Localization(C dataView, AutoscalePaneController<V> paneController) {
        this(dataView, paneController, UUID.randomUUID());
    }

    public Localization(C dataView, AutoscalePaneController<V> paneController, String labelText) {
        this(dataView, paneController, UUID.randomUUID(), labelText);
    }

    public Localization(C dataView,
                        AutoscalePaneController<V> paneController,
                        UUID uuid) {
        this(dataView, paneController, uuid, null);
    }

    public Localization(C dataView,
                        AutoscalePaneController<V> paneController,
                        UUID uuid,
                        String labelText) {
        this.dataView = dataView;
        this.paneController = paneController;
        this.uuid = uuid;
        label.set(labelText);
        init();

    }

    private void init() {
        var labelLocationHint = dataView.getLabelLocationHint();

        labelView.setTextOrigin(VPos.BOTTOM);
        labelView.layoutXProperty().bind(labelLocationHint.xProperty());
        labelView.layoutYProperty().bind(labelLocationHint.yProperty());
        labelView.textProperty().bind(label);

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

        visible.addListener((obs, oldv, newv) -> {
            if (newv) {
                paneController.getPane().getChildren().addAll(dataView.getView(), labelView);
            }
            else {
                paneController.getPane().getChildren().removeAll(dataView.getView(), labelView);
            }
        });

    }


    public C getDataView() {
        return dataView;
    }

    public AutoscalePaneController<V> getPaneController() {
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

    public boolean isVisible() {
        return visible.get();
    }

    public BooleanProperty visibleProperty() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }
}
