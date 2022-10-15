package de.scriptsdk.core.exceptions.io;

import de.scriptsdk.core.exceptions.BaseException;

public class PacketReaderException extends BaseException {
    public PacketReaderException(Throwable cause) {
        super(cause);
    }

    public PacketReaderException(String format, Object... args) {
        super(String.format(format, args));
    }

}
