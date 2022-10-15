package de.scriptsdk.core.interfaces;

import de.scriptsdk.core.exceptions.enums.EnumException;

import java.util.Arrays;
import java.util.Objects;

public interface Enumerable {

    static <T extends Enumerable> T valueOf(int value, Class<T> clazz) {
        T[] values = clazz.getEnumConstants();

        return Arrays.stream(values).filter(type -> Objects.equals(type.getId(), value)).
                findFirst().orElseThrow(() -> new EnumException(clazz, value));
    }

    Integer getId();
}