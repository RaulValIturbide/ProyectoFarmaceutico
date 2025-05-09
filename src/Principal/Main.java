package Principal;

import Data.*;
import Exception.IdNoExistenteException;
import Exception.MedicamentoInexistenteException;
import Exception.MedicamentoYaExistenteException;
import Ficheros.*;
import Persona.Medico;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        boolean endProgram = false;//booleano que gestionara el momento en el que se cierre el programa
        boolean endLoop = false;//booleano que gestionar� el ciclo de los menus principales
        int usuario = 0;
        gestorLotes lotesGS = new gestorLotes();
        gestorMedicamentos medGS = new gestorMedicamentos();
        BBDD.crearBBDD();
        gestorPrescripcion gs = new gestorPrescripcion();
        gs.generarListaPrescripcion();
        

        do {
            try {
                System.out.println("1-FARMACEUTICO\n2-M�DICO\n\n0-Salir");
                System.out.print(">>");
                usuario = teclado.nextInt();
                endLoop = false;
                switch (usuario) {
                    case 1:
                        medGS.alertaStockMinimo();//ACTIVA de manera "automatica" un listado de los medicamentos por debajo del minimo para avisar al farmaceutico
                        lotesGS.generarAlertaManual(); //ACTIVA de manera "automatica" cada vez que se inicia el menu del farmaceutico
                        do {
                            try {
                                System.out.println("""
                                                   1-Medicamentos
                                                   2-Lotes
                                                   3-Revisar prescripcion
                                                   4-Historial
                                                   
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
                                        gs.leerHistorialPrescripcion();
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
                        Medicamento aux = medicamentoGS.buscarMedicamentoIterador(Menu.buscarMedicamento());
                        if (aux != null) {
                            System.out.println("\nInfo del medicamento:\n" + aux.toString() + "Stock total del medicamento: " + lotesGS.stockTotal(aux.getCodigo()) + " cajas\n");
                        } else {
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
                        lotesGS.generarTreeSet();
                        lotesGS.buscarLote();
                        //Metodo que devuelva la informacion de un lote en particular
                         
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
        gs.generarListaPrescripcion();

        if (gs.getPrescripcionNoRevisada().isEmpty()) {
            System.out.println("Ya no quedan mas prescripciones por revisar\n");

        } else {
            System.out.println("REVISANDO LA SIGUIENTE PRESCRIPCI�N:\n");
            System.out.println(gs.getPrescripcionNoRevisada().getFirst().toString());
            System.out.println("\n");
            boolean aceptada = Menu.esCierto("�Aceptas esta prescripci�n?");
            if (aceptada) {
                System.out.println("Enviando prescripci�n...");
                gs.prescripcionRevisada(gs.getPrescripcionNoRevisada().getFirst());//Aqui vamos a comprobar si reune los requisitos para ser aceptada o no
                gs.getPrescripcionNoRevisada().removeFirst();//Eliminamos la prescripcion de la lista
                gs.escribirArrayList(gs.getPrescripcionNoRevisada());//Volvemos a escribir la lista en el archivo para que machaque la anterior y ya no exista la 1�
            } else {
                teclado.nextLine();
                System.out.println("�Alguna nota para el m�dico?");
                String nota = teclado.nextLine();
                System.out.println("Enviando mensaje...\n");
                System.out.println("Mensaje enviado al correo del medico " + gs.getPrescripcionNoRevisada().getFirst().getMedico().getEmail() + "\n");
                //Tenemos que enviarla a revisadas
                gs.prescripcionRevisada(gs.getPrescripcionNoRevisada().getFirst());
                gs.getPrescripcionNoRevisada().removeFirst();//Eliminamos la prescripcion de la lista
                gs.escribirArrayList(gs.getPrescripcionNoRevisada());//Volvemos a escribir la lista en el archivo para que machaque la anterior y ya no exista la 1�
            }
        }
    }

    private static void menuPrescripcionMedico(Scanner teclado) throws IdNoExistenteException {
        boolean salirMenu = false;
        int usuario = 0;
        Medico medico = null;
        medico = gestorMedico.encontrarMedico(Menu.addIdMedico());

        do {
            System.out.println("1-Crear prescripci�n\n\n0-Volver atr�s");
            System.out.print(">>");
            usuario = teclado.nextInt();

            switch (usuario) {
                case 1:
                    try {
                        gestorPrescripcion.escribirPrescripcion(new Prescripcion(medico));
                    } catch (IdNoExistenteException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }catch(ClassCastException cce){
                        System.out.println("\nProblema con la prescripcion, informando al medico...\n");
                    }
                    break;
                case 0:
                    salirMenu = true;
                    break;
            }

        } while (!salirMenu);

    }
}
