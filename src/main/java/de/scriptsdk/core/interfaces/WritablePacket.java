package de.scriptsdk.core.interfaces;

import de.scriptsdk.core.model.io.PacketWriter;

@FunctionalInterface
public interface WritablePacket<T> {
    void write(PacketWriter writer, T value);
}
