package Ficheros;

import Data.Lote;
import Data.Medicamento;
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
import java.util.Iterator;
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
     * Este metodo pretende reescribir el treeset de lotes en el archivo con los
     * nuevos datos dados al restar los stocks en el metodo de restar stocks, es
     * decir, se trata de un metodo complementario a este último, simplemente
     * toma la lista que teniamos creada de treeset al haberla leido del
     * archivo, borra el archivo para no generar problemas con las cabeceras y
     * vuelve a escribir la nueva lista
     *
     * @param lista el treeSet que se escribirá en el nuevo archivo
     */
    private void escribirTreeSet(TreeSet<Lote> lista) {
        if (lista.isEmpty()) {
            System.out.println("La lista esta vacia");
        }
        ObjectOutputStream salida = null;
        try {
            salida = new ObjectOutputStream(new FileOutputStream(rutaArchivo));
            for (Lote l : lista) {
                salida.writeObject(l);

            }
            salida.flush();
        } catch (IOException ex) {
            System.out.println("ERROR FATAL en la escritura del treeset tras el resto de stock");
        } finally {
            if (salida != null) {
                try {
                    salida.close();
                } catch (IOException ex) {
                    System.out.println("Error fatal al intentar cerrar el flujo de datos de salida de la escritura del treeset al restar el stock");
                }
            }
        }
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
     * Este metodo pide por parametro el codigo del medicamento y saca de la
     * base de datos la cantidad de stock total que hay en estos momentos
     *
     * @param nombre el codigo del medicamento
     * @return devuelve el total de stock de un medicamento
     */
    public int stockTotal(int codigo) {
        int stockTotal = 0;
        ObjectInputStream entrada = null;

        try {
            entrada = new ObjectInputStream(new FileInputStream(rutaArchivo));

            while (true) {
                Lote aux = (Lote) entrada.readObject();

                if (aux.getCodigoMed() == codigo) {
                    stockTotal += aux.getStockAhora();
                }
            }
        } catch (FileNotFoundException ex) {

        } catch (EOFException eo) {
            System.out.println("");
        } catch (ClassNotFoundException cnf) {
            System.out.println("Clase no encontrada");
        } catch (IOException io) {
            System.out.println("ERROR FATAL en la generacion del stock total del medicamento con codigo: " + codigo);
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    System.out.println("Error fatal al intentar cerrar el flujo de datos de entrada al contar el stock total");
                }
            }
        }
        return stockTotal;
    }
    
    /**
     * Metodo del demonio que sirve para, tomando un medicamento de referencia, buscar su lotes y restar la cantidad que se ha dado por la prescripción
     * ya que el treeSet está ordenado por caducidad se eliminarán primero los lotes que más pronto esten por caducar respetando el FIFO
     * y se usará un metodo complementario para reescribir el treeset en el archivo
     * @param m el medicamento que buscamos al que restarle los lotes
     * @param cantidad  la cantidad que deseamos restar
     */
    public void restarStock(Medicamento m, int cantidad) {
        generarTreeSet();//Genera la lista para poder interactuar con ella
        Iterator<Lote> iterador = lista.iterator();

        int cantidadRestante = cantidad;
        

        while (iterador.hasNext() && cantidadRestante > 0) {
            Lote lote = iterador.next();

            if (lote.getCodigoMed() == m.getCodigo()) {
                int stockDisponible = lote.getStockAhora();
                int cantidadARestar = Math.min(stockDisponible, cantidadRestante);

                if (cantidadARestar > 0) {
                    lote.setStockAhora(stockDisponible - cantidadARestar);
                    cantidadRestante -= cantidadARestar;
                   
                }

                if (lote.getStockAhora() == 0) {
                    iterador.remove(); // Solo eliminamos si el stock REALMENTE llegó a 0
                }
            }
        }
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
