package org.mbari.imgfx;

import org.mbari.imgfx.glass.GlassItem;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;

/**
 * A resizeable image pane with a a "glass" layer allowing objects to be drawn 
 * on top of the image.
 */
public class GlassImagePaneController {

  private final Pane root; // For addign nodes to 
  private final ImageView imageView;
  private final ImageViewExt imageViewExt;
  private ObservableList<GlassItem> glassItems = FXCollections.observableArrayList(); 

  public GlassImagePaneController(ImageView imageView) {
    this.imageView = imageView;
    imageView.setPreserveRatio(true);

    this.imageViewExt = new ImageViewExt(imageView);

    this.root = new Pane() {
      @Override
      protected void layoutChildren() {
        double width = getWidth();
        double height = getHeight();
        // Fit the image view to match this container, the aspect ratio will be preserved
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        // Adjust the image view origin to center within this container
        layoutInArea(imageView, 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
        redrawGlass();
      }
    };
    root.setPadding(Insets.EMPTY);
    root.setBorder(null);
    root.getChildren().add(imageView);


    glassItems.addListener((ListChangeListener.Change<? extends GlassItem> c) -> {
      while (c.next()) {
        if (c.wasAdded()) {
          var items = c.getAddedSubList();
          items.forEach(gi -> {
            gi.doLayout(imageViewExt);
            root.getChildren().addAll(gi.getNodes());
          });
        }
        if (c.wasRemoved()) {
          var items = c.getRemoved();
          items.forEach(gi -> {
            root.getChildren().removeAll(gi.getNodes());
          });
        }
      }
      imageViewExt.recomputeScale();
      redrawGlass();
    });
  }

  private void redrawGlass() {
    glassItems.forEach(i -> i.doLayout(imageViewExt));
  }

  public ObservableList<GlassItem> glassItems() {
    return glassItems;
  }

  public Pane getRoot() {
    return root;
  }


  public ImageView getImageView() {
    return imageView;
  }

  public ImageViewExt getImageViewExt() {
    return imageViewExt;
  }
  
}
