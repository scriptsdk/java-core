package de.scriptsdk.core.model.network;

import de.scriptsdk.core.enums.network.ErrorCode;
import de.scriptsdk.core.enums.network.PacketType;
import de.scriptsdk.core.enums.network.ScriptState;
import de.scriptsdk.core.exceptions.network.PacketClientException;
import de.scriptsdk.core.interfaces.Enumerable;
import de.scriptsdk.core.interfaces.PacketReaderAction;
import de.scriptsdk.core.model.io.PacketReader;
import de.scriptsdk.core.model.io.PacketWriter;
import de.scriptsdk.core.model.packet.PacketRequest;
import de.scriptsdk.core.model.packet.PacketResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public final class PacketClient {
    private final Integer port;
    private final String url;
    private final UUID id;
    private final Map<Integer, PacketType> outgoingPacketRegistry;
    private final Map<Integer, PacketResponse> incomingPackets;
    private final AtomicInteger sequence;
    private final SocketClient client;
    private ScriptState state;
    private PacketReaderAction onEventAction;

    public PacketClient(String url, Integer port) {
        this.id = UUID.randomUUID();
        this.url = url;
        this.port = getApiPort(url, port);
        this.state = ScriptState.UNKNOWN;

        this.outgoingPacketRegistry = new ConcurrentHashMap<>();
        this.incomingPackets = new ConcurrentHashMap<>();
        this.sequence = new AtomicInteger();
        this.client = new SocketClient(url, this.port);
        onEventAction = null;
    }

    public PacketReaderAction getOnEventAction() {
        return onEventAction;
    }

    public void setOnEventAction(PacketReaderAction onEventAction) {
        this.onEventAction = onEventAction;
    }

    public String getUrl() {
        return url;
    }

    public UUID getId() {
        return id;
    }

    public ScriptState getState() {
        return state;
    }

    private Integer getApiPort(String url, Integer port) {
        SocketClient tempClient = new SocketClient(url, port);
        try {
            if (tempClient.connect()) {

                log.info("Sending PacketType: Api Port Request");


                PacketWriter writer = new PacketWriter();

                writer.writeWord(4);
                writer.writeSmallInteger(239);
                writer.writeSmallInteger(190);
                writer.writeSmallInteger(173);
                writer.writeSmallInteger(222);


                tempClient.write(writer.getStream());

                while (tempClient.canRead() < 4) {
                    tempClient.sleep(20);
                }

                final int maxLength = 4;

                byte[] bytes = tempClient.read(maxLength);

                if (bytes.length == maxLength) {

                    final int statusCode = 2;

                    PacketReader reader = new PacketReader(bytes);
                    final int returnCode = reader.readWord();

                    if (returnCode == statusCode) {
                        return reader.readWord();
                    }
                    throw new PacketClientException("Incorrect status code because %d were expected and %d has been returned!",
                            statusCode, returnCode);
                }
                throw new PacketClientException("Incorrect status code because %d were expected and %d has been returned!",
                        maxLength, bytes.length);
            }
        } finally {
            if (tempClient.isConnected()) {
                tempClient.disconnect();
            }
        }
        return 0;
    }

    public PacketReader exchange(PacketType type) {
        return exchange(type, new PacketWriter());
    }

    public PacketReader exchange(PacketType type, PacketWriter writer) {

        int nextSequence = internalWrite(type, writer);

        final LocalDateTime now = LocalDateTime.now();
        do {
            synchronizePacketsInReaderQueue();
            if (!isPacketAvailable(nextSequence) &&
                    (LocalDateTime.now().isAfter(now.plusSeconds(60)))) {
                throw new PacketClientException("Error on timeout! Stealth seems to be unable to respond!");
            }
        } while (!isPacketAvailable(nextSequence));

        PacketResponse response = this.getIncomingPacket(nextSequence);
        if (response == null) {
            throw new PacketClientException("Null pointer exception on reading packet response");
        }

        PacketReader reader = new PacketReader(response.getData());

        removeSequenceFromQueue(nextSequence);

        return reader;
    }

    public void send(PacketType type, PacketWriter writer) {
        int nextSequence = internalWrite(type, writer);
        removeSequenceFromQueue(nextSequence);
    }

    public void send(PacketType type) {
        send(type, new PacketWriter());
    }

    private Integer internalWrite(PacketType packetType, PacketWriter writer) {
        this.validateClient();
        Integer nextSequence = this.getNextSequence();

        PacketRequest request = new PacketRequest(packetType, nextSequence, writer.getStream());

        if (!outgoingPacketRegistry.containsKey(nextSequence)) {

            outgoingPacketRegistry.put(nextSequence, packetType);

            log.info(String.format("Sending PacketType: %s", packetType.name()));

            byte[] bytes = request.generatePacket();

            client.write(bytes);

            log.info(String.format("Sending Transaction-ID: %d", nextSequence));

            return nextSequence;
        } else {
            throw new PacketClientException("Sequence %d is already existing on Queue!", nextSequence);
        }
    }

    public Boolean connect() {

        if ((!state.equals(ScriptState.UNKNOWN)) && (!client.isConnected())) {
            state = ScriptState.UNKNOWN;
        }

        if (!client.isConnected()) {
            boolean newState = client.connect();
            if (newState) {
                state = ScriptState.STARTED;
            }
            return newState;
        }
        return true;
    }

    private void validateClient() {
        if (this.port == 0) {
            throw new PacketClientException("Client cannot communicate to port 0!");
        }

        if (this.client == null) {
            throw new PacketClientException("Client is not fully initialized!");
        }

        if (!this.client.isConnected()) {
            throw new PacketClientException("Client is not connected!");
        }

        if (this.state != ScriptState.STARTED) {
            switch (state) {
                case UNKNOWN -> throw new PacketClientException(
                        "Client cannot perform action since Stealth is stopped!");
                case PAUSED -> throw new PacketClientException(
                        "Client cannot perform action since Stealth is paused!");
                default -> throw new PacketClientException("Unexpected state: %s", state);
            }
        }
    }

    private void synchronizePacketsInReaderQueue() {
        while (client.canRead() > 0) {

            log.info("Reading size of data");

            int size = new PacketReader(client.read(4)).readInteger();

            while (client.canRead() < size) {
                sleep(10);
            }

            log.info(String.format("%d bytes are in queue", size));

            log.info("Reading packet");

            byte[] packet = client.read(size);

            PacketReader reader = new PacketReader(packet);
            PacketType type = Enumerable.valueOf(reader.readWord(), PacketType.class);

            switch (type) {
                case RETURN_VALUE -> {
                    int nextSequence = reader.readWord();

                    log.info(String.format("Packet response type is %s", type.name()));
                    PacketResponse response = new PacketResponse(new PacketReader(reader.readBytes()), size, type, nextSequence);
                    handleResponse(response);
                }
                case SCRIPT_DLL_TERMINATE -> handleTermination();
                case PAUSE_RESUME_SCRIPT -> handleStateChange();
                case EXEC_EVENT_PROC -> handleEvent(new PacketReader(reader.readBytes()));
                case ERROR_REPORT -> handleError();
                default -> {
                    int nextSequence = reader.readWord();
                    throw new PacketClientException(String.format("Unexpected type of transaction %d", nextSequence));
                }
            }
        }
    }

    private void handleError() {
        state = ScriptState.UNKNOWN;

        PacketReader reader = new PacketReader(client.read(4));

        ErrorCode code = Enumerable.valueOf(reader.readWord(), ErrorCode.class);

        PacketType type = Enumerable.valueOf(reader.readWord(), PacketType.class);

        throw new PacketClientException("Error \" %s \" on PacketType %s with value %d", code.getText(), type.name(), type.getId());
    }

    private void handleEvent(PacketReader reader) {
        if (onEventAction != null) {
            onEventAction.execute(reader);
        }
    }

    private void handleStateChange() {
        switch (state) {
            case STARTED -> state = ScriptState.PAUSED;
            case PAUSED -> state = ScriptState.STARTED;
            default -> throw new PacketClientException("Script has already been terminated!");
        }
    }

    private void handleTermination() {
        state = ScriptState.UNKNOWN;
        client.disconnect();

        throw new PacketClientException("Script has been terminated!");
    }

    private void handleResponse(PacketResponse response) {
        if (!outgoingPacketRegistry.containsKey(response.getSequence())) {
            throw new PacketClientException("Incoming packet doesnt match api design and seems to be invalid!");
        }
        log.info(String.format("Transaction-ID: %d", response.getSequence()));
        incomingPackets.put(response.getSequence(), response);
    }

    private Integer getNextSequence() {
        int nextValue;
        do {
            nextValue = sequence.incrementAndGet();
            if (nextValue == Integer.MAX_VALUE) {
                nextValue = sequence.getAndSet(0);
            }
        } while ((outgoingPacketRegistry.containsKey(nextValue)) || (incomingPackets.containsKey(nextValue)));

        return nextValue;
    }

    private PacketResponse getIncomingPacket(Integer sequence) {

        if (this.isPacketAvailable(sequence)) {
            return incomingPackets.get(sequence);
        }
        return null;
    }

    private boolean isPacketAvailable(Integer sequence) {
        return incomingPackets.containsKey(sequence);
    }

    private void removeSequenceFromQueue(Integer sequence) {
        outgoingPacketRegistry.entrySet().removeIf(entry ->
                Objects.equals(entry.getKey(), sequence));

        incomingPackets.entrySet().removeIf(entry ->
                Objects.equals(entry.getKey(), sequence));
    }

    public void sleep(Integer delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PacketClientException(e);
        }
    }
}
