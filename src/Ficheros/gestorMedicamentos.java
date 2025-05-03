package Ficheros;

import Data.Medicamento;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Esta clase tiene como objetivo gestionar los medicamentos, es decir, escribir
 * medicamentos en el archivo y leerlos de √©l
 */
public class gestorMedicamentos implements Serializable {

    private TreeSet<Medicamento> lista = new TreeSet<>();


    private static final String rutaCarpeta = "src/Inventario";
    private static final String rutaArchivo = rutaCarpeta + "/listaMedicamentos";

    /**
     * Este metodo sirve para crear un medicamento en el archivo
     * "listaMedicamentos"
     *
     * @param medicamento el medicamento que deseemos escribir
     */
    public static void escribirMedicamento(Medicamento medicamento) {
        File carpeta = new File(rutaCarpeta);
        File archivo = new File(rutaArchivo);
        ObjectOutputStream salida = null;
        try {
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            if (archivo.length() == 0) {
                salida = new ObjectOutputStream(new FileOutputStream(archivo));
                salida.writeObject(medicamento);
            } else {
                salida = new MiObjectOutputStream(new FileOutputStream(archivo, true));
                salida.writeObject(medicamento);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Ruta no encontrada");
        } catch (IOException e) {
            System.out.println("ERROR FATAL");
        } finally {
            if (salida != null) {
                try {
                    salida.flush();
                    salida.close();
                } catch (IOException ex) {
                    System.out.println("Error al cerrar el flujo de saldia");
                }
            }
        }
    }

    /**
     * Este metodo lee el archivo de los medicamentos y devolver√° true si se
     * encuentra o false si no lo hace
     *
     * @param nombre el nombre del medicamento que estamos buscando
     * @return devolver√° true si el nombre del medicamento ya se encuentra en
     * la bbdd o false si no lo hace
     */
    public static boolean MedicamentoExisteEnLaBBDD(String nombre) {
        ObjectInputStream entrada = null;

        try {
            entrada = new ObjectInputStream(new FileInputStream(rutaArchivo));
            while (true) {

                Medicamento aux = (Medicamento) entrada.readObject();
                if (aux.getNombre().equalsIgnoreCase(nombre)) {
                    return true;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("No existe el archivo buscado");
        } catch (EOFException e) {
            System.out.println("");
        } catch (IOException ex) {
            System.out.println("ERROR FATAL: busqueda de medicamento por nombre");
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
     * Este metodo servir√° para a√±adir los objetos medicamento que esten en el
     * archivo al treeSet
     *
     * @param med
     */
    private void addMedicamento(Medicamento med) {
        lista.add(med);
    }

    /**
     * Este metodo introducir√° todos los objetos del archivo en la lista
     * treeSet para poder modificarlos
     */
    public void generarTreeSet() {
        ObjectInputStream entrada = null;
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Pruebe a introducir un medicamento en la base de datos e intentelo de nuevo");
        } else {
            try {
                entrada = new ObjectInputStream(new FileInputStream(archivo));

                while (true) {
                    Medicamento aux = (Medicamento) entrada.readObject();
                    addMedicamento(aux);
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
    }

    /**
     * Metodo que nos muestra todos los objetos que se encuentran en el archivo
     */
    public void mostrarLista() {
        for (Medicamento m : lista) {
            System.out.println(m.toString());
        }
    }

    public TreeSet<Medicamento> getLista() {
        return lista;
    }

    public void setLista(TreeSet<Medicamento> lista) {
        this.lista = lista;
    }

    /**
     * En este metodo el ususario Farmaceutico introducira el nombre o el codigo
     * de un medicamento, usaremos un iterador para que cuando el medicamento
     * buscado sea encontrado deje de recorrer la lista haciendolo m·s eficiente
     * sin necesidad de recorrerla entera, convertiremos el codigo a String para
     * compararlo con el dato introducido por el usuario y sacaremos la info del
     * medicamento si se encuentra en la Base de datos
     *
     * @param usuario el nombre o codigo del medicamento que se esta buscando
     * @return devuelve el medicamento si existe o null si no lo hace
     */
    public Medicamento buscarMedicamentoIterador(String usuario) {
        boolean encontrado = false;
        Medicamento buscado = null;
        generarTreeSet();
        Iterator<Medicamento> iterador = lista.iterator();
        do {
            Medicamento aux = iterador.next();
            String nombreMed = aux.getNombre();
            String codigoMed = String.valueOf(aux.getCodigo());

            if (nombreMed.equalsIgnoreCase(usuario) || codigoMed.equalsIgnoreCase(usuario)) {
                buscado = aux;
                encontrado = true;
            }
        } while (!encontrado && iterador.hasNext());
        return buscado;
    }
    
    /**
     * Este metodo pretende buscar en la lista de medicamentos y comparar el stock minimo de cada medicamento con el 
     * metodo complemetario del gestor de lotes "stockTotal" para cada medicamento, si el stockMinimo es superior al 
     * stock total entonces se lanzar· una alerta en el sistema que informar· de los medicamentos que se encuentran 
     * en infra Stock
     */
    public void alertaStockMinimo(){
        gestorLotes lotesGS = new gestorLotes(); //Primero necesitamos el gestor de lotes para calcular el stocktotal de cada medicamento
        generarTreeSet(); //Lanzamos este metodo complementario para crear el treeset 
        
        for(Medicamento m: lista){
            int stockActual = lotesGS.stockTotal(m.getCodigo());
            if(m.getStockMinimo() >= stockActual ){
                System.out.println("°ALERTA DE STOCK MINIMO!");
                System.out.println("El medicamento: " + m.getNombre() +" con codigo " + m.getCodigo() + " se encuentra con un stock por debajo del mÌnimo.");
                System.out.println("\tStock actual del medicamento: " + stockActual);
                System.out.println("\tStock minimo recomendable: " + m.getStockMinimo()+"\n");
            }
        }
    }
    
}
