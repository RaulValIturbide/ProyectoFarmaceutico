package Persona;
import java.io.Serializable;
/**
 *
 * @author baske
 */
public class Paciente extends Persona implements Serializable{
    
    public Paciente(String dni,String nombre,String email){
        super(dni,nombre,email);
    }
    
    
    @Override
    public String mostrarDatos(){
        return "Dni: " + super.getDni() + "\tNombre: " + super.getNombre() + "\tEmail: " + super.getEmail();
    }
    
    
}
