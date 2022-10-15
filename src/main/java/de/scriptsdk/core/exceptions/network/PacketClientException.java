package de.scriptsdk.core.exceptions.network;

import de.scriptsdk.core.exceptions.BaseException;

public class PacketClientException extends BaseException {
    public PacketClientException(Throwable cause) {
        super(cause);
    }

    public PacketClientException(String format, Object... args) {
        super(format, args);
    }
}
