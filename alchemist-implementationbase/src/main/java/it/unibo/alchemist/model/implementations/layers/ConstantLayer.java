/*
 * Copyright (C) 2010-2019, Danilo Pianini and contributors listed in the main project's alchemist/build.gradle file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.layers;

import it.unibo.alchemist.model.interfaces.Layer;
import it.unibo.alchemist.model.interfaces.Position;

/**
 * a Layer where the concentration is the same at every point in space.
 *
 * @param <P> position type
 * @param <T> concentration type
 */
public final class ConstantLayer<T, P extends Position<? extends P>> implements Layer<T, P> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final T level;

    /**
     * @param level
     *            the concentration
     */
    public ConstantLayer(final T level) {
        this.level = level;
    }

    @Override
    public T getValue(final P p) {
        return level;
    }

}
