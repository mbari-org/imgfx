package org.mbari.imgfx.roi;

public class EditingDecorator {

    public static void decorate(DataView<?, ?> dataView) {
        var decorator = dataView.getImageViewDecorator();
        var editingProperty = dataView.editingProperty();
        // Dont' edit while resizing. It mucks things up
        decorator.getImageView()
                .boundsInParentProperty()
                .addListener((obs, oldv, newv) -> {
                    editingProperty.set(false);
                    dataView.updateView();
                });


        editingProperty.addListener((obs, oldv, newv) -> {
            if (newv) {
                dataView.updateView();
            }
            else {
                dataView.updateData();
            }
        });

    }
}
