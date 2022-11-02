package de.scriptsdk.core.enums.io;

import de.scriptsdk.core.interfaces.Enumerable;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author Crome696
 * @version 1.0
 */
public enum DataType implements Enumerable {
    SMALL_INTEGER(0, 1, Integer.class),
    BYTE(1, 1, Byte.class),
    WORD(2, 2, Integer.class),
    INTEGER(3, 4, Integer.class),
    CARDINAL(4, 4, Long.class),
    DOUBLE(5, 8, Double.class),
    DATETIME(6, 8, LocalDateTime.class),
    ULONG(7, 8, BigInteger.class),
    STRING(8, 2, String.class),
    BOOLEAN(9, 1, Boolean.class);


    private final int size;
    private final Class<?> converterClass;
    private final Integer id;

    DataType(Integer id, Integer size, Class<?> converterClass) {
        this.size = size;
        this.converterClass = converterClass;
        this.id = id;
    }

    public int getSize() {
        return size;
    }
    
    public Class<?> getConverterClass() {
        return converterClass;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
