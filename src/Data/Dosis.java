/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import Ficheros.gestorMedicamentos;
import java.time.LocalDate;

/**
 *
 * @author baske
 */
public class Dosis {

    String nombre;//Nombre del medicamento
    int cantidad;//cantidad de capsulas
    LocalDate periodo;//El tiempo que tiene que tomarse la medicacion
    boolean cronico;//si es cronico o no

    public Dosis() {
        boolean correcto = false;
        do {
            nombre = Menu.addNombre();
            if (!gestorMedicamentos.MedicamentoExisteEnLaBBDD(nombre)) {
                System.out.println("Ese medicamento no existe, pruebe con uno que esté en la base de datos");
            } else {
                cantidad = Menu.addStockDosis();
                Menu.limpiarBuffer();
                cronico = Menu.isCronico();
                if (cronico) {
                    periodo = Menu.addFecha("Vamos a introducir el dia en el que el usuario recibirá la siguiente dosis", "Cual será el dia de la siguiente dosis?", "Cual será el mes de la siguiente dosis?", "Cual será el año de la siguiente dosis?", "Introduzca una fecha superior al dia de hoy e intentelo de nuevo");
                }
                correcto = true;
            }
        } while (!correcto);
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
