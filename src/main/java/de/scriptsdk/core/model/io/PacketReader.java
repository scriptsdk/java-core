package de.scriptsdk.core.model.io;


import de.scriptsdk.core.enums.io.DataType;
import de.scriptsdk.core.exceptions.io.PacketReaderException;
import de.scriptsdk.core.interfaces.Deserializable;
import de.scriptsdk.core.interfaces.ReadablePacket;
import de.scriptsdk.core.model.generic.BaseList;
import de.scriptsdk.core.model.mapper.DelphiDateTimeMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Crome696
 * @version 1.0
 */
public class PacketReader {
    private final ByteArrayInputStream stream;

    public PacketReader(byte[] bytes) {
        this.stream = new ByteArrayInputStream(bytes);
    }


    public <T extends Deserializable> T readObject(Class<T> clazz) {
        try {
            T value = clazz.getDeclaredConstructor().newInstance();
            value.deserialize(this);
            return value;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new PacketReaderException(e);
        }
    }

    public Double readDouble() {
        return readInternalDouble(DataType.DOUBLE);
    }

    public LocalDateTime readDateTime() {
        return new DelphiDateTimeMapper(readInternalDouble(DataType.DATETIME)).getDateTime();
    }

    public Long readCardinal() {
        return readInternalLong(DataType.CARDINAL);
    }

    public Integer readSmallInteger() {
        return readInternalInteger(DataType.SMALL_INTEGER);

    }

    public Integer readWord() {
        return readInternalInteger(DataType.WORD);

    }

    public Integer readInteger() {
        return readInternalInteger(DataType.INTEGER);
    }

    public BigInteger readBigInteger() {
        return BigInteger.valueOf(readInternalLong(DataType.ULONG));
    }

    private Long readInternalLong(DataType type) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.put(readBytes(type.getSize()));
        byteBuffer.rewind();

        return byteBuffer.getLong();
    }

    private Integer readInternalInteger(DataType type) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.put(readBytes(type.getSize()));
        byteBuffer.rewind();

        return byteBuffer.getInt();
    }

    private Double readInternalDouble(DataType type) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.put(readBytes(type.getSize()));
        byteBuffer.rewind();

        return byteBuffer.getDouble();
    }

    public Boolean readBoolean() {
        return Objects.equals(readSmallInteger(), 1);
    }

    public byte[] readBytes(int size) {
        try {
            return stream.readNBytes(size);
        } catch (IOException e) {
            throw new PacketReaderException(e);
        }
    }

    public byte[] readBytes() {
        return readBytes(stream.available());
    }

    public String readString() {
        return new String(readBytes(readInteger()), StandardCharsets.UTF_16LE);
    }

    public <T> BaseList<T> readList(ReadablePacket<T> readable, int size) {
        BaseList<T> returnable = new BaseList<>();
        for (int index = 0; index < size; index++) {
            returnable.add(readCustom(readable));
        }

        return returnable;
    }

    public <T> BaseList<T> readList(ReadablePacket<T> readable) {
        int size = this.readInteger();
        return readList(readable, size);
    }

    public <T> T readCustom(ReadablePacket<T> readable) {
        return readable.read(this);
    }


    public Integer getSize() {
        return stream.available();
    }
}
