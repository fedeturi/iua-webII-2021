
package com.iw2.core;

import java.net.ServerSocket;

/**
 *
 * @author CARLOMAGNO
 */
public class Server {
    // private static final int PORT=8877;
    public void IniciarServidor(int Puerto) {
        try {
            ServerSocket server = new ServerSocket(Puerto);
            System.out.println("Servidor IW2 funcionado en el Puerto: " + Puerto);
            while (true) {
                new administradorSocket(server.accept()).procesarSocket();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
