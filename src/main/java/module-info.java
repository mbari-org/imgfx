/*
 * Copyright © 2025 MBARI (brian@mbari.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
