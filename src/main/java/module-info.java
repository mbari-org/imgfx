module imgfx {
  requires transitive javafx.controls;

  // Required to interact with ScenicView
  requires java.desktop;
  requires java.instrument;
  requires java.rmi;
  requires io.reactivex.rxjava3;

  exports org.mbari.imgfx;
  exports org.mbari.imgfx.demos;
  exports org.mbari.imgfx.tools;
  exports org.mbari.imgfx.roi;
  exports org.mbari.imgfx.etc.rx;
  exports org.mbari.imgfx.etc.jfx;
  exports org.mbari.imgfx.etc.rx.events;
  exports org.mbari.imgfx.etc.jfx.controls;
}
