
package Ficheros;

import Persona.Medico;
import java.io.EOFException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author baske
 */
public class gestorMedico {

    ArrayList<Medico> listaMedico = new ArrayList<>();
    static String rutaCarpeta = "src/Inventario/Personas";
    static String rutaArchivo = rutaCarpeta + "/listaMedicos";

    public static void escribirMedico(Medico med) {
        File carpeta = new File(rutaCarpeta);
        File archivo = new File(rutaArchivo);
        ObjectOutputStream salida = null;

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        try {
            if (archivo.length() == 0) {
                salida = new ObjectOutputStream(new FileOutputStream(archivo));
                salida.writeObject(med);
            } else {
                salida = new MiObjectOutputStream(new FileOutputStream(archivo, true));
                salida.writeObject(med);
            }
        } catch (IOException ex) {
            System.out.println("ERROR FATAL: en la creacion del medico");
        } finally {
            try {
                salida.flush();
                salida.close();

            } catch (IOException ex) {
                System.out.println("ERROR FATAL: no se pudo cerrar el flujo de salida al escribir un medico");
            }
        }

    }

    public void addMedico(Medico med) {
        listaMedico.add(med);
    }

    public void generarArrayList() {
        ObjectInputStream entrada = null;
        File archivo = new File(rutaArchivo);
        try {
            entrada = new ObjectInputStream(new FileInputStream(archivo));

            while (true) {
                Medico aux = (Medico) entrada.readObject();
                addMedico(aux);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Ruta no encontrada");
        } catch (EOFException eo) {
            System.out.println("");
        } catch (ClassNotFoundException cnf) {
            System.out.println("Error: fin de objetos");
        } catch (IOException io) {
            System.out.println("Error fatal");
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    System.out.println("Error al intentar cerrar la entrada del generador de TreeSet");
                }

            }
        }

    }

    public void mostrarLista() {
        for (Medico m : listaMedico) {
            System.out.println(m.mostrarDatos());
        }
    }

    public static boolean MedicoExisteEnLaBBDD(int idMedico) {
        ObjectInputStream entrada = null;

        try {
            entrada = new ObjectInputStream(new FileInputStream(rutaArchivo));
            while (true) {

                Medico aux = (Medico) entrada.readObject();
                if (aux.getIdMedico() == idMedico) {
                    return true;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("No existe el archivo buscado");
        } catch (EOFException e) {
            System.out.println("");
        } catch (IOException ex) {
            System.out.println("ERROR FATAL: busqueda de medico por id fallada");
        } catch (ClassNotFoundException cn) {
            System.out.println("Clase no encontrada");
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    System.out.println("ERROR FATAL: fallo intentando cerrar el flujo de datos de entrada en la busqueda de medicamento");
                }

            }
        }
        return false;
    }

    /**
     * Este metodo sirve para buscar un medico y si lo encuentra lo devuelve
     *
     * @param idMedico el id del medico por el que buscará
     * @return devolverá el objeto medico si lo encuentra o null si no lo
     * encuentra
     */
    public static Medico encontrarMedico(int idMedico) {
        Medico medicoEncontrado = null;
        ObjectInputStream entrada = null;

        try {
            entrada = new ObjectInputStream(new FileInputStream(rutaArchivo));

            while (true) {
                Medico aux = (Medico) entrada.readObject();
                if (aux.getIdMedico() == idMedico) {
                    medicoEncontrado = aux;
                }

            }

        } catch (EOFException ex) {
            System.out.println("");
        } catch (ClassNotFoundException cnf) {
            System.out.println("Clase no encontrada buscando al medico");
        } catch (IOException io) {
            System.out.println("Error fatal encontrando al medico");
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    System.out.println("Fallo total al intentar cerrar el flujo de datos del medico");
                }
            }
        }
        return medicoEncontrado;
    }

}
