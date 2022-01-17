package org.mbari.imgfx.imageview;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.mbari.imgfx.AutoscalePaneController;

public class ImagePaneController implements AutoscalePaneController<ImageView> {

    private final Pane pane;
    private final ImageView imageView;
    private final ImageViewAutoscale imageViewDecorator;

    public ImagePaneController(ImageView imageView) {
        this.imageView = imageView;
        this.imageViewDecorator = new ImageViewAutoscale(imageView);
        this.pane = new Pane() {
            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                double width = getWidth();
                double height = getHeight();
                // Fit the image view to match this container, the aspect ratio will be preserved
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                // Adjust the image view origin to center within this container
                layoutInArea(imageView, 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
            }
        };
        init();
    }

    private void init() {
        imageView.setPreserveRatio(true);
        pane.setPadding(Insets.EMPTY);
        pane.setBorder(null);
        pane.getChildren().add(imageView);
    }

    public Pane getPane() {
        return pane;
    }

    public ImageView getView() {
        return imageView;
    }

    public ImageViewAutoscale getAutoscale() {
        return imageViewDecorator;
    }


}
