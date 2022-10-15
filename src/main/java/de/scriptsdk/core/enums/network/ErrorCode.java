package de.scriptsdk.core.enums.network;

import de.scriptsdk.core.interfaces.Enumerable;

public enum ErrorCode implements Enumerable {
    PACKET_SIZE_TOO_SMALL(0, "Packet size is not correct!"),
    UNFINISHED_PACKET(1, "Packet has not been finished!"),
    ZERO_METHOD_NUM(2, "Type of packet can´t be 0x00!"),
    UNFINISHED_RECEIVE(3, "Stealth were unable to receive packet properly!"),
    PACKET_PROCESS_ERROR(4, "There has been an error on processing packet!"),
    UNKNOWN_SCRIPT_LANGUAGE(5, "Script language is not supported!"),
    OLD_VERSION(6, "This version is outdated!"),
    METHOD_NOT_FOUND(7, "Type of packet does not exist in Stealth!"),
    METHOD_NOT_FOUND_IN_UO(8, "Type of packet does not exist in UO!");
    private final Integer id;
    private final String text;

    ErrorCode(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
