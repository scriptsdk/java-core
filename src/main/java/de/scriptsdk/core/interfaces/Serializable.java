package de.scriptsdk.core.interfaces;

import de.scriptsdk.core.model.io.PacketWriter;

/**
 * @author Crome696
 * @version 1.0
 */
public interface Serializable {
    void serialize(PacketWriter writer);
}
