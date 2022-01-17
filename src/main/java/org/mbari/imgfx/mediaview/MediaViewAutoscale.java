package org.mbari.imgfx.mediaview;


import javafx.scene.media.MediaView;
import org.mbari.imgfx.Autoscale;

public class MediaViewAutoscale extends Autoscale<MediaView> {

    public MediaViewAutoscale(MediaView view) {
        super(view);
        view.mediaPlayerProperty().addListener(i -> recomputeScale());
        view.fitHeightProperty().addListener(i -> recomputeScale());
        view.fitWidthProperty().addListener(i -> recomputeScale());
        recomputeScale();
    }

    @Override
    public Double getUnscaledWidth() {
        return (double) view.getMediaPlayer().getMedia().getWidth();
    }

    @Override
    public Double getUnscaledHeight() {
        return (double) view.getMediaPlayer().getMedia().getHeight();
    }
}