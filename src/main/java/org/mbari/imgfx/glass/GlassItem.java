package org.mbari.imgfx.glass;

import java.util.List;
import java.util.UUID;
import org.mbari.imgfx.ImageViewExt;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public interface GlassItem {

  List<Node> getNodes();

  void doLayout(ImageViewExt ext);

  void toFront();

  UUID getUuid();

  void setUuid(UUID uuid);

  void setColor(Color color);

  
}
