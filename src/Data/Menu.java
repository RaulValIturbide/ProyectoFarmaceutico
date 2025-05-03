package Data;

import java.util.Scanner;
import java.util.InputMismatchException;
import Exception.*;
import Ficheros.gestorMedico;
import Ficheros.gestorPaciente;
import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Esta clase sirve para interactuar con el usuario
 *
 */
public class Menu {

    static Scanner teclado = new Scanner(System.in);

    /**
     * Metodo para que el usuario introduzca el nombre del medicamento
     *
     * @return devuelve el nombre del medicamento en mayusculas para poder
     * tratar todo en el mismo formato
     */
    public static String addNombre() {
        System.out.println("Cual es el nombre del medicamento?");
        String nombre = teclado.nextLine();
        nombre = nombre.toUpperCase();
        return nombre;
    }

    /**
     * Metodo que pedirá la clasificación al usuario y si no es ninguna de las
     * posibles lo pedirá hasta que se den las condiciones correctas
     *
     * @return devuelve la clasificación
     */
    public static String addClasificacion() {
        boolean correcto = false;
        String respuesta = null;
        int usuario = 0;
        do {
            try {
                System.out.println("Cual es la clasificación?\n1-ANALGESICO\n2-ANTIINFECCIOSO\n3-ANTITUSIVO\n4-ANTIACIDO");
                System.out.print(">>");
                usuario = teclado.nextInt();
                switch (usuario) {
                    case 1:
                        respuesta = "ANALGESICO";
                        correcto = true;
                        break;
                    case 2:
                        respuesta = "ANTIINFECCIOSO";
                        correcto = true;
                        break;
                    case 3:
                        respuesta = "ANTITUSIVO";
                        correcto = true;
                        break;
                    case 4:
                        respuesta = "ANTIACIDO";
                        correcto = true;
                        break;
                }
                if (!rangoCorrecto(usuario, 1, 4)) {
                    throw new ClasificacionIncorrectaException("Elija un número de los posibles");
                }

            } catch (InputMismatchException ex) {
                System.out.println("Eliga un tipo de valor numerico");
                teclado.nextLine();//Limpiar buffer
            } catch (ClasificacionIncorrectaException cie) {
                System.out.println("Error: " + cie.getMessage());
            }
        } while (!correcto);

        return respuesta;
    }

    public static String buscarMedicamento() {
        System.out.println("Nombre o codigo del medicamento que estas buscando");
        System.out.print(">>");
        String usuario = teclado.nextLine();
        return usuario;
    }

    /**
     * Este metodo sirve para decir la cantidad de cajas que estamos
     * introduciendo al objeto medicamento que estamos creando
     *
     * @return devuelve la cantidad de cajas nuevas en formato int
     */
    public static int addStockAhora() {
        boolean correcto = false;
        int usuario = 0;
        do {
            try {
                System.out.println("Introduzca la cantidad de stock del medicamento que está introduciendo");
                System.out.print(">>");
                usuario = teclado.nextInt();
                if (usuario < 0) {
                    throw new NumeroNegativoException("Introduzca un número igual o superior a 0");
                }
                correcto = true;
            } catch (InputMismatchException ex) {
                System.out.println("Error: Introduzca un tipo de valor numerico");
                teclado.nextLine();//Limpiamos buffer
            } catch (NumeroNegativoException nne) {
                System.out.println("Error: " + nne.getMessage());
                teclado.nextLine();//Limpiamos buffer
            }
        } while (!correcto);
        return usuario;
    }

    /**
     * Este metodo sirve para el stock mínimo que el usuario debe introducir a
     * la hora de meter cada medicamento
     *
     * @return devuelve un int que es la cantidad mínima de cajas necesarias
     */
    public static int addStockMinimo() {
        boolean correcto = false;
        int usuario = 0;
        do {
            try {
                System.out.println("Introduzca la cantidad de stock mínimo que puede haber");
                System.out.print(">>");
                usuario = teclado.nextInt();
                if (usuario < 0) {
                    throw new NumeroNegativoException("Introduzca un número igual o superior a 0");
                }
                correcto = true;
            } catch (InputMismatchException ex) {
                System.out.println("Error: Introduzca un tipo de valor numerico");

            } catch (NumeroNegativoException nne) {
                System.out.println("Error: " + nne.getMessage());

            } finally {
                teclado.nextLine();//Limpiamos el buffer
            }

        } while (!correcto);
        return usuario;
    }

