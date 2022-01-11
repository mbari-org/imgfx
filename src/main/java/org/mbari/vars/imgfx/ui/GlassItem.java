package org.mbari.vars.imgfx.ui;

import java.util.List;
import javafx.scene.Node;

public interface GlassItem {

  List<Node> getShapes();

  void doLayout(ImageViewExt ext);
  
}
