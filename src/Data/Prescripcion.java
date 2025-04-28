/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;
import Exception.IdMedicoNoExistenteException;
import java.util.ArrayList;
import java.util.Scanner;
import Ficheros.gestorMedicamentos;
/**
 *
 * @author baske
 */
public class Prescripcion {
    
    private int idMedico;
    private String nombreMedico;
    private String idPaciente;
    private String nombrePaciente;
    private String emailPaciente;
    
    ArrayList<Dosis> receta = new ArrayList<>();//conjunto de dosis 
    
    
    public Prescripcion() throws IdMedicoNoExistenteException{
        idMedico = Menu.addIdMedico();
        menuPrescripcion();
        

    }
    
    private void mostrarLista(){
        for(Dosis d : receta){
            System.out.println(d.toString());
        }
    }
    private void menuPrescripcion(){
        Scanner teclado = new Scanner(System.in);
        boolean correcto = false;
        
        do{
            System.out.println("1-mostrar lista de medicamentos\n2-Crear medicamentos");
            int usuario = teclado.nextInt();
            
            switch(usuario){
                case 1:
                    mostrarLista();
                    break;
                case 2:
                    receta.add(new Dosis());
                    break;
            }
        
        }while(!correcto);
    }
    
    
    
}
