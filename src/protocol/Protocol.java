package protocol;

public interface Protocol {
    String getProtocolName();

    String read();

    void write(String data);
}