module imgfx {
  requires transitive javafx.controls;

  // Required to interact with ScenicView
  requires java.instrument;
  requires java.rmi;

  exports org.mbari.imgfx;
  exports org.mbari.imgfx.controls;
  exports org.mbari.imgfx.demos.app;
  exports org.mbari.imgfx.demos.bb;
  exports org.mbari.imgfx.glass;
}
