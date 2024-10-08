package entrypoint;

import out.IdentificationWriter;

public final class Identification {

    private static final Identification identification = new Identification();
    private final IdentificationWriter writer = IdentificationWriter.getInstance();

    private Identification() {}

    public static Identification getInstance() {
        return identification;
    }

    public void start() {

    }
}
