package Ficheros;

import Data.Dosis;
import Data.Prescripcion;
import Interface.comiteFarmaceutico;
import java.io.EOFException;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author baske
 */
public class gestorPrescripcion implements comiteFarmaceutico {

    private static final String rutaCarpeta = "src/Inventario/Prescripciones";
    private static final String rutaArchivoNoRevisada = rutaCarpeta + "/porRevisar";
    private static final String rutaArchivoRevisada = rutaCarpeta + "/Revisadas";
    private ArrayList<Prescripcion> PrescripcionNoRevisada = new ArrayList<>();

    private void addPrescripcionNoRevisada(Prescripcion p) {
        PrescripcionNoRevisada.add(p);
    }

    public ArrayList<Prescripcion> getPrescripcionNoRevisada() {
        return PrescripcionNoRevisada;
    }

    public void setPrescripcionNoRevisada(ArrayList<Prescripcion> PrescripcionNoRevisada) {
        this.PrescripcionNoRevisada = PrescripcionNoRevisada;
    }

    public static void escribirPrescripcion(Prescripcion p) {
        ObjectOutputStream salida = null;
        File archivo = new File(rutaArchivoNoRevisada);
        File carpeta = new File(rutaCarpeta);
        try {
            if (!archivo.exists()) {
                carpeta.mkdirs();
                salida = new ObjectOutputStream(new FileOutputStream(rutaArchivoNoRevisada));
                salida.writeObject(p);
            } else {
                salida = new MiObjectOutputStream(new FileOutputStream(rutaArchivoNoRevisada, true));
                salida.writeObject(p);
            }
        } catch (IOException ex) {
            System.out.println("ERROR FATAL en la escritura del la prescripcion en el archivo");
            ex.printStackTrace();
        } finally {

            if (salida != null) {
                try {
                    salida.close();
                } catch (IOException ex) {
                    System.out.println("Error al intentar cerrar la salida del escritor de prescripciones");
                }
            }

        }
    }
        public static void escribirPrescripcionRevisada(Prescripcion p) {
        ObjectOutputStream salida = null;
        File archivo = new File(rutaArchivoRevisada);
        File carpeta = new File(rutaCarpeta);
        try {
            if (!archivo.exists()) {
                carpeta.mkdirs();
                salida = new ObjectOutputStream(new FileOutputStream(rutaArchivoRevisada));
                salida.writeObject(p);
            } else {
                salida = new MiObjectOutputStream(new FileOutputStream(rutaArchivoRevisada, true));
                salida.writeObject(p);
            }
        } catch (IOException ex) {
            System.out.println("ERROR FATAL en la escritura del la prescripcion revisada en el archivo");
            ex.printStackTrace();
        } finally {

            if (salida != null) {
                try {
                    salida.close();
                } catch (IOException ex) {
                    System.out.println("Error al intentar cerrar la salida del escritor de prescripciones revisadas");
                }
            }

        }
    }
    