    /**
     * Metodo que establece la cantidad que el medico va a recetarle al paciente
     *
     * @return devuelve el numero que va a recetarle
     */
    public static int addStockDosis() {
        int uso = 0;
        boolean correcto = false;

        do {
            try {
                System.out.println("Dosis a recetar:");
                System.out.print(">>");
                uso = teclado.nextInt();
                correcto = true;

            } catch (InputMismatchException ex) {
                System.out.println("Introduce un tipo de valor numerico");
                teclado.nextLine();//Limpiar buffer
            }
        } while (!correcto);
        return uso;
    }

    /**
     * Este metodo crea una pregunta para saber si es algo true o false
     *
     * @param frase la pregunta que se hará para la que el usuario debe
     * responder true o false
     * @return
     */
    public static boolean esCierto(String frase) {
        boolean correcto = false;
        boolean resultado = false;
        String usuario = null;
        do {
            try {
                System.out.println(frase + " Y/N");
                System.out.print(">>");
                usuario = teclado.nextLine();

                if (usuario.equalsIgnoreCase("Y")) {
                    resultado = true;
                    correcto = true;
                } else if (usuario.equalsIgnoreCase("N")) {
                    resultado = false;
                    correcto = true;
                } else {
                    throw new TrueFalseException("Introduzca \"Y\" o \"N\"");
                }

            } catch (TrueFalseException tfe) {
                System.out.println("Error: " + tfe.getMessage());
            }
        } while (!correcto);
        return resultado;
    }

    /**
     * Metodo que pregunta al usuario la fecha de caducidad del nuevo lote y
     * comprueba de que sea una fecha logica
     *
     * @return devolverá la fecha que haya introducido el usuario
     */
    public static LocalDate addFecha(String frase1, String preguntaDia, String preguntaMes, String preguntaYear, String fechaIncorrecta) {
        int dia = 0;
        int mes = 0;
        int anio = 0;
        LocalDate fecha = null;
        LocalDate sistema = LocalDate.now();
        boolean correcto = false;

        do {
            try {
                System.out.println(frase1);

                System.out.println(preguntaDia);
                System.out.print(">>");
                dia = teclado.nextInt();

                System.out.println(preguntaMes);
                System.out.print(">>");
                mes = teclado.nextInt();

                System.out.println(preguntaYear);
                System.out.print(">>");
                anio = teclado.nextInt();

                //Con la fecha inicializada ahora intentamos construirla con los datos del usuario
                fecha = LocalDate.of(anio, mes, dia);
                if (fecha.isBefore(sistema) || fecha.isEqual(sistema)) {
                    throw new FechaMinimaException(fechaIncorrecta);
                }
                correcto = true;

            } catch (InputMismatchException ex) {
                System.out.println("Introduce solo valores numericos");

            } catch (DateTimeException dte) {
                System.out.println("Introduzca una fecha válida");
            } catch (FechaMinimaException fme) {
                System.out.println("Error: " + fme.getMessage());
            } finally {
                teclado.nextLine(); //Limpiamos buffer
            }
        } while (!correcto);
        return fecha;

    }

    /**
     * Este metodo comprueba los datos introducidos por el usuario y se los
     * muestra para preguntarle si está de acuerdo con todos los datos
     * declarados o desea repetir la creación del medicamento
     *
     * @param nombre nombre introducido por el usuario
     * @param clasificacion el tipo que ha introducido el usuario
     * @param stockMinimo el stockMinimo introducido por el usuario
     * @param altoCosto si se considera o no de alto coste
     * @param restringido si se considera o no de alto coste
     * @return devolverÃ¡ true, creando el objeto medico o false volviendo a
     * repetir las preguntas
     */
    public static boolean confirmar(String nombre, String clasificacion, int stockMinimo, boolean altoCosto, boolean restringido) {

        boolean correcto = false;
        boolean confirmar = false;
        String usuario = null;
        do {

            System.out.format("""
                          Nombre: %s
                          Clasificación: %s
                          Stock máximo: %d
                          Alto costo: %b
                          Restringido: %b
                          """,
                    nombre, clasificacion, stockMinimo, altoCosto, restringido);
            System.out.println("\n\nSon estos datos correctos? Y/N");
            System.out.print(">>");
            usuario = teclado.nextLine();
            if (usuario.equalsIgnoreCase("Y")) {
                confirmar = true;
                correcto = true;
            } else if (usuario.equalsIgnoreCase("N")) {
                confirmar = false;
                correcto = true;
            } else {
                System.out.println("Error: Introduzca Y/N");
            }
        } while (!correcto);

        return confirmar;

    }

