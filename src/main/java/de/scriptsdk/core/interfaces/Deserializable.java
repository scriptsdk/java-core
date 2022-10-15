package de.scriptsdk.core.interfaces;

import de.scriptsdk.core.model.io.PacketReader;

public interface Deserializable {
    void deserialize(PacketReader reader);
}
