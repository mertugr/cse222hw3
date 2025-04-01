package protocol;

public class UART implements Protocol {
    @Override
    public String getProtocolName() {
        return "UART";
    }

    @Override
    public String read() {
        return getProtocolName() + ": Reading.";
    }

    @Override
    public void write(String data) {
        System.out.println(getProtocolName() + ": Writing \"" + data + "\".");
    }
}