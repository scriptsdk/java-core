package de.scriptsdk.core.model.packet;

import de.scriptsdk.core.enums.network.PacketType;
import de.scriptsdk.core.model.io.PacketWriter;

public final class PacketRequest {
    private final PacketType type;
    private final Integer sequence;
    private final byte[] body;

    public PacketRequest(PacketType packetType, Integer sequence, byte[] body) {
        this.type = packetType;
        this.sequence = sequence;
        this.body = body;
    }

    public byte[] generatePacket() {

        PacketWriter argsWriter = new PacketWriter();

        argsWriter.writeWord(type.getId());
        argsWriter.writeWord(sequence);
        argsWriter.writeBytes(body);
        byte[] bytes = argsWriter.getStream();

        PacketWriter packetWriter = new PacketWriter();
        packetWriter.writeInteger(bytes.length);
        packetWriter.writeBytes(bytes);

        return packetWriter.getStream();
    }
}
