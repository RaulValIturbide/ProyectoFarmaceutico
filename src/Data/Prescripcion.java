/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;
import Exception.IdMedicoNoExistenteException;
import java.util.ArrayList;
import java.util.Scanner;
import Ficheros.gestorMedicamentos;
import Ficheros.gestorMedico;
import Persona.Medico;
import Persona.Paciente;
import java.io.ObjectInputStream;
/**
 *
 * @author baske
 */
public class Prescripcion {
 
    private Medico medico;
    private int idMedico;
    private Paciente paciente;
    private String idPaciente;
    private String emailPaciente;
    
    ArrayList<Dosis> receta = new ArrayList<>();//conjunto de dosis 
    
    
    public Prescripcion() throws IdMedicoNoExistenteException{
        idMedico = Menu.addIdMedico();
        medico = gestorMedico.encontrarMedico(idMedico);
        menuPrescripcion(medico);
        

    }
    
    private void mostrarLista() {
        if (receta.isEmpty()) {
            System.out.println("Todavia no has añadido ninguna dosis a esta prescripción");
        } else {
            for (Dosis d : receta) {
                System.out.println(d.toString());
            }
        }
    }

    private void menuPrescripcion(Medico medico) {
        Scanner teclado = new Scanner(System.in);
        boolean correcto = false;

        do {
            System.out.println("1-Añadir dosis a la prescripción\n2-Leer las dosis actuales\n3-Ver prescripcion\n\n0-Firmar receta");
            System.out.print(">>");
            int usuario = teclado.nextInt();

            switch (usuario) {
                case 1:
                    receta.add(new Dosis());
                    break;
                case 2:
                      mostrarLista();
                    break;
                case 3:
                    System.out.println(medico.mostrarDatos());
                    break;
                case 0:
                    if (receta.isEmpty()) {
                        System.out.println("No puedes firmar la prescripcion si no has añadido ninguna dosis");
                    } else {
                        System.out.println("Enviar Prescripcion TODO");
                    }
            }

        } while (!correcto);
    }
    
   

    
    
}
