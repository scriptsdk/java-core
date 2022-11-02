package de.scriptsdk.core.model.io;

import de.scriptsdk.core.enums.io.DataType;
import de.scriptsdk.core.exceptions.io.PacketWriterException;
import de.scriptsdk.core.interfaces.Serializable;
import de.scriptsdk.core.interfaces.WritablePacket;
import de.scriptsdk.core.model.mapper.DelphiDateTimeMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Crome696
 * @version 1.0
 */
public class PacketWriter {
    final ByteArrayOutputStream stream;

    public PacketWriter() {
        stream = new ByteArrayOutputStream();
    }


    public void writeBoolean(Boolean value) {
        writeInternalByte(((byte) (Objects.equals(value, true) ? 1 : 0)));
    }

    public void writeDateTime(LocalDateTime value) {
        writeInternalDouble(new DelphiDateTimeMapper(value).getValue(), DataType.DATETIME);
    }

    public void writeSmallInteger(Integer value) {
        writeInternalInteger(value, DataType.SMALL_INTEGER);
    }

    public void writeWord(Integer value) {
        writeInternalInteger(value, DataType.WORD);
    }

    public void writeInteger(Integer value) {
        writeInternalInteger(value, DataType.INTEGER);
    }

    public void writeCardinal(Long value) {
        writeInternalLong(value, DataType.CARDINAL);
    }


    public void writeDouble(Double value) {
        writeInternalDouble(value, DataType.DOUBLE);
    }


    public void writeBigInteger(BigInteger value) {
        writeInternalBigInteger(value);
    }

    public void writeString(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_16LE);
        writeInteger(bytes.length);
        writeBytes(bytes);
    }

    private void writeInternalBigInteger(BigInteger value) {
        writeInternalLong(value.longValue(), DataType.ULONG);
    }

    private void writeInternalInteger(Integer value, DataType type) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(value);
        byteBuffer.rewind();

        writeBytes(Arrays.copyOf(byteBuffer.array(), type.getSize()));
    }

    private void writeInternalLong(Long value, DataType type) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putLong(value);
        byteBuffer.rewind();

        writeBytes(Arrays.copyOf(byteBuffer.array(), type.getSize()));
    }

    private void writeInternalDouble(Double value, DataType type) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putDouble(value);
        byteBuffer.rewind();

        writeBytes(Arrays.copyOf(byteBuffer.array(), type.getSize()));
    }

    private void writeInternalByte(Byte value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.put(value);
        byteBuffer.rewind();

        writeBytes(Arrays.copyOf(byteBuffer.array(), DataType.BYTE.getSize()));
    }

    public void writeBytes(byte[] bytes) {
        try {
            stream.write(bytes);
        } catch (IOException e) {
            throw new PacketWriterException(e);
        }
    }


    public byte[] getStream() {
        return stream.toByteArray();
    }


    public <T extends Serializable> void writeObject(T value) {
        value.serialize(this);
    }

    public <T> void writeCustom(WritablePacket<T> writable, T value) {
        writable.write(this, value);
    }

    public <T> void writeList(List<T> values, WritablePacket<T> writable) {
        writeList(values, writable, PacketWriter::writeInteger);
    }

    public <T> void writeList(List<T> values, WritablePacket<T> writable, WritablePacket<Integer> writablePacket) {
        writablePacket.write(this, values.size());
        for (T value : values) {
            writable.write(this, value);
        }
    }

    public PacketWriter addCardinal(long value) {
        writeCardinal(value);
        return this;
    }

    public PacketWriter addWord(Integer value) {
        writeWord(value);
        return this;
    }

    public PacketWriter addString(String value) {
        writeString(value);
        return this;
    }

    public PacketWriter addBoolean(Boolean value) {
        writeBoolean(value);
        return this;
    }

    public PacketWriter addDateTime(LocalDateTime value) {
        writeDateTime(value);
        return this;
    }

    public PacketWriter addSmallInteger(Integer value) {
        writeSmallInteger(value);
        return this;
    }

    public PacketWriter addInteger(Integer value) {
        writeInteger(value);
        return this;
    }

    public PacketWriter addDouble(Double value) {
        writeDouble(value);
        return this;
    }

    public PacketWriter addBigInteger(BigInteger value) {
        writeBigInteger(value);
        return this;
    }

    public PacketWriter addCardinal(Long... values) {
        for (Long value : values) {
            writeCardinal(value);
        }
        return this;
    }

    public PacketWriter addWord(Integer... values) {
        for (Integer value : values) {
            writeWord(value);
        }
        return this;
    }

    public PacketWriter addString(String... values) {
        for (String value : values) {
            writeString(value);
        }
        return this;
    }

    public PacketWriter addBoolean(Boolean... values) {
        for (Boolean value : values) {
            writeBoolean(value);
        }
        return this;
    }

    public PacketWriter addDateTime(LocalDateTime... values) {
        for (LocalDateTime value : values) {
            writeDateTime(value);
        }
        return this;
    }

    public PacketWriter addSmallInteger(Integer... values) {
        for (Integer value : values) {
            writeSmallInteger(value);
        }
        return this;
    }

    public PacketWriter addInteger(Integer... values) {
        for (Integer value : values) {
            writeInteger(value);
        }
        return this;
    }

    public PacketWriter addDouble(Double... values) {
        for (Double value : values) {
            writeDouble(value);
        }
        return this;
    }

    public PacketWriter addBigInteger(BigInteger... values) {
        for (BigInteger value : values) {
            writeBigInteger(value);
        }
        return this;
    }

    public <T extends Serializable> PacketWriter addObject(T value) {
        writeObject(value);
        return this;
    }

    @SafeVarargs
    public final <T extends Serializable> PacketWriter addObject(T... values) {
        for (T value : values) {
            writeObject(value);
        }
        return this;
    }
}
