package de.scriptsdk.core.model.network;

import de.scriptsdk.core.exceptions.network.SocketClientException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HexFormat;
import java.util.Objects;

@Slf4j
public final class SocketClient {
    private final int port;
    private final String url;
    private Socket socket;

    public SocketClient(String url, Integer port) {
        this.port = port;
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public String getUrl() {
        return url;
    }

    public void sleep(Integer delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SocketClientException(e);
        }
    }

    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected();
    }

    public boolean connect() {
        try {
            if (this.socket == null) {
                socket = new Socket(this.getUrl(), this.getPort());
                return true;
            }

            if (!this.socket.isConnected()) {
                this.socket.connect(new InetSocketAddress(this.getUrl(), this.getPort()));

                return this.isConnected();
            }
            return false;
        } catch (IOException e) {
            throw new SocketClientException(e);
        }
    }

    public Boolean write(byte[] bytes) {
        try {
            if (Objects.equals(isConnected(), false)) {
                return false;
            }
            DataOutputStream output = new DataOutputStream(this.socket.getOutputStream());

            logPacket("Sending %s", bytes);

            output.write(bytes, 0, bytes.length);

            output.flush();
            return true;
        } catch (IOException e) {
            throw new SocketClientException(e);
        }
    }

    public byte[] read(int length) {
        try {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[length];

            if (canRead() >= length) {
                DataInputStream input = new DataInputStream(this.socket.getInputStream());

                arrayOutputStream.write(buffer, 0, input.read(buffer));
                byte[] bytes = arrayOutputStream.toByteArray();

                logPacket("Receiving %s", bytes);

                return bytes;
            }

            return buffer;
        } catch (IOException e) {
            throw new SocketClientException(e);
        }
    }

    public boolean disconnect() {
        try {
            if ((this.socket != null) && (this.socket.isConnected())) {
                this.socket.close();

                socket = null;
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new SocketClientException(e);
        }
    }

    public int canRead() {
        try {
            if (this.isConnected()) {

                DataInputStream input = new DataInputStream(this.socket.getInputStream());
                return input.available();
            }
            return 0;
        } catch (IOException e) {
            throw new SocketClientException(e);
        }
    }

    private void logPacket(String format, byte[] bytes) {
        log.info(String.format(format, HexFormat.ofDelimiter(", ").withPrefix("0x").formatHex(bytes)));
    }
}
