package com.company;

// importamos todas las clases necesarias para crear nuestro server

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;


// Nuestra clase Server extenderá a Thread. De esa forma cada solicitud que
// reciba se ejecutará en un hilo independiente para poder atender múltiples
// clientes.
public class HTTPServer extends Thread {
    private Socket cliente = null; // creamos un socket llamado cliente
    private BufferedReader inCliente = null; // Buffer de recepción de entrada
    private DataOutputStream outCliente = null; // flujo de salida

    public HTTPServer(Socket cl) {
        cliente = cl;
    }

    public static void main(String args[]) throws Exception {
        // creamos un server en el puerto 5000
        ServerSocket server = new ServerSocket(5000, 10, InetAddress.getByName("127.0.0.1"));
        System.out.println("HTTPServer se inicio en el puerto 5000");
        while (true) {
            // Lazo infinito para aceptar solicitudes desde los clientes
            Socket connected = server.accept();
            HTTPServer httpServer = new HTTPServer(connected);
            httpServer.start(); // método heredado de Thread . iniciamos un hilo ...
        }
    }

    //implementamos el método run del Thread, ya que por defecto este método
    // está vacío
    public void run() {
        try {
            System.out.println("El Cliente " + cliente.getInetAddress() + ":"
                    + cliente.getPort() + " está conectado");
            // asignamos los flujos de entrada y salida ....
            inCliente = new BufferedReader(new
                    InputStreamReader(cliente.getInputStream()));
            outCliente = new DataOutputStream(cliente.getOutputStream());
            String requestString = inCliente.readLine();
            String headerLine = requestString;
            /* leemos la primera linea del encabezado recibido el que puede ser nulo */
            if (headerLine == null)
                return;
            // descomponemos el header en tokens
            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            // si el pedido es GET, entonces es el primer token del encabezado
            String httpMethod = tokenizer.nextToken();
            /* si es GET, los parámetros del pedido vienen inmediatamente después
            de GET */
            String httpQueryString = tokenizer.nextToken();
            System.out.println("El Pedido HTTP es ....");
            while (inCliente.ready()) {
                /* Leemos todo el pedido HTTP hasta el final.... */
                System.out.println(requestString);
                requestString = inCliente.readLine();
            }
            if (httpMethod.equals("GET")) {
                // vemos si han enviado parámetros. Si solo está la barra volvemos a
                // home
                if (httpQueryString.equals("/")) {
                    /* volvemos a home page */
                    homePage();
                } else if (httpQueryString.startsWith("/Hola")) {
                    /* vamos a la página Hola */
                    HolaPage(
                            httpQueryString.substring(httpQueryString.lastIndexOf('/') + 1,
                                    httpQueryString.length()));
                } else if (httpQueryString.startsWith("/Sumar")) {
                    String Parametro = httpQueryString.substring(httpQueryString.lastIndexOf('/') + 2,
                            httpQueryString.length());
                    String[] Parametros = Parametro.split("&");
                    // parametros es un array que contiene num1= xx , num2= yy
                    float num1 = Float.parseFloat(Parametros[0].substring(Parametros[0].indexOf("=") + 1));
                    float num2 = Float.parseFloat(Parametros[1].substring(Parametros[1].indexOf("=") + 1));
                    float resultado = num1 + num2;
                    SumarPage(resultado);
                } else if (httpQueryString.startsWith("/Restar")) {
                    String Parametro = httpQueryString.substring(httpQueryString.lastIndexOf('/') + 2,
                            httpQueryString.length());
                    String[] Parametros = Parametro.split("&");
                    // parametros es un array que contiene num1= xx , num2= yy
                    float num1 = Float.parseFloat(Parametros[0].substring(Parametros[0].indexOf("=") + 1));
                    float num2 = Float.parseFloat(Parametros[1].substring(Parametros[1].indexOf("=") + 1));
                    float resultado = num1 - num2;
                    RestarPage(resultado);
                } else if (httpQueryString.startsWith("/Multiplicar")) {
                    String Parametro = httpQueryString.substring(httpQueryString.lastIndexOf('/') + 2,
                            httpQueryString.length());
                    String[] Parametros = Parametro.split("&");
                    // parametros es un array que contiene num1= xx , num2= yy
                    float num1 = Float.parseFloat(Parametros[0].substring(Parametros[0].indexOf("=") + 1));
                    float num2 = Float.parseFloat(Parametros[1].substring(Parametros[1].indexOf("=") + 1));
                    float resultado = num1 * num2;
                    MultiplicarPage(resultado);
                } else if (httpQueryString.startsWith("/Dividir")) {
                    String Parametro = httpQueryString.substring(httpQueryString.lastIndexOf('/') + 2,
                            httpQueryString.length());
                    String[] Parametros = Parametro.split("&");
                    // parametros es un array que contiene num1= xx , num2= yy
                    float num1 = Float.parseFloat(Parametros[0].substring(Parametros[0].indexOf("=") + 1));
                    float num2 = Float.parseFloat(Parametros[1].substring(Parametros[1].indexOf("=") + 1));
                    if (num2 == 0) {
                        sendResponse(400, "<b>Intentó hacer una division por 0. " +
                                "<br>Eso es matemáticamente <strong>ILEGAL!</strong> :-)</b>");
                    }
                    float resultado = num1 / num2;
                    DividirPage(resultado);
                } else {
                    sendResponse(404, "<b>El Recurso solicitado no se encuentra disponible</b>");
                }
            } else {
                sendResponse(404, "<b>El Recurso solicitado no se encuentra disponible</b>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendResponse(int statusCode, String responseString) throws Exception {
        String HTML_START = "<html><title>Servidor HTTP</title><body>";
        String HTML_END = "</body></html>";
        String NEW_LINE = "\r\n";
        String statusLine = null;
        String serverdetails = Headers.SERVER + ": Servidor HTTP en Java";
        String contentLengthLine = null;
        String contentTypeLine = Headers.CONTENT_TYPE + ": text/html" + NEW_LINE;

        if (statusCode == 200)
            statusLine = Status.HTTP_200;
        else
            statusLine = Status.HTTP_404;
        statusLine += NEW_LINE;
        responseString = HTML_START + responseString + HTML_END;
        contentLengthLine = Headers.CONTENT_LENGTH + responseString.length() + NEW_LINE;
        outCliente.writeBytes(statusLine);
        outCliente.writeBytes(serverdetails);
        outCliente.writeBytes(contentTypeLine);
        outCliente.writeBytes(contentLengthLine);
        outCliente.writeBytes(Headers.CONNECTION + ": close" + NEW_LINE);
        /* adding the new line between header and body */
        outCliente.writeBytes(NEW_LINE);
        outCliente.writeBytes(responseString);
        outCliente.close();
    }

    public void homePage() throws Exception {
        StringBuffer responseBuffer = new StringBuffer();
        responseBuffer.append("<b>HTTPServer Página Principal</b><BR><BR>");
        responseBuffer.append("<p>El servidor permite realizar diferentes acciones usando<br>" +
                "el método HTTP GET. Vea la siguiente lista de endpoints posibles:</p>");
        responseBuffer.append("<ul>\n" +
                "    <li>Saludar: http://localhost:5000/Hola/nombre</li>\n" +
                "    <li>Sumar: http://localhost:5000/Sumar/num1=xxx&num2=yyy</li>\n" +
                "    <li>Restar: http://localhost:5000/Restar/num1=xxx&num2=yyy</li>\n" +
                "    <li>Multiplicar: http://localhost:5000/Multiplicar/num1=xxx&num2=yyy</li>\n" +
                "    <li>Dividir: http://localhost:5000/Dividir/num1=xxx&num2=yyy</li>\n" +
                "    <li>Tabla numero X: </li>\n" +
                "    <li>Login usuario:</li>\n" +
                "    <li>Registro de nuevos ususarios:</li>\n" +
                "</ul>");
        sendResponse(200, responseBuffer.toString());
    }

    public void HolaPage(String name) throws Exception {
        StringBuffer responseBuffer = new StringBuffer();
        responseBuffer.append("<style> h2 {background-color:honeydew; border: 2 px solid;" +
                "width: 80 %; margin: auto} </style>");
        responseBuffer.append("<h2 > Hola: ").append(name).append("</h2><BR>");
        sendResponse(200, responseBuffer.toString());
    }

    public void SumarPage(float valor) throws Exception {
        StringBuffer responseBuffer = new StringBuffer();
        responseBuffer.append("<style> h2 {background- color:lightblue; border: 2 px solid;" +
                "width: 80%; margin: auto}</style>");
        responseBuffer.append("<h2> El Resultado de la Suma es: ").append(String.valueOf(valor)).append("</h2><BR>");
        sendResponse(200, responseBuffer.toString());
    }

    public void RestarPage(float valor) throws Exception {
        StringBuffer responseBuffer = new StringBuffer();
        responseBuffer.append("<style> h2 {background- color:lightblue; border: 2 px solid;" +
                "width: 80%; margin: auto}</style>");
        responseBuffer.append("<h2> El Resultado de la Resta es: ").append(String.valueOf(valor)).append("</h2><BR>");
        sendResponse(200, responseBuffer.toString());
    }

    public void MultiplicarPage(float valor) throws Exception {
        StringBuffer responseBuffer = new StringBuffer();
        responseBuffer.append("<style> h2 {background- color:lightblue; border: 2 px solid;" +
                "width: 80%; margin: auto}</style>");
        responseBuffer.append("<h2> El Resultado de la Multiplicacion es: ").append(String.valueOf(valor)).append("</h2><BR>");
        sendResponse(200, responseBuffer.toString());
    }

    public void DividirPage(float valor) throws Exception {
        StringBuffer responseBuffer = new StringBuffer();
        responseBuffer.append("<style> h2 {background- color:lightblue; border: 2 px solid;" +
                "width: 80%; margin: auto}</style>");
        responseBuffer.append("<h2> El Resultado de la Division es: ").append(String.valueOf(valor)).append("</h2><BR>");
        sendResponse(200, responseBuffer.toString());
    }

    // constantes del protocolo HTTP
    private static class Headers {
        public static final String SERVER = "Server";
        public static final String CONNECTION = "Connection";
        public static final String CONTENT_LENGTH = "Content-Length";
        public static final String CONTENT_TYPE = "Content-Type";
    }

    // constantes de status
    private static class Status {
        public static final String HTTP_200 = "HTTP/1.1 200 OK";
        public static final String HTTP_404 = "HTTP/1.1 404 Recurso no Encontrado";
    }
}