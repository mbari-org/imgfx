package org.mbari.imgfx;

/**
 * A builder is a type of tool that creates an instance of a given class type in the UI
 *
 * @param <T> The type that
 */
public interface Builder<T> extends Tool {

    Class<T> getBuiltType();

}
