# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

imgfx is a Java 21 JavaFX library (published to Maven Central, `org.mbari.imgfx:imgfx`) of controls for building image/video annotation apps. It is a named JPMS module (`src/main/java/module-info.java`) — any new package that consumers need must be added to its `exports` list.

## Commands

Use the Maven wrapper:

- Build: `./mvnw clean package`
- Run the demo editor app: `./mvnw clean javafx:run` (main class `org.mbari.imgfx.demos.imageview.editor.App`, configured in `pom.xml`)
- Run another demo: change `mainClass` in the `javafx-maven-plugin` config in `pom.xml`, or launch the class from the IDE. Demos live in `org.mbari.imgfx.demos`.
- There is no `src/test` directory; there are no unit tests or lint config. The `demos` package serves as manual verification.

Source files intentionally carry no license header; a license plugin adds Apache 2 headers separately (see `pom.xml`). Don't add headers by hand.

## Architecture

Core concepts (all under `org.mbari.imgfx`):

- **Data / DataView / Localization** (`roi/`) — three layers for one annotation:
  - `Data` (`CircleData`, `LineData`, `PolygonData`, ...) is the model, in *unscaled image coordinates*.
  - `DataView<A extends Data, B extends Shape>` (`CircleView`, `LineView`, `PolygonView`, ...) is the JavaFX shape bound to a Data. `updateView()`/`updateData()` sync the two; it owns an `Autoscale` and an editing flag.
  - `Localization<C, V>` wraps a DataView with a UUID, label text, visibility, and the `AutoscalePaneController` it is drawn in.
- **Autoscale** (`Autoscale`, `imageview/ImageViewAutoscale`, `mediaview/MediaViewAutoscale`) — tracks the scale between a view's displayed size and the underlying image/media size so Data stays in image coordinates while shapes render at display size as the pane resizes. `AutoscalePaneController` (`ImagePaneController`, `MediaPaneController`) hosts the image/media plus the overlay pane where localizations are drawn.
- **Tool / Builder** — a `Tool` is a mouse-interaction mode; a `Builder<T>` (`CircleBuilder`, `RectangleBuilder`, `LineBuilder`, `MarkerBuilder`, `PolygonBuilder` in `roi/`) creates a given ROI type from user gestures. `ColoredBuilder` adds color support. `BuilderCoordinator` ensures only the current builder is enabled and disables all builders while a DataView is being edited/dragged.
- **EditingDecorator / LineViewEditor** — attach drag/resize handles to a DataView when editing.
- **EventBus** (`etc/rx/`) — a global RxJava 3 `PublishSubject` bus. Events in `etc/rx/events/` (`AddLocalizationEvent`, `RemoveLocalizationEvent`, `EditLocalizationEvent`, `UpdatedLocalizationsEvent`, ...) are how builders, panes, and the app communicate; publish/subscribe there rather than wiring components directly.
- `etc/javafx/` holds geometry helpers (Cohen-Sutherland and Sutherland-Hodgman clipping, `LineEqn`, `MutablePoint`) and small custom controls (`BoundingBox`, `CrossHairs`, `SelectionRectangle`, `FilteredComboBoxDecorator`).
- `demos/` (including `demos/imageview/editor/`) contains runnable sample apps; the editor `App` is a fuller example wiring tool, color, concept, and annotation panes together. These are exported from the module and ship in the jar.

Logging uses `System.Logger` (no external logging dependency).
