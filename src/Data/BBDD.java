/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import static Ficheros.gestorLotes.escribirLote;
import static Ficheros.gestorMedicamentos.escribirMedicamento;
import Persona.*;
import static Ficheros.gestorMedico.escribirMedico;
import static Ficheros.gestorPaciente.escribirPaciente;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 *
 * @author baske
 */
public class BBDD {

    private static final String rutaCarpeta = "src/Inventario";
    private static final String BBDD = rutaCarpeta + "/BBDD";

    /**
     * Este metodo pretende introducir la base de datos base en el programa,
     * solo se iniciar� cuando el archivo "BBDD" no exista, es decir, la
     * primera vez que se inicie el programa y a partir de entonces no se
     * volver�n a introducir
     */
    public static void crearBBDD() {
        File carpeta = new File(rutaCarpeta);
        File archivoBaseDatos = new File(BBDD);

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        if (archivoBaseDatos.exists()) {
            System.out.println("Base de datos ya iniciada\n");
        } else {
            System.out.println("Iniciando base de datos...\n");
            try {
                archivoBaseDatos.createNewFile();
            } catch (IOException ex) {
                System.out.println("Fallo iniciando la base de datos");
            }

            //Medicamentos
            //Aqui estan los ANALGESICOS:
            escribirMedicamento(new Medicamento("IBUPROFENO", "ANALGESICO", 300, false, false));
            escribirMedicamento(new Medicamento("PARACETAMOL", "ANALGESICO", 300, false, false));

            //Lotes
            LocalDate ibuprofeno1 = LocalDate.of(2025, 5, 20);
            LocalDate ibuprofeno2 = LocalDate.of(2030, 10, 5);

            escribirLote(new Lote(1001, 300, ibuprofeno1));
            escribirLote(new Lote(1001, 50, ibuprofeno2));

            LocalDate paracetamol1 = LocalDate.of(2025, 8, 2);
            LocalDate paracetamol2 = LocalDate.of(2027, 12, 2);

            escribirLote(new Lote(1002, 200, paracetamol1));
            escribirLote(new Lote(1002, 150, paracetamol2));

            //Aquí estan los ANTIINFECCIOSOS
            escribirMedicamento(new Medicamento("ANOXICILINA", "ANTIINFECCIOSO", 150, false, true));
            escribirMedicamento(new Medicamento("CEFLACOR", "ANTIINFECCIOSO", 200, false, false));

            //Lotes
            LocalDate anoxicilina1 = LocalDate.of(2030, 5, 12);
            LocalDate anoxicilina2 = LocalDate.of(2028, 3, 13);

            escribirLote(new Lote(1003, 35, anoxicilina1));
            escribirLote(new Lote(1003, 150, anoxicilina2));

            LocalDate ceflacor1 = LocalDate.of(2026, 12, 12);
            LocalDate ceflacor2 = LocalDate.of(2025, 10, 26);

            escribirLote(new Lote(1004, 300, ceflacor1));
            escribirLote(new Lote(1004, 200, ceflacor2));

            //Aquí están los ANTITUSIVOS
            escribirMedicamento(new Medicamento("CLOPERASTINA", "ANTITUSIVO", 40, false, true));
            escribirMedicamento(new Medicamento("DEXTROMETORFANO", "ANTITUSIVO", 200, false, false));

            //Lotes
            LocalDate cloperastina1 = LocalDate.of(2030, 12, 3);
            LocalDate cloperastina2 = LocalDate.of(2026, 3, 12);

            escribirLote(new Lote(1005, 140, cloperastina1));
            escribirLote(new Lote(1005, 200, cloperastina2));

            LocalDate dextrometorfano1 = LocalDate.of(2029, 3, 12);
            LocalDate dextrometorfano2 = LocalDate.of(2026, 3, 12);

            escribirLote(new Lote(1006, 30, dextrometorfano1));
            escribirLote(new Lote(1006, 100, dextrometorfano2));

            //Aquí están los ANTIACIDOS
            escribirMedicamento(new Medicamento("HIDROTALCITA", "ANTIACIDO", 400, true, false));
            escribirMedicamento(new Medicamento("CARBONATO DE CALCIO", "ANTIACIDO", 20, false, false));

            //Lotes
            LocalDate hidrotalcita1 = LocalDate.of(2030, 12, 12);
            LocalDate hidrotalcita2 = LocalDate.of(2027, 3, 16);

            escribirLote(new Lote(1007, 200, hidrotalcita1));
            escribirLote(new Lote(1007, 40, hidrotalcita2));

            LocalDate carbonato1 = LocalDate.of(2030, 1, 20);
            LocalDate carbonato2 = LocalDate.of(2026, 4, 12);

            escribirLote(new Lote(1008, 300, carbonato1));
            escribirLote(new Lote(1008, 20, carbonato2));
            //Personas

            //Medico
            escribirMedico(new Medico("111A", "Diego", "diego@hospital.com", 1234));
            escribirMedico(new Medico("222B", "Sara", "sara@hospital.com", 1243));
            escribirMedico(new Medico("333C", "Raquel", "raquel@hospital.com", 3421));
            escribirMedico(new Medico("444D", "Leon", "leon@hospital.com", 4321));

            //Pacientes
            escribirPaciente(new Paciente("555E", "Pepe", "pepe@gmail.com"));
            escribirPaciente(new Paciente("666F", "Diego", "diego@gmail.com"));
            escribirPaciente(new Paciente("777G", "Marta", "marta@gmail.com"));
            escribirPaciente(new Paciente("888H", "Sara", "sara@gmail.com"));

        }

    }

}
