package out;

public class IdentificationWriter {

    private static final IdentificationWriter identificationWriter = new IdentificationWriter();

    private IdentificationWriter() {}

    public static IdentificationWriter getInstance() {
        return identificationWriter;
    }
}
