package de.scriptsdk.core.interfaces;

import de.scriptsdk.core.model.io.PacketReader;

/**
 * @author Crome696
 * @version 1.0
 */
@FunctionalInterface
public interface PacketReaderAction {
    void execute(PacketReader reader);
}
