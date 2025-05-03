/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ficheros;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 * Este metodo gestiona la creacion de un codigo personal para cada medicamento
 * que se almacenará en un archivo para mantener la persistencia
 */
public class gestorCodigoMedicamento {

    private static final String rutaCarpeta = "src/Inventario";
    private static final String rutaArchivo = rutaCarpeta + "/codigoMedicamento";

    /**
     * Este metodo crea una carpeta que gestionará un numero para darle un
     * codigo que nunca se repetirá a cada medicamento
     *
     * @return devuelve el numero del codigo del medicamento
     */
    public static int generarCodMed() {
        File carpeta = new File(rutaCarpeta);
        File archivo = new File(rutaArchivo);
        DataOutputStream salida = null;
        int numero = 1001; //Empezamos con este numero por defecto
        try {
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
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
     * Este metodo es complementario a carpetaCodMed, permite leer el archivo y
     * devolver el numero que hay escrito
     *
     * @param ruta
     * @return
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
