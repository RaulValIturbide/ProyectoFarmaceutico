package Principal;

import Data.*;
import Exception.IdNoExistenteException;
import Exception.MedicamentoInexistenteException;
import Exception.MedicamentoYaExistenteException;
import Ficheros.*;
import Persona.Medico;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        
        Scanner teclado = new Scanner(System.in);
        boolean endProgram = false;//booleano que gestionara el momento en el que se cierre el programa
        boolean endLoop = false;//booleano que gestionar� el ciclo de los menus principales
        int usuario = 0;
        gestorMedico listaMed = new gestorMedico();
        gestorPaciente listaPaciente = new gestorPaciente();
        BBDD.crearBBDD();
        
        do {
            try {
                System.out.println("1-FARMACEUTICO\n2-M�DICO\n\n0-Salir");
                System.out.print(">>");
                usuario = teclado.nextInt();
                endLoop = false;
                switch (usuario) {
                    case 1:
                        do {
                            try {
                                System.out.println("""
                                                   1-Medicamentos
                                                   2-Lotes
                                                   3-Prescripciones
   
                                                   0-Volver atr�s""");
                                System.out.print(">>");
                                usuario = teclado.nextInt();
                                switch (usuario) {
                                    case 1:
                                        menuMedicamento(teclado);
                                        break;
                                    case 2:
                                        menuLote(teclado);
                                        break;
                                    case 3:
                                        menuPrescripcionFarmaceutico(teclado);
                                        
                                        break;
                                    case 4:
                                        
                                        break;
                                    case 0:
                                        endLoop = true;
                                }
                            } catch (InputMismatchException ex) {
                                System.out.println("Introduzca un tipo de valor num�rico");
                                teclado.nextLine();
                            }
                            
                        } while (!endLoop);
                        break;
                    case 2:
                        try {
                            menuPrescripcionMedico(teclado);
                        } catch (IdNoExistenteException ex) {
                            System.out.println("Error: " + ex.getMessage());
                        }
                        break;
                    case 0:
                        endProgram = true;
                        break;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Introduce un tipo de valor num�rico");
                teclado.nextLine();
            }
        } while (!endProgram);
    }
    
    private static void menuMedicamento(Scanner teclado) {
        gestorMedicamentos medicamentoGS = new gestorMedicamentos();//Lista donde se guardan los medicamentos
        gestorLotes lotesGS = new gestorLotes();
        boolean salirMenu = false;
        int usuario = 0;
        do {
            try {
                System.out.println("""
                                   1-A�adir medicamento
                                   2-Ver lista medicamentos 
                                   3-Buscar medicamento
                                   
                                   0-Volver atr�s""");
                System.out.print(">>");
                usuario = teclado.nextInt();
                
                switch (usuario) {
                    case 1:
                        try {
                            gestorMedicamentos.escribirMedicamento(new Medicamento());
                        } catch (MedicamentoYaExistenteException ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case 2:
                        medicamentoGS.generarTreeSet();
                        medicamentoGS.mostrarLista();
                        break;
                    case 3:
                        medicamentoGS.generarTreeSet();
                        Medicamento aux = medicamentoGS.buscarMedicamentoIterador();
                        if(aux != null){
                        System.out.println("\nInfo del medicamento:\n" + aux.toString() + "Stock total del medicamento: " + lotesGS.stockTotal(aux.getCodigo()) + " cajas\n");
                        }else{
                            System.out.println("\nNo existe ningun medicamento con ese Nombre/Codigo\n");
                        }
                        break;
                    case 0:
                        salirMenu = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Introduzca un valor de tipo num�rico");
                teclado.nextLine();//Limpiar buffer
            }
        } while (!salirMenu);
        
    }
    private static void menuLote(Scanner teclado) {
        gestorLotes lotesGS = new gestorLotes();
        boolean salirMenu = false;
        int usuario = 0;
        do {
            try {
                System.out.println("""
                                   1-Crear lote
                                   2-Ver lista de lotes 
                                   3-Buscar lote
                                   4-Comprobar caducidades (manual)
                                   
                                   0-Volver atr�s
                                   """);
                System.out.print(">>");
                usuario = teclado.nextInt();
                
                switch (usuario) {
                    case 1:
                        try {
                            gestorLotes.escribirLote(new Lote());
                        } catch (MedicamentoInexistenteException ex) {
                            System.out.println("Error: " + ex.getMessage());
                        }
                        break;
                    case 2:
                        lotesGS.generarTreeSet();
                        lotesGS.mostrarLista();
                        break;
                    case 3:
                        int total = lotesGS.stockTotal(1001);
                        System.out.println(total);
                        break;
                    case 4:
                        lotesGS.generarAlertaManual();
                        break;
                    case 0:
                        salirMenu = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Introduzca un valor de tipo num�rico");
                teclado.nextLine();//Limpiar buffer
            }
        } while (!salirMenu);
        
    }
    
    private static void menuPrescripcionFarmaceutico(Scanner teclado) {
        gestorPrescripcion gs = new gestorPrescripcion();
        ArrayList<Prescripcion> listaNoRevisadas = gs.generarListaPrescripcion();
        
        if (listaNoRevisadas.isEmpty()) {
            System.out.println("Ya no quedan mas prescripciones por revisar\n");
        } else {
            System.out.println("REVISANDO LA SIGUIENTE PRESCRIPCI�N:\n");
            System.out.println(listaNoRevisadas.getFirst().toString());
            System.out.println("\n");
            boolean aceptada = Menu.esCierto("�Aceptas esta prescripci�n?");
            if (aceptada) {
                System.out.println("Enviando prescripci�n...");
                gs.prescripcionRevisada(listaNoRevisadas.getFirst());//Aqui vamos a comprobar si reune los requisitos para ser aceptada o no
                listaNoRevisadas.removeFirst();//Eliminamos la prescripcion de la lista
                gs.escribirArrayList(listaNoRevisadas);//Volvemos a escribir la lista en el archivo para que machaque la anterior y ya no exista la 1�
            } else {
                teclado.nextLine();
                System.out.println("�Alguna nota para el m�dico?");
                String nota = teclado.nextLine();
                listaNoRevisadas.getFirst().setNotaFarmaceutico(nota);//Establecemos en la prescripcion una nota para el medico
                
                gs.prescripcionRevisada(listaNoRevisadas.getFirst());//Aqui vamos a comprobar si reune los requisitos para ser aceptada o no
                listaNoRevisadas.removeFirst();//Eliminamos la prescripcion de la lista
                gs.escribirArrayList(listaNoRevisadas);//Volvemos a escribir la lista en el archivo para que machaque la anterior y ya no exista la 1�
            }
        }
    }

    private static void menuPrescripcionMedico(Scanner teclado) throws IdNoExistenteException {
        boolean salirMenu = false;
        int usuario = 0;
        Medico medico = null;
        medico = gestorMedico.encontrarMedico(Menu.addIdMedico());
        
        do {
            System.out.println("1-Crear prescripci�n\n2-Comprobar correo\n\n0-Volver atr�s");
            System.out.print(">>");
            usuario = teclado.nextInt();
            
            switch (usuario) {
                case 1:
                    try {
                        gestorPrescripcion.escribirPrescripcion(new Prescripcion(medico));
                    } catch (IdNoExistenteException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                    break;
                case 2:
                    break;
                
                case 0:
                    salirMenu = true;
                    break;
            }
            
        } while (!salirMenu);
        
    }
}
