/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import Exception.MedicamentoYaExistenteException;
import Ficheros.gestorCodigoMedicamento;
import Ficheros.gestorMedicamentos;
import java.io.Serializable;
import java.util.Objects;

public class Medicamento implements Serializable, Comparable<Medicamento> {

    private final int codigo;//Este codigo aparecerá cada vez que se cree un medicamento dandole un valor unico que será persistente
    private String nombre; //Nombre del medicamento que servirá como código ya que ningún medicamento tiene el mismo nombre que otro
    private String clasificacion; //Se dividirán en "ANALGESICO", "ANTIINFECCIOSO","ANTITUSIVO" o "ANTIACIDO", lo usaremos como medicina alterniva
    private int stockMinimo; //La cantidad mínima que podemos tener de esta medicina antes de que salte la alarma
    private boolean altoCosto; //Será true si se considera de alto costo 
    private boolean restringido; //Será true si se considera un medicamento restringido

    /**
     * Este constructor lo usaremos para iniciar las medicinas desde el propio
     * programa
     *
     * @param nombre
     * @param clasificacion
     * @param stockMinimo
     * @param altoCosto
     * @param restringido
     */
    public Medicamento(String nombre, String clasificacion, int stockMinimo, boolean altoCosto, boolean restringido) {
        codigo = gestorCodigoMedicamento.generarCodMed();
        this.nombre = nombre;
        this.clasificacion = clasificacion;
        this.stockMinimo = stockMinimo;
        this.altoCosto = altoCosto;
        this.restringido = restringido;
    }

    /**
     * Este constructor lo usaremos para que nuestro farmaceutico pueda
     * introducir medicinas de manera manual
     */
    public Medicamento() throws MedicamentoYaExistenteException {
        boolean correcto = false;
        do {
            nombre = Menu.addNombre();
            if (gestorMedicamentos.MedicamentoExisteEnLaBBDD(nombre)) {
                System.out.println("Este medicamento ya existe en la base de datos, cancelando su creacion...");
                throw new MedicamentoYaExistenteException("");
            }
            clasificacion = Menu.addClasificacion();
            stockMinimo = Menu.addStockMinimo();
            altoCosto = Menu.isAltoCosto();
            restringido = Menu.isRestringido();
            correcto = Menu.confirmar(nombre, clasificacion, stockMinimo, altoCosto, restringido);
        } while (!correcto);
        codigo = gestorCodigoMedicamento.generarCodMed();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public boolean isAltoCosto() {
        return altoCosto;
    }

    public void setAltoCosto(boolean altoCosto) {
        this.altoCosto = altoCosto;
    }

    public boolean isRestringido() {
        return restringido;
    }

    public void setRestringido(boolean restringido) {
        this.restringido = restringido;
    }

    @Override
    public int compareTo(Medicamento otro) {
        if (this.codigo > otro.codigo) {
            return 1;
        } else if (this.codigo == otro.codigo) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        Medicamento aux = (Medicamento) obj;
        return codigo == aux.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo
                + "\nNombre: " + nombre
                + "\nClasificacion: " + clasificacion
                + "\nStock minimo: " + stockMinimo
                + "\nAlto costo: " + altoCosto
                + "\nRestringido: " + restringido
                + "\n\n";
    }
}
