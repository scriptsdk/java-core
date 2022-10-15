package de.scriptsdk.core.model.packet;


import de.scriptsdk.core.enums.network.PacketType;
import de.scriptsdk.core.model.io.PacketReader;

public final class PacketResponse {
    private final Integer totalSize;
    private final Integer sequence;
    private final PacketType type;
    private final byte[] data;
    private final Integer contentSize;

    public PacketResponse(PacketReader reader, Integer totalSize, PacketType type,
                          int sequence) {
        this.totalSize = totalSize;
        this.type = type;
        this.sequence = sequence;
        this.contentSize = reader.getSize();
        this.data = reader.readBytes(this.getContentSize());
    }

    public Integer getSequence() {
        return sequence;
    }

    public PacketType getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public Integer getContentSize() {
        return contentSize;
    }
}
