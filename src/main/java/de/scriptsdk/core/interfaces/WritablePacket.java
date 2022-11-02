package de.scriptsdk.core.interfaces;

import de.scriptsdk.core.model.io.PacketWriter;

/**
 * @author Crome696
 * @version 1.0
 */
@FunctionalInterface
public interface WritablePacket<T> {
    void write(PacketWriter writer, T value);
}
