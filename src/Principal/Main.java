package Principal;

import Data.*;
import Exception.IdMedicoNoExistenteException;
import Exception.MedicamentoInexistenteException;
import Exception.MedicamentoYaExistenteException;
import Ficheros.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        boolean endProgram = false;//booleano que gestionara el momento en el que se cierre el programa
        boolean endLoop = false;//booleano que gestionará el ciclo de los menus principales
        int usuario = 0;
        gestorMedico listaMed = new gestorMedico();
        gestorPaciente listaPaciente = new gestorPaciente();
        BBDD.crearBBDD();

        do {
            try {
                System.out.println("1-FARMACEUTICO\n2-MEDICO\n\n0-Salir");
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
   
                                                   0-Volver atras""");
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

                                        break;
                                    case 4:

                                        break;
                                    case 0:
                                        endLoop = true;
                                }
                            } catch (InputMismatchException ex) {
                                System.out.println("Introduzca un tipo de valor numerico");
                                teclado.nextLine();
                            }

                        } while (!endLoop);
                        break;
                    case 2:
                        menuPrescripcion(teclado);
                        break;
                    case 0:
                        endProgram = true;
                        break;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Introduce un tipo de valor numerico");
                teclado.nextLine();
            }
        } while (!endProgram);
    }

    private static void menuMedicamento(Scanner teclado) {
        gestorMedicamentos medicamentoGS = new gestorMedicamentos();//Lista donde se guardan los medicamentos
        boolean salirMenu = false;
        int usuario = 0;
        do {
            try {
                System.out.println("""
                                   1-Añadir medicamento
                                   2-Ver lista medicamentos 
                                   3-Buscar medicamento
                                   
                                   0-Volver atras""");
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
                        medicamentoGS.buscarMedicamentoIterador();
                        break;
                    case 0:
                        salirMenu = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Introduzca un valor de tipo numerico");
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
                                   
                                   0-Volver atras
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
                        System.out.println("TO DO");
                        break;
                    case 4:
                        lotesGS.generarAlertaManual();
                        break;
                    case 0:
                        salirMenu = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Introduzca un valor de tipo numerico");
                teclado.nextLine();//Limpiar buffer
            }
        } while (!salirMenu);

    }

    private static void menuPrescripcion(Scanner teclado) {
        boolean salirMenu = false;
        int usuario = 0;

        do {
            System.out.println("1-Crear receta\n\n0-Volver atras");
            System.out.print(">>");
            usuario = teclado.nextInt();

            switch (usuario) {
                case 1:
                    try {
                        Prescripcion p1 = new Prescripcion();
                    } catch (IdMedicoNoExistenteException ex) {
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
