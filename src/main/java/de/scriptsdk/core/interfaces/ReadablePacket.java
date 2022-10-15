package de.scriptsdk.core.interfaces;

import de.scriptsdk.core.model.io.PacketReader;

@FunctionalInterface
public interface ReadablePacket<T> {

    T read(PacketReader reader);
}
