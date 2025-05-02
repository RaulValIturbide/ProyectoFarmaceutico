package Data;

import Exception.IdNoExistenteException;
import Ficheros.gestorLotes;
import java.util.ArrayList;
import java.util.Scanner;
import Ficheros.gestorPaciente;
import Persona.Medico;
import Persona.Paciente;
import java.io.Serializable;

/**
 *
 * @author baske
 */
public class Prescripcion implements Serializable {

    private Medico medico; //El medico que creará la prescripcion
    private int idMedico; //El id del medico
    private Paciente paciente; //El paciente al que se le recetará la prescripcion
    private String idPaciente; //El id del paciente
    private String emailPaciente; //El email del paciente
    private String notaFarmaceutico;

    ArrayList<Dosis> receta = new ArrayList<>();//conjunto de dosis 

    public Prescripcion(Medico medico) throws IdNoExistenteException {
        this.medico = medico;
        paciente = gestorPaciente.encontrarPaciente(Menu.addIdPaciente());
        idMedico = medico.getIdMedico();
        idPaciente = paciente.getDni();
        emailPaciente = paciente.getEmail();
        menuPrescripcion(medico, paciente);
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

    /**
     * Este metodo gestionará la creacion de dosis de la prescripcion hasta que
     * se firme la receta
     *
     * @param medico el medico que firma la receta
     * @param paciente el paciente al que se le entrega la receta
     */
    private void menuPrescripcion(Medico medico, Paciente paciente) {
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
                    System.out.println("");
                    break;
                case 3:
                    System.out.println(medico.mostrarDatos());
                    System.out.println(paciente.mostrarDatos());
                    System.out.println("");
                    mostrarLista();
                    break;
                case 0:
                    if (receta.isEmpty()) {
                        System.out.println("No puedes firmar la prescripción si no has añadido ninguna dosis\n");
                    } else {
                        System.out.println("Enviando prescripción...");
                        correcto = true;
                    }
            }

        } while (!correcto);
    }
    


    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getNotaFarmaceutico() {
        return notaFarmaceutico;
    }

    public void setNotaFarmaceutico(String notaFarmaceutico) {
        this.notaFarmaceutico = notaFarmaceutico;
    }
    

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getEmailPaciente() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente = emailPaciente;
    }

    public ArrayList<Dosis> getReceta() {
        return receta;
    }

    public void setReceta(ArrayList<Dosis> receta) {
        this.receta = receta;
    }

    @Override
    public String toString() {
        return "Info Médico: " + medico.mostrarDatos()
                + "\nInfo Paciente: " + paciente.mostrarDatos()
                + "\nRecetas: \n" + receta.toString();
    }

}