        /**
         * Este metodo es el metodo principal en el que se ejecutaran el resto de metodos complementarios para saber que es lo que debemos hacer con la prescripción, puesto que hay
         * muchas dudas cada caso se resuelve en su propio metodo complementario pero finaliza aquí donde se decide el camino, para saber cuales van a ser las bifurcaciones de la 
         * prescripcion debemos saber 1º si hay suficiente stock, de no haberlo la prescripcion es eliminada directamente, 2º si hay alguna dosis en la prescripcion que necesite o no
         * autorización, 3º si la necesita debemos saber si el comite acepta o no y 4º debemos saber si en la prescripción hay alguna dosis cronica que necesite enviar un mensaje al 
         * correo de nuestro paciente
         * @param p  la prescripción que estamos tratando
         */
    public void prescripcionRevisada(Prescripcion p) {
        gestorLotes lotesGS = new gestorLotes();
        ArrayList<Dosis> listaDosis = p.getReceta();//La lista con todas las dosis de la prescripcion
        //Primero comprobamos si hay suficiente stock de todos los medicamentos pedidos para aceptar la prescripcion
        if (suficienteStock(p)) {
            System.out.println("\nSi, hay suficiente stock para la dosis requerida por el médico de los medicamentos\n");
            //Después comprobamos si se necesita una autorización, es decir, si alguna dosis es de alto coste o restringido
            if (necesitaAutorizacion(p)) {
                System.out.println("Informando al comite para pedir la autorización de la prescripción...");
                System.out.println("Esperando respuesta...\n");
                //Preguntar al comité
                //Si dice que si -> pasamos a comprobar si es cronico o no siguiendo el arbol de este else
                //Si dice que no -> la prescripción es cancelada y se informa al medico(no se como)
                if (esAceptado(p)) {
                    System.out.println("Prescripción aceptada por el comité!");
                    System.out.println("Analizando si existen dosis cronicas...\n");

                    //Comprobamos si hay alguna dosis que necesite una nueva cita si es crónico
                    ArrayList<Dosis> dosisCronicas = mensajeCronico(p);//Almacenamos (si las hubiese) las dosis cronicas que hay en la prescripción
                    if (dosisCronicas != null) {
                        for (Dosis d : dosisCronicas) {
                            System.out.println("\nEnviando mensaje al correo electronico del paciente:" + p.getEmailPaciente() + " para que venga a recibir su nueva dosis\nde "
                                    + d.getNombre() + " el día " + d.getPeriodo() + "\n");
                        }
                        //Se envia la prescripcion a Revisadas y se retira el stock que toque
                    } else {                       
                        for (Dosis d : listaDosis) {
                            lotesGS.restarStock(d.getMedicamento(),d.getCantidad());
                        }
                        //Se envia la prescripcion a Revisadas y se retira el stock que toque
                    }
                } else {
                    System.out.println("Prescripción rechazada por el comite");
                    System.out.println("Informando al medico...\n");
                    System.out.println("Mensaje enviado al correo " + p.getMedico().getEmail() + "\n");
                    //Enviar mensaje al medico

                }
            } else {
                //Comprobamos si hay alguna dosis que necesite una nueva cita si es crónico
                ArrayList<Dosis> dosisCronicas = mensajeCronico(p);//Almacenamos (si las hubiese) las dosis cronicas que hay en la prescripción
                if (dosisCronicas != null) {
                    for (Dosis d : dosisCronicas) {
                        System.out.println("\nEnviando mensaje al correo electronico del paciente:" + p.getEmailPaciente() + " para que venga a recibir su nueva dosis\nde "
                                + d.getNombre() + " el día " + d.getPeriodo() + "\n");
                    }
                    //Se envia la prescripcion a Revisadas y se retira el stock que toque
                } else {
                    for (Dosis d : listaDosis) {
                        lotesGS.restarStock(d.getMedicamento(), d.getCantidad());
                    }
                    //Se envia la prescripcion a Revisadas y se retira el stock que toque
                }

            }
            escribirPrescripcionRevisada(p);
        } else {
            System.out.println("\nNo hay suficiente stock en uno de los medicamentos inscritos");
            System.out.println("Cancelando la prescripción e informando al médico...\n");
            System.out.println("Mensaje enviado al correo " + p.getMedico().getEmail() + "\n");
        }

    }
    
