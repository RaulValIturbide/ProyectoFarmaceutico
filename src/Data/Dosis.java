package Data;

import Ficheros.gestorMedicamentos;
import java.time.LocalDate;
import java.io.Serializable;

public class Dosis implements Serializable {

    private String nombre;//Nombre del medicamento
    private int idMed;
    private Medicamento medicamento;
    private int cantidad;//cantidad de capsulas
    private LocalDate periodo;//El tiempo que tiene que tomarse la medicacion
    private boolean cronico;//si es cronico o no
    gestorMedicamentos gs = new gestorMedicamentos();

    public Dosis() {
        boolean correcto = false;
        do {
            medicamento = gs.buscarMedicamentoIterador();
            if ( medicamento == null || !gestorMedicamentos.MedicamentoExisteEnLaBBDD(medicamento.getNombre())) {
                System.out.println("Ese medicamento no existe, pruebe con uno que esté en la base de datos");
            } else {
                nombre = medicamento.getNombre();
                idMed = medicamento.getCodigo();
                cantidad = Menu.addStockDosis();
                Menu.limpiarBuffer();
                cronico = Menu.esCierto("¿Se trata de una dosis crónica?");
                if (cronico) {
                    periodo = Menu.addFecha("Vamos a introducir el dia en el que el usuario recibirá la siguiente dosis", "Cual será el dia de la siguiente dosis?", "Cual será el mes de la siguiente dosis?", "Cual será el año de la siguiente dosis?", "Introduzca una fecha superior al dia de hoy e intentelo de nuevo");
                }
                correcto = true;
            }
        } while (!correcto);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdMed() {
        return idMed;
    }

    public void setIdMed(int idMed) {
        this.idMed = idMed;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }
    

    public LocalDate getPeriodo() {
        return periodo;
    }

    public void setPeriodo(LocalDate periodo) {
        this.periodo = periodo;
    }

    public boolean isCronico() {
        return cronico;
    }

    public void setCronico(boolean cronico) {
        this.cronico = cronico;
    }

    @Override
    public String toString() {
        if (this.cronico) {
            return "Nombre: " + nombre + "\tCantidad: " + cantidad + "\tSiguiente toma: " + periodo;
        } else {
            return "Nombre: " + nombre + "\tCantidad: " + cantidad;
        }
    }

}
