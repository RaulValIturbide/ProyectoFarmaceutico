
package Persona;

import java.io.Serializable;

/**
 *
 * @author baske
 */
public class Medico extends Persona implements Serializable  {

    private int idMedico;

    public Medico(String dni, String nombre, String email, int idMedico) {
        super(dni, nombre, email);
        this.idMedico = idMedico;
    }

    public int getIdMedico() {
        return idMedico;
    }

    @Override
    public String mostrarDatos(){
        return "Dni: " + super.getDni() + "\tId medico: " + idMedico + "\tNombre: " + super.getNombre() + "\tEmail: " + super.getEmail();
    }

}
