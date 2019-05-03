/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adivinadortcp_servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Lorena Leal ID: 208313
 * @asignatura Interconectividad
 */
public class AdivinadorTCP_Servidor {

    //@PUERTO, variabe que define el puerto del servidor que esta a la escucha 
    public static final int PUERTO = 4444;

    //@tope, valor maximo de numero aleatorio
    public static int tope = 100;
    //Generacion de numero aleatorio rango entre 0 y 100 con la funcion Math.random()
    public static final int aleatorio = (int) (Math.random() * tope);
    //@contador, variable que lleva el conteo del numero de intentos del cliente, inicializada en 1
    public static int contador = 1;

    private static String decimal(String b) {
        int decimal = Integer.parseInt(b, 2);
        System.out.println("Binario Recibido : " + b + " / nuemro decimal : " + decimal);
        return String.valueOf(decimal);
    }

    //Metodo principal de la clase
    public static void main(String[] args) throws IOException {
        //@socketServidor objeto tipo socket del lado del servidor
        ServerSocket socketServidor = null;
        try {
            //Instancia del objeto socketServidor tipo socket con parametro PUERTO
            socketServidor = new ServerSocket(PUERTO);
        } catch (IOException e) {
            //Mensaje que se lanzara en caso de no ejecutarse correctamente el try
            //Posteriormente se saldra del programa
            System.out.println("No puede escuchar en el puerto: " + PUERTO);
            System.exit(-1);
        }

        Socket socketCliente = null;
        BufferedReader entrada = null;
        PrintWriter salida = null;
        System.out.println("Escuchando: " + socketServidor);

        try {
            // Se bloquea hasta que recibe alguna petición de un cliente
            // abriendo un socket para el cliente
            socketCliente = socketServidor.accept();
            System.out.println("Connexión acceptada: " + socketCliente);
            //Inicio de medicion de tiempo
            long Tinicial = System.currentTimeMillis();
            System.out.println("Numero aletorio generado: " + aleatorio);
            // Establece canal de entrada
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            // Establece canal de salida
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);

            // Ciclo infinito que se ejecuta hasta que el cliente adivine el numero aleatorio
            while (true) {
                //@str, variable que contiene el mensaje de entrada del cliente tipo string
                String str = entrada.readLine();
                //@numero, variable que guarda el numero enviado convertido en entero para poder comparar
                int numero = Integer.parseInt(decimal(str)); //
                //@mensaje, definicion de varible tipo string que almacena un concatenado de cadena de caracteres
                //con las pistas que se enviaran al cliente, camibara segun compare el numero sea mayor o menor 
                String mensaje;
                //@nivel, definicion de variable tipo String que almacena un concatenado de cadena de caacteres
                //que almacena el mensaje que se envia al cliente cuanto se adivine el numero cambiara 
                //segun el rango donde se cuentre el numero de intentos que lleve
                String nivel = "";

                //Comparacion de numero enviado por el cliente con el aleatorio
                if (numero < aleatorio) {
                    //System.out.println("El numero es mayor");      
                    //Si el numero es menor el mensaje indicara que el numero aleatorio es mayor
                    mensaje = "El numero es mayor | numero ingresado " + numero + " |Intento: " + contador + "/10" + "| te quedan: " + Integer.toString(10 - contador) + " intentos";
                } else {
                    //Si el numero es mayor el mensaje indicara que el numero aleatorio es menor
                    mensaje = "El numero es menor | numero ingresado " + numero + " |Intento: " + contador + "/10" + "| te quedan: " + Integer.toString(10 - contador) + " intentos";
                }
 
                //Condicional que evalua si el numero enviado es equivalente al aleatorio
                if (numero == aleatorio) {

                    //Subcondicionales que comparan el contador de intentos con los rangos dados
                    if (contador == 1) {
                        nivel = "Me has vencido en el primer intento, realmente "
                                + "eres un Maestro, el número secreto adivinado es: "
                                + Integer.toString(aleatorio);
                    }
                    if (contador >= 2 && contador <= 3) {
                        nivel = "Casi eres eres un Maestro, te ha tomado " + contador
                                + " cantidad de intentos, has gastado muy pocos intentos "
                                + "y estuviste muy cerca, el número secreto adivinado es: "
                                + Integer.toString(aleatorio);
                    }
                    if (contador >= 4 && contador <= 5) {
                        nivel = "Eres un Aprendiz, te ha tomado " + contador
                                + " cantidad de intentos,   podrías haberlo descubierto en"
                                + " menor número de intentos, el número secreto adivinado es: "
                                + Integer.toString(aleatorio);
                    }
                    if (contador >= 6 && contador <= 7) {
                        nivel = "Eres un Novato, te has tomado " + contador
                                + " cantidad de intentos,  has tardado mucho, el número secreto adivinado es: "
                                + Integer.toString(aleatorio);
                    }
                    if (contador >= 8 && contador <= 10) {
                        nivel = "Eres un Ingenuo, te has tomado " + contador
                                + " cantidad de intentos y te he vencido, "
                                + "el número secreto es: " + Integer.toString(aleatorio);
                    }

                    //calculo del tiempo de ejecucion
                    long Tfinal = System.currentTimeMillis();
                    double tiempo = (Tfinal - Tinicial) / 1000;
                    //En caso de avinar el numero se envia palabra clave Adivinaste y se concatena con 
                    //la variable nivel
                    mensaje = "Adivinaste !! " + nivel + " | Tu tiempo fue: " + tiempo + " Segundos";
                    //Envio del mensaje al cliente
                    salida.println(mensaje);
                    //Salida del programa
                    break;

                }
                //se envia el mensaje al cliente
                salida.println(mensaje);
                //se incrementa en uno el contador de intentos
                contador++;

            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
        //Cierre del socket
        salida.close();
        entrada.close();
        socketCliente.close();
        socketServidor.close();

    }

}
