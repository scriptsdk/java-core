package de.scriptsdk.core.interfaces;

import de.scriptsdk.core.model.io.PacketReader;

@FunctionalInterface
public interface PacketReaderAction {
    void execute(PacketReader reader);
}