    /**
     * Este metodo nos permite dejar que el usuario (supuesto medico) escriba su
     * id en la consola, el metodo primero se asegurará de que el usuario
     * escriba solo digitos y que sean 4, si no lo hace entrará en loop hasta
     * que lo haga, despues de esto, el metodo buscará en un metodo
     * complementario si el id escrito por el medico se encuentra en la base de
     * datos, si lo hace nos devolvera el numero de id para escribirlo en la
     * prescripcion, si no lo encuentra lanzará una excepcion para cancelar la
     * construccion del objeto "Prescripcion" y aconsejará al medico que si no
     * está registrado lo haga en el menu
     *
     * @return nos devuelve o bien el numero de id del medico o una excepcion ya
     * que nunca deberia de poder llegar al -1
     * @throws IdMedicoNoExistenteException la excepcion que se lanzará si el
     * metodo no encuentra un id igual que el proporcionado por el usuario
     */
    public static int addIdMedico() throws IdNoExistenteException {
        String usuario = null;
        String expReg = "^\\d{4}$";
        boolean correcto = false;

        do {
            System.out.println("Introduce tu id de medico");
            System.out.print(">>");
            usuario = teclado.nextLine();
            if (usuario.matches(expReg)) {
                int idMed = Integer.parseInt(usuario);

                if (gestorMedico.MedicoExisteEnLaBBDD(idMed)) {
                    return idMed;
                } else {
                    throw new IdNoExistenteException("Este id no existe en ningún medico de nuestra BBDD si no estas registrado, prueba a hacerlo en el menú principal.\nCancelando prescripción...\n\n");
                }
            } else {
                System.out.println("El id de medico son 4 digitos, pruebe de nuevo");
            }
        } while (!correcto);
        return -1;
    }

    /**
     * Este metedo permite al medico una vez que ha entrado en su cuenta el
     * introducir el id del paciente, el "dni" se ha simplificado a 3 numeros y
     * una letra por motivos de simpleza, si el medico no escribe un dni acorde
     * con el formato establecido (3 numeros y 1 mayus) le pedirá nuevamente que
     * lo intente, si el dni posee el formato necesario pero no existe en la
     * base de datos se cancelará la prescripción dejando al medico que registre
     * al usuario en el menu principal, si se encuentra un dni con esos datos
     * introducidos se devolverán en formato String
     *
     * @return devuelve el id de un paciente existente en la base de datos o se
     * cancela su construccion
     * @throws IdNoExistenteException esta excepcion permite cancelar la
     * creación del constructor
     */
    public static String addIdPaciente() throws IdNoExistenteException {
        boolean correcto = false;
        String usuario = null;
        String expReg = "\\d{3}[A-Z]";

        do {
            System.out.println("Introduce el dni del paciente");
            System.out.print(">>");
            usuario = teclado.nextLine();
            if (!usuario.matches(expReg)) {
                System.out.println("Introduce un dni con un formato correcto \"111Z\"");
            } else if (!gestorPaciente.pacienteExisteEnLaBBDD(usuario)) {
                throw new IdNoExistenteException("Este dni no se encuentra en la base de datos, cancelando prescripcion...");
            } else {
                correcto = true;
            }
        } while (!correcto);

        return usuario;

    }

    public static void limpiarBuffer() {
        teclado.nextLine();
    }

    /**
     * Este metodo sirve para comprobar que el usuario mete los datos dentro del
     * rango correcto
     *
     * @param usuario el numero del usuario
     * @param minimo el numero minimo que puede introducirse(inclusive)
     * @param maximo el numero maximo que puede introducirse(inclusive)
     * @return true si es correcto, false si no lo es
     */
    private static boolean rangoCorrecto(int usuario, int minimo, int maximo) {
        boolean respuesta = false;
        if (usuario <= maximo && usuario >= minimo) {
            respuesta = true;
        } else {
            respuesta = false;
        }
        return respuesta;
    }

}
