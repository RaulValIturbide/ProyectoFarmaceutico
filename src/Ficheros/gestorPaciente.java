
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
         
}
