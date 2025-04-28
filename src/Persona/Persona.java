/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persona;

import java.io.Serializable;

/**
 *
 * @author baske
 */
abstract class Persona implements Serializable {
    
    private String dni;
    private String nombre;
    private String email;
    
    public Persona(String dni,String nombre,String email){
        this.dni = dni;
        this.nombre = nombre;
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
    
    
    public abstract String mostrarDatos();
    
    
    
    
}
