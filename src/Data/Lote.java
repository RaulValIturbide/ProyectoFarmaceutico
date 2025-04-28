package Data;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import Exception.MedicamentoInexistenteException;
import Ficheros.gestorCodigoLote;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author baske
 */
public class Lote implements Comparable<Lote>, Serializable {

    private final String rutaLista = "src/Inventario/listaMedicamentos";
    private int codigoMed;
    private int codigoLote;
    private LocalDate fechaCaducidad;
    private int stockAhora;

    /**
     * Metodo constructor que crea lotes en la consola
     *
     * @param codigoMed
     * @param codigoLote
     * @param stockAhora
     * @param fechaCaducidad
     */
    public Lote(int codigoMed, int stockAhora, LocalDate fechaCaducidad) {
        this.codigoMed = codigoMed;
        this.codigoLote = gestorCodigoLote.generarCodLote();
        this.stockAhora = stockAhora;
        this.fechaCaducidad = fechaCaducidad;
    }

    /**
     * Este constructor genera un lote de medicamento a la vez que se asegura de
     * que ese medicamento exista, ya que de no existir lanzar√° una excepcion y
     * cancelar√° la creacion del lote
     *
     * @throws MedicamentoInexistenteException salta cuando no encuentra en la
     * lista de medicamentos uno con el mismo nombre, haciendo imposible crear
     * un lote de un medicamento que no esta en la base de datos todavia
     */
    public Lote() throws MedicamentoInexistenteException {
        String nombre = Menu.addNombre();
        int codigoTemp = retornarCodigoMedicamento(nombre); //variable que almacena de manera temporal el codigo mientras se est√° revisando
        if (codigoTemp == -1) {
            throw new MedicamentoInexistenteException("Este medicamento no existe, cancelando la creaciÛn del lote...");
        } else {
            codigoMed = codigoTemp;
            codigoLote = gestorCodigoLote.generarCodLote();
            stockAhora = Menu.addStockAhora();
            fechaCaducidad = Menu.addFecha("Vamos a introducir la fecha de caducidad","Cual es el dia en el que caduca?","Cual es el mes en el que caduca?", "Cual es el aÒo en el que caduca?","No puede introducir un lote que caduque hoy o que ya lo haya hecho, asegurese de los datos e intentelo de nuevo:\n");
        }
    }

    public int getCodigoMed() {
        return codigoMed;
    }

    public void setCodigoMed(int codigoMed) {
        this.codigoMed = codigoMed;
    }

    public int getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(int codigoLote) {
        this.codigoLote = codigoLote;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public int getStockAhora() {
        return stockAhora;
    }

    public void setStockAhora(int stockAhora) {
        this.stockAhora = stockAhora;
    }

    /**
     * Este metodo busca en la lista de medicamentos el nombre del medicamento
     * buscado por el usuario y devuelve su codigo, de no existir devolver· -1
     *
     * @param nombre el nombre del medicamento buscado por el usuario, ignorando
     * mayusculas/minusculas
     * @return devolver· el numero del codigo del medicamento en cuestion o -1
     * si no existe en la base de datos
     */
    private int retornarCodigoMedicamento(String nombre) {
        ObjectInputStream entrada = null;
        try {
            entrada = new ObjectInputStream(new FileInputStream(rutaLista));
            while (true) {
                Medicamento aux = (Medicamento) entrada.readObject();
                if (aux.getNombre().equalsIgnoreCase(nombre)) {
                    return aux.getCodigo();
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("");//No queremos que diga nada en esta situacion ya que el usuario solo necesita ver que el medicamento no existe
        } catch (EOFException eo) {
            System.out.println("");
        } catch (ClassNotFoundException cn) {
            System.out.println();
        } catch (IOException io) {
            System.out.println("Error fatal en la entrada de retornarCodigoMedicamento");
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    System.out.println("Error al intentar cerrar el flujo de entrada");
                }
            }
        }

        return -1;
    }

    @Override
    public String toString() {
        return "codigo Medicamento: " + codigoMed
                + "\ncodigo lote: " + codigoLote
                + "\nstock ahora: " + stockAhora
                + "\nfechaCaducidad " + fechaCaducidad
                + "\n\n";
    }

    @Override
    public boolean equals(Object ob) {
        Lote aux = (Lote) ob;
        return this.codigoLote == aux.codigoLote;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoLote);
    }

    /**
     * Usaremos el metodo compareTo para que se escriban primero por orden de
     * medicamentos y luego por orden de fecha de caducidad asi podremos echar
     * mano de las medicinas que estan mas proximans a caducar
     *
     * @param lote
     * @return
     */
    @Override
    public int compareTo(Lote lote) {
        if (this.codigoMed > lote.codigoMed) {
            return 1; // Si es mayor, va despu√©s
        } else if (this.codigoMed < lote.codigoMed) {
            return -1; // Si es menor, va antes
        } else {
            return this.fechaCaducidad.compareTo(lote.fechaCaducidad);
        }
    }

}