    /**
     * Este metodo tiene como objetivo saber si existen cronicos en la prescripción para poder enviar los mensajes al paciente
     * para ello usará un array donde se acumularán las dosis que son cronicas de donde podremos sacar la fecha donde se le debe informar
     * @param p la prescripción que estamos revisando
     * @return nos devolverá null si no existen dosis cronicas o un array con las cronicas de existir una o varias
     */
    private ArrayList<Dosis> mensajeCronico(Prescripcion p) {
        ArrayList<Dosis> aux = p.getReceta();
        gestorLotes gs = new gestorLotes();
        ArrayList<Dosis> cronicos = new ArrayList();
        for (Dosis d : aux) {
            if (d.isCronico()) {
                cronicos.add(d);
            }
        }
        if (cronicos.isEmpty()) {
            return null;
        } else {
            return cronicos;
        }
    }
    /**
     * Este metodo comprueba que los medicamentos de las dosis que el medico estça recetando tengan suficiente stock total para suplir con la demanda
     * si algun medicamento no tiene suficiente stock para la dosis, se cancelará la prescripcion y devolverá false
     * @param p la prescripcion que estamos analizando
     * @return devolverá false si una dosis de la prescripcion es demasiado grande como para suplir el stock o true si está correcto
     */
    private boolean suficienteStock(Prescripcion p) {
        ArrayList<Dosis> aux = p.getReceta();
        gestorLotes gs = new gestorLotes();

        for (Dosis d : aux) {
            if (d.getCantidad() > gs.stockTotal(d.getIdMed())) {
                return false;
            }
        } 
        return true;
    }
    /**
     * Este metodo comprueba que los medicamentos introducidos en el arrayList de dosis de la prescripción necesitan o no autorización,
     * para ello comprueban en un for each por cada medicamento si sus costes o restricciones son positivas
     * @param p la prescripción que estamos tratando
     * @return devolverá true si necesita autorización o false si no la necesita
     */
    private boolean necesitaAutorizacion(Prescripcion p) {
        ArrayList<Dosis> aux = p.getReceta();
        gestorLotes gs = new gestorLotes();

        for (Dosis d : aux) {
            if (d.getMedicamento().isAltoCosto() || d.getMedicamento().isRestringido()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Prescripcion> generarListaPrescripcion() {
        File archivo = new File(rutaArchivoNoRevisada);

        ObjectInputStream entrada = null;

        try {
            entrada = new ObjectInputStream(new FileInputStream(rutaArchivoNoRevisada));

            while (true) {
                Prescripcion aux = (Prescripcion) entrada.readObject();
                addPrescripcionNoRevisada(aux);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("");
        } catch (ClassNotFoundException cnf) {
            System.out.println("No se encuentra esta clase");
        } catch (EOFException eo) {
            System.out.println("");
        } catch (IOException ex) {
            System.out.println("ERROR FATAL");
            ex.printStackTrace();
        }

        return getPrescripcionNoRevisada();
    }

    public void escribirArrayList(ArrayList<Prescripcion> lista) {

        ObjectOutputStream salida = null;

        try {
            salida = new ObjectOutputStream(new FileOutputStream(rutaArchivoNoRevisada));

            for (Prescripcion p : lista) {
                salida.writeObject(p);
            }
            salida.flush();
        } catch (IOException ex) {
            System.out.println("Fallo en la salida de la escritura ArrayList de prescripciones");
        } finally {
            if (salida != null) {
                try {
                    salida.close();
                } catch (IOException e) {
                    System.out.println("Fallo al intentar cerrar el flijo de salida de escritura de prescripciones");
                }
            }
        }
    }

    public void mostrarListaNoRevisada() {
        for (Prescripcion p : PrescripcionNoRevisada) {
            System.out.println(p.toString());
        }
    }
    

    
    @Override
    public boolean esAceptado(Prescripcion p) {
        Random random = new Random();
        LocalDate hoy = LocalDate.now();
        boolean decision = false;
        if (p.getReceta().getFirst().getMedicamento().isRestringido()) {//Si la primera medicacion de la prescripción es restringida dicen no
            decision = false;
        } else if (hoy.lengthOfMonth() == 28) { //Si es febrero dicen si
            decision = true;
        } else if (hoy.lengthOfMonth() == 31 && p.getReceta().getFirst().getMedicamento().isAltoCosto()) { //Si es un mes con 31 dias y el medicamento de la primera receta es de alto coste dicen no 
            decision = false;
        } else {//Si no se cumple nada de eso tiran un dado de 3 caras :)
            int numero = random.nextInt(1, 4);
            switch (numero) {
                case 1:
                    decision = true;//Sale 1 dicen que si
                    break;
                case 2:
                    decision = true;//Sale 2 dicen que si
                    break;
                case 3:
                    decision = false;//Sale 3 dicen que no
                    break;
            }
        }
        return decision;
    }

}
