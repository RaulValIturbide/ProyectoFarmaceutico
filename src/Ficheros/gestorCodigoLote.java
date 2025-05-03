/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ficheros;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author baske
 */
public class gestorCodigoLote {

    private static final String rutaArchivo = "src/inventario/codigoLotes";

    /**
     * Este metodo genera un archivo cuyo contenido es un int como 1001 y va
     * aumentando de 1 en 1 cada vez qeu se usa para garantizar que cada
     * medicamento posee un codigo independiente
     *
     * @return
     */
    public static int generarCodLote() {
        File archivo = new File(rutaArchivo);
        DataOutputStream salida = null;
        int numero = 1001; //Empezamos con este numero por defecto
        try {
            if (!archivo.exists()) {
                salida = new DataOutputStream(new FileOutputStream(archivo));
                numero = 1001;
                salida.writeInt(numero);

            } else {
                numero = lectorCodMed(archivo) + 1;
                salida = new DataOutputStream(new FileOutputStream(archivo));
                salida.writeInt(numero);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Carpeta no encontrada");
        } catch (IOException e) {
            System.out.println("Fallo del escritorio");
        } finally {
            if (salida != null) {
                try {
                    salida.flush();
                    salida.close();
                } catch (IOException ex) {
                    System.out.println("Fallo al intentar cerrar el flujo de salida");
                }

            }
        }

        return numero;
    }

    /**
     * Este metodo privado es usado por el generador para saber cual es el
     * anterior numero y poder darselo al lote
     *
     * @param ruta la ruta del archivo donde se encuentra el contador
     * @return devolverá el numero que toque en el contador o -1 si el nombre
     * del medicamento no existe y por lo tanto no se puede hacer un lote sobre
     * él
     */
    private static int lectorCodMed(File ruta) {
        int nuevoNumero = -1;
        DataInputStream entrada = null;
        try {
            entrada = new DataInputStream(new FileInputStream(ruta));

            nuevoNumero = entrada.readInt();

        } catch (FileNotFoundException ex) {
            System.out.println("Carpeta no encontrada");
        } catch (IOException e) {
            System.out.println("Error en la entrada de flujo del codigo de carpeta");
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    System.out.println("Fallo al cerrar el flujo de entrada del lector");
                }
            }
        }
        return nuevoNumero;
    }

}
