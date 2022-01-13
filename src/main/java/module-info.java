module imgfx {
  requires transitive javafx.controls;

  // Required to interact with ScenicView
  requires java.instrument;
  requires java.rmi;

  exports org.mbari.imgfx;
  exports org.mbari.imgfx.demos;
  exports org.mbari.imgfx.data;
  exports org.mbari.imgfx.controls;
  exports org.mbari.imgfx.old.app;
  exports org.mbari.imgfx.old.bb;
  exports org.mbari.imgfx.old.glass;
  exports org.mbari.imgfx.old;
}
