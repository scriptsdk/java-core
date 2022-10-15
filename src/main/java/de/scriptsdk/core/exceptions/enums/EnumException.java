package de.scriptsdk.core.exceptions.enums;

import de.scriptsdk.core.exceptions.BaseException;
import de.scriptsdk.core.interfaces.Enumerable;

public class EnumException extends BaseException {
    public <T extends Enumerable> EnumException(Class<T> clazz, Integer value) {
        super(String.format("Unable to create instance of %s with value %s", clazz.getName(), value));
    }
}
