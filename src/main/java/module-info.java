module org.mbari.imgfx {
  requires transitive javafx.controls;
  requires transitive javafx.media;

  // Required to interact with ScenicView
  requires java.desktop;
  requires java.instrument;
  requires java.rmi;
  requires java.prefs;

  // 3rd party module
  requires io.reactivex.rxjava3;
  requires transitive org.kordamp.ikonli.material2;
  requires transitive org.kordamp.ikonli.bootstrapicons;
  requires org.kordamp.ikonli.core;
  requires org.kordamp.ikonli.javafx;


  exports org.mbari.imgfx;
  exports org.mbari.imgfx.demos;
  exports org.mbari.imgfx.roi;
  exports org.mbari.imgfx.etc.rx;
  exports org.mbari.imgfx.etc.javafx;
  exports org.mbari.imgfx.etc.rx.events;
  exports org.mbari.imgfx.etc.javafx.controls;
  exports org.mbari.imgfx.mediaview;
  exports org.mbari.imgfx.imageview;
  exports org.mbari.imgfx.util;
  exports org.mbari.imgfx.demos.imageview.editor;
}
