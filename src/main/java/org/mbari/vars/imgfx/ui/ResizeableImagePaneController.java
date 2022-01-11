package org.mbari.vars.imgfx.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ResizeableImagePaneController {

  private final StackPane root = new StackPane();
  private final Pane glass = new Pane(); // For addign nodes to 
  private final ImageView imageView;
  private final ImageViewExt imageViewExt;
  private ObservableList<GlassItem> glassItems = FXCollections.observableArrayList(); 

  public ResizeableImagePaneController(ImageView imageView) {
    this.imageView = imageView;
    this.imageViewExt = new ImageViewExt(imageView);

    root.setPadding(Insets.EMPTY);
    glass.setPadding(Insets.EMPTY);

    root.getChildren().addAll(imageView, glass);
    root.widthProperty()
      .addListener((obs, oldv, newv) -> {
        imageView.setFitWidth(newv.doubleValue());
        glass.setLayoutX(imageView.getLayoutX());
      });
    root.heightProperty()
      .addListener((obs, oldv, newv) -> {
        imageView.setFitHeight(newv.doubleValue());
        glass.setLayoutX(imageView.getLayoutX());
        System.out.println("ImageView LayoutBounds: " +imageView.getLayoutBounds());
        System.out.println("Glass LayoutBounds    : " + glass.getLayoutBounds());
        System.out.println("Root LayoutBounds     : " + root.getLayoutBounds());
      });

    imageView.imageProperty().addListener(i -> redrawGlass());
    imageView.fitHeightProperty().addListener(i -> redrawGlass());
    imageView.fitWidthProperty().addListener(i -> redrawGlass());

    glassItems.addListener((ListChangeListener.Change<? extends GlassItem> c) -> {
      while (c.next()) {
        if (c.wasAdded()) {
          var items = c.getAddedSubList();
          items.forEach(gi -> glass.getChildren().addAll(gi.getShapes()));
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

  public StackPane getRoot() {
    return root;
  }

  public Pane getGlass() {
    return glass;
  }

  public ImageView getImageView() {
    return imageView;
  }

  public ImageViewExt getImageViewExt() {
    return imageViewExt;
  }




  




  
}
