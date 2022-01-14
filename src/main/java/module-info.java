module imgfx {
  requires transitive javafx.controls;

  // Required to interact with ScenicView
  requires java.instrument;
  requires java.rmi;
  requires io.reactivex.rxjava3;

  exports org.mbari.imgfx;
  exports org.mbari.imgfx.demos;
  exports org.mbari.imgfx.controls;
  exports org.mbari.imgfx.old.app;
  exports org.mbari.imgfx.old.bb;
  exports org.mbari.imgfx.old.glass;
  exports org.mbari.imgfx.old;
  exports org.mbari.imgfx.roi;
  exports org.mbari.imgfx.ext.rx;
  exports org.mbari.imgfx.ext.jfx;
}
