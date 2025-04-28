package Ficheros;

import Data.Lote;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

public class gestorLotes {

    private TreeSet<Lote> lista = new TreeSet<>();

    private static final String rutaCarpeta = "src/Inventario";
    private static final String rutaArchivo = rutaCarpeta + "/listaLotes";

    public static void escribirLote(Lote lote) {

        File archivo = new File(rutaArchivo);
        ObjectOutputStream salida = null;
        try {
            if (archivo.length() == 0) {

                salida = new ObjectOutputStream(new FileOutputStream(rutaArchivo));

                salida.writeObject(lote);
            } else {

                salida = new MiObjectOutputStream(new FileOutputStream(rutaArchivo, true));

                salida.writeObject(lote);
            }

        } catch (IOException ex) {
            System.out.println("ERROR FATAL con el escritor de lotes");
            ex.printStackTrace();
        } finally {
            if (salida != null) {
                try {
                    salida.close();
                } catch (IOException ex) {
                    System.out.println("Fallo al intentar cerrar el flujo de salida del escritor de lotes");
                }
            }
        }

    }

    private void addLote(Lote lote) {
        lista.add(lote);

    }

    /**
     * Este metodo toma los objetos lotes creados en el archivol "listaLotes" y
     * los vuelca en el treeset para poder modificarlos
     */
    public void generarTreeSet() {
        String ruta = "src/Inventario/listaLotes";
        ObjectInputStream entrada = null;
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            System.out.println("Pruebe a introducir un medicamento en la base de datos e intentelo de nuevo");
        } else {
            try {
                entrada = new ObjectInputStream(new FileInputStream(archivo));
                while (true) {
                    Lote aux = (Lote) entrada.readObject();
                    addLote(aux);
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
    }

    /**
     * Metodo que nos muestra todos los objetos que se encuentran en el archivo
     */
    public void mostrarLista() {
        for (Lote l : lista) {
            System.out.println(l.toString());
        }
    }

    public TreeSet<Lote> getLista() {
        return lista;
    }

    public void setLista(TreeSet<Lote> lista) {
        this.lista = lista;
    }

    /**
     * Este metodo nos permite recorrer el treeset que se crea a partir de la
     * lectura de los lotes del archivo y comprobar la fecha de hoy en el
     * sistema con la fecha de caducidad de cada lote, avisandonos si en algun
     * lote quedan menos de 30 dias para su caducidad
     */
    public void generarAlertaManual() {
        LocalDate hoy = LocalDate.now();
        generarTreeSet();

        for (Lote l : lista) {
            if (l.getFechaCaducidad().isAfter(hoy) && hoy.until(l.getFechaCaducidad(), ChronoUnit.DAYS) < 30) {
                System.out.println("Peligro de caducar en menos de 30 dias para el lote: " + l.getCodigoLote() + "\n" + l.toString());
            }
        }

    }

}
