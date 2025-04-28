package Ficheros;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MiObjectOutputStream extends ObjectOutputStream {
    
    /**
     * Constructor que usa un OutputStream.
     */
    public MiObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    /**
     * Sobrescribe writeHeader para evitar escribir la cabecera en archivos ya existentes.
     */
    @Override
    protected void writeStreamHeader() throws IOException {
        // Se deja vacío para evitar que ObjectOutputStream escriba una nueva cabecera
    }
}

