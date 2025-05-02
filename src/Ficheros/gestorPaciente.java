
package Ficheros;


import Persona.Paciente;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author baske
 */
public class gestorPaciente {
    
     ArrayList<Paciente> listaPaciente = new ArrayList<>();
     
    static String rutaCarpeta = "src/Inventario/Personas";
    static String rutaArchivo = rutaCarpeta + "/listaPacientes";

    /**
     * Este metodo permite escribir un paciente en el archivo indicado para la lista de pacientes
     * @param p el paciente que se escribirá
     */
    public static void escribirPaciente(Paciente p) {
        File carpeta = new File(rutaCarpeta);
        File archivo = new File(rutaArchivo);
        
        ObjectOutputStream salida = null;

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        try {
            if (archivo.length() == 0) {
                salida = new ObjectOutputStream(new FileOutputStream(archivo));
                salida.writeObject(p);
            } else {
                salida = new MiObjectOutputStream(new FileOutputStream(archivo, true));
                salida.writeObject(p);
            }
        } catch (IOException ex) {
            System.out.println("ERROR FATAL: en la creacion del paciente");
        } finally {
            try {
                salida.close();

            } catch (IOException ex) {
                System.out.println("ERROR FATAL: no se pudo cerrar el flujo de salida al escribir un paciente");
            }
        }

    }
        
        
    public void addPaciente(Paciente p) {
        listaPaciente.add(p);
    }
        
        /**
         * Este metodo toma los pacientes escritos en el fichero y los escribe en el array list para poder tratarlos
         */
    public void generarArrayList() {
        String ruta = "src/inventario/Personas/listaPacientes";
        ObjectInputStream entrada = null;
        File archivo = new File(ruta);
        try {
            entrada = new ObjectInputStream(new FileInputStream(archivo));

            while (true) {
                Paciente aux = (Paciente) entrada.readObject();
                addPaciente(aux);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Ruta no encontrada");
        } catch (EOFException eo) {
            System.out.println("Lista completa:\n");
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
    
    public void mostrarLista(){
        for(Paciente p : listaPaciente){
            System.out.println(p.mostrarDatos());
        }
    }
    
    /**
     * Este metodo busca el id de un paciente en la lista y lo devuelve si existe
     * @param idPaciente el id del paciente que buscamos
     * @return  devolverá el objeto paciente con el mismo id o null si no existe
     */
    public static Paciente encontrarPaciente(String idPaciente) {
        Paciente pacienteEncontrado = null;
        ObjectInputStream entrada = null;

        try {
            entrada = new ObjectInputStream(new FileInputStream(rutaArchivo));

            while (true) {
                Paciente aux = (Paciente) entrada.readObject();
                if (aux.getDni().equals(idPaciente)) {
                    pacienteEncontrado = aux;
                }

            }

        } catch (EOFException ex) {
            System.out.println("");
        } catch (ClassNotFoundException cnf) {
            System.out.println("Clase no encontrada buscando al paciente");
        } catch (IOException io) {
            System.out.println("Error fatal encontrando al paciente");
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    System.out.println("Fallo total al intentar cerrar el flujo de datos del paciente");
                }
            }
        }
        return pacienteEncontrado;
    }
    
    
    /**
     * Este metodo busca en la "bbdd" y comprueba si un paciente con el id dado por parametro existe o no
     * @param dni el id del paciente
     * @return devolverá true si existe o false si no
     */
    public static boolean pacienteExisteEnLaBBDD(String dni) {

        ObjectInputStream entrada = null;
        boolean encontrado = false;
        try {
            entrada = new ObjectInputStream(new FileInputStream(rutaArchivo));

            while (true) {

                Paciente aux = (Paciente) entrada.readObject();

                if (aux.getDni().equals(dni)) {
                    encontrado = true;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Fichero no encontrado");
        } catch (EOFException eo) {

        } catch (ClassNotFoundException cnf) {
            System.out.println("Clase no encontrada");
        } catch (IOException io) {
            System.out.println("ERROR FATAL con el lector de la busqueda de paciente en la bbdd");
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    System.out.println("Error intentando cerrar el lector de busqueda de paciente");
                }
            }
        }
        return encontrado;
    }
         
}
