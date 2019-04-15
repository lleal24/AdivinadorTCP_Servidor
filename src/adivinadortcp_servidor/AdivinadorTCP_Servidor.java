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
 * @author user
 */
public class AdivinadorTCP_Servidor {

    //establece el puerto en el servidor
    public static final int PUERTO = 4444;
    //Generacion de numero aleatorio rango 1 a 100
    public static int n = 100;
    public static final int aleatorio = (int) (Math.random() * n) + 1;
    public static int contador = 1;

    public static void main(String[] args) throws IOException {
        ServerSocket socketServidor = null;
        try {
            socketServidor = new ServerSocket(PUERTO);
        } catch (IOException e) {
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
            System.out.println("Numero: " + aleatorio);
            // Establece canal de entrada
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            // Establece canal de salida
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);

            // Hace eco de lo que le proporciona el cliente, hasta que recibe "Adios"
            while (true) {
                String str = entrada.readLine();
                //conversion de String a int
                int numero = Integer.parseInt(str);
                String mensaje;
                System.out.println("Cliente: " + str);
                System.out.println("CONTADOR: " + contador);
                if(numero < aleatorio){
                    //System.out.println("El numero es mayor");      
                    mensaje = "El numero es mayor | numero ingresado "+str+" | te quedan: "+ Integer.toString(10 - contador);
                }else{
                    mensaje = "El numero es menor | numero ingresado "+str+" | te quedan: "+ Integer.toString(10 - contador);
                    }
                
                if(numero == aleatorio){
                    mensaje = "Adivinaste!! | numero ingresado "+str+" | te quedan: "+ Integer.toString(10 - contador);
                    break;
                }
                salida.println(mensaje);
                contador++;
                if (str.equals("Adios")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
        salida.close();
        entrada.close();
        socketCliente.close();
        socketServidor.close();

    }

}
