package de.scriptsdk.core.interfaces;

import de.scriptsdk.core.model.io.PacketWriter;

public interface Serializable {
    void serialize(PacketWriter writer);
}
