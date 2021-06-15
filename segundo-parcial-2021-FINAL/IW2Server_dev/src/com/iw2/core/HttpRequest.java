/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iw2.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 *
 * @author CARLOMAGNO
 */
public class HttpRequest {
  InputStream flujoentrada;
  private static BufferedReader buffer;
  private String[] parametros = null;
  private String[] HttpRequest;
  private String parametro = null;
  private String metodoPedido;
  private String linea;
  private String httpPedido;
  private String header;
  private String NEW_LINE = "\r\n";
  private String accion;
  private String SITE_NAME = "api";

  public HttpRequest(String flujoentrada) throws IOException {
    httpPedido = flujoentrada;
    System.out.println(httpPedido);
    // como httpPedido es un String separados por saltos de linea, generamos un
    // array
    HttpRequest = httpPedido.split("\r\n");
  }

  public String getMetodo() throws IOException {
    // Sout(HttpRequest[0]);
    String[] palabras = HttpRequest[0].split("/");
    metodoPedido = palabras[0];
    return metodoPedido;
  }

  public String getAccion() throws IOException {
    accion = null;
    String[] palabras = HttpRequest[0].split("/");
    accion = palabras[1].substring(0, palabras[1].indexOf("HTTP")).trim();
    return accion;
  }

  public String[] getParametros() throws IOException {
    parametros = null;
    String[] palabras = HttpRequest[0].split("/");
    String Pedido = palabras[1].substring(0, palabras[1].indexOf("HTTP"));
    Sout(Pedido);
    if (Pedido.length() > 0) {
      Sout("Estoy en length >0");
      if (Pedido.indexOf("?") != -1) { // hay `por lo menos un parametro
        Sout("Estoy en no tengo ?");
        if (Pedido.indexOf("&") == -1) { // Hay un solo parametro
          Sout("Estoy en no tengo &");
          Pedido += "&";
          parametros = Pedido.split("&");
        } else {
          parametros = Pedido.substring(1).split("&");
        }
      } else {
        parametros = null;
        Sout("Estoy en no envíe parametros ....");
      }
    }
    return parametros;
  }

  public String getValorParametro(String parametro) throws IOException {
    String Busqueda = parametro;
    String Valor = null;
    for (int i = 0; i < parametros.length; i++) {

      if (parametros[i].contains(Busqueda)) {
        Valor = parametros[i].substring(parametros[i].indexOf("=") + 1);
      }

    }
    return Valor;
  }

  public String getHeader() throws IOException {
    header = this.buffer.readLine();
    return header;
  }

  public String getHeaderBody() throws IOException {
    String headerBody = null;
    int posicionDatos;
    while ((linea = buffer.readLine()) != null && (linea.length() != 0)) {
      headerBody += "HTTP-HEADER: " + linea + "\n\r";
    }
    return headerBody;
  }

  public void enviarArchivo(String nombreArchivo, PrintStream ps) throws IOException {
    String filename = "";
    if (nombreArchivo.equals("")) {
      String s = HttpRequest[0];
      StringTokenizer st = new StringTokenizer(s);

      try {
        // Paraeamos el nombre del archivo del comando Get
        if (st.hasMoreElements() && st.nextToken().equalsIgnoreCase("GET") && st.hasMoreElements())
          filename = st.nextToken();
        else
          throw new FileNotFoundException(); // Bad request

        // Si no pidieron nada, enviamos la página index.html
        if (filename.endsWith("/"))
          filename += "com/company/index.html";

        // quitamos la / del nombre del archivo
        while (filename.indexOf("/") == 0)
          filename = filename.substring(1);

        // Reemplazamos la / por \ para archivos basados en windows
        filename = filename.replace('/', File.separator.charAt(0));

        // Como no se debe permitir accesos a subdirectorios elimimamos los caracteres
        // ..
        if (filename.indexOf("..") >= 0 || filename.indexOf(':') >= 0 || filename.indexOf('|') >= 0)
          throw new FileNotFoundException();

        // If a directory is requested and the trailing / is missing,
        // send the client an HTTP request to append it. (This is
        // necessary for relative links to work correctly in the client).
        if (new File(filename).isDirectory()) {
          filename = filename.replace('\\', '/');
          ps.print("HTTP/1.0 301 Moved Permanently\r\n" + "Location: /" + filename + "/\r\n\r\n");
          ps.close();
          return;
        }

      } catch (FileNotFoundException x) {
        ps.print("HTTP/1.0 404 Not Found\r\n" + "Content-type: text/html\r\n\r\n" + "<html><head></head><body>"
            + filename + " not found</body></html>\n");
        ps.close();
      }
    } else
      filename = nombreArchivo;

    // abrimos el archivo y lo enviamos
    // Open the file (may throw FileNotFoundException)

    System.out.println(filename);
    InputStream f = new FileInputStream(filename);

    // Determine the MIME type and print HTTP header
    String mimeType = "text/plain";
    if (filename.endsWith(".html") || filename.endsWith(".htm"))
      mimeType = "text/html";
    else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
      mimeType = "image/jpeg";
    else if (filename.endsWith(".gif"))
      mimeType = "image/gif";
    else if (filename.endsWith(".pdf"))
      mimeType = "application/pdf";
    else if (filename.endsWith(".class"))
      mimeType = "application/octet-stream";
    else if (filename.endsWith(".doc"))
      mimeType = "application/msword";
    System.out.println(mimeType);
    ps.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");

    // Send file contents to client, then close the connection
    byte[] a = new byte[1048576];
    int n;
    while ((n = f.read(a)) > 0)
      ps.write(a, 0, n);
    ps.close();
  }

  private void Sout(Object object) {

    System.out.println(object);

  }
}
