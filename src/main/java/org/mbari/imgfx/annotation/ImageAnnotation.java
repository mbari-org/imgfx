package org.mbari.imgfx.annotation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.mbari.imgfx.roi.Data;

import java.util.UUID;

public class ImageAnnotation<T extends Data> {

    private UUID uuid;
    private UUID imageUuid;
    private final T data;

    private final StringProperty label = new SimpleStringProperty();

    public ImageAnnotation(T data, UUID imageUuid) {
        this(data, imageUuid, UUID.randomUUID());
    }

    public ImageAnnotation(T data, UUID imageUuid, UUID uuid) {
        this.data = data;
        this.imageUuid = imageUuid;
        this.uuid = uuid;
    }

    public ImageAnnotation(T data, UUID imageUuid, UUID uuid, String label) {
        this(data, uuid);
        this.label.set(label);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getImageUuid() {
        return imageUuid;
    }

    public void setImageUuid(UUID imageUuid) {
        this.imageUuid = imageUuid;
    }

    public T getData() {
        return data;
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }
}
