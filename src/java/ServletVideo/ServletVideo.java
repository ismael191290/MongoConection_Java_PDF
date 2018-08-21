/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServletVideo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONException;
import org.json.JSONObject;
import util.HttpUrl;

/**
 *
 * @author GS-Server
 */
@WebServlet(name = "ServletVideo", urlPatterns = {"/ServletVideo"})
@MultipartConfig(
        location = "C:\\Users\\GS-Server\\Desktop\\Proyectos Integra",
        //location = "C:\\Users\\root\\Desktop\\MEXA",
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 180, // 10 MB 
        maxRequestSize = 1024 * 1024 * 9 * 81 // 25 MB
)
public class ServletVideo extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            JSONObject json = new JSONObject();
            String filename = "";
            try {
                Collection<Part> parts = request.getParts();

                String type = Normalizer.normalize(new String(request.getParameter("tipo")//se sgrego
                        .getBytes("ISO-8859-1"), "UTF-8"), Normalizer.Form.NFD);//se sgrego
                final String evento = type.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");//se sgrego

                for (Part part : parts) {
                    /*se modifico*/
                    if (getFileName(part) != null) {
                        filename = System.currentTimeMillis() + getFileName(part);
                        part.write(filename);
                    }
                }

                JSONObject c = new JSONObject();
                c.put("usuario", request.getParameter("usuario"));
                c.put("tipo", request.getParameter("tipo"));
                c.put("latitud", Double.parseDouble(request.getParameter("latitud")));
                c.put("longitud", Double.parseDouble(request.getParameter("longitud")));
                c.put("fecha", request.getParameter("fecha"));
                c.put("estado", request.getParameter("estado"));
                c.put("municipio", request.getParameter("municipio"));
                c.put("colonia", request.getParameter("colonia"));
                c.put("cp", request.getParameter("cp"));
                c.put("direccion", request.getParameter("direccion"));
                c.put("descripcion", request.getParameter("descripcion"));
                c.put("nos", request.getParameter("nos"));
                
                
                if(request.getParameter("tel") !=null){
                   c.put("", request.getParameter("tel"));
                }
                if(request.getParameter("nombre") !=null){
                   c.put("nombre", request.getParameter("nombre"));
                }
                if(request.getParameter("email") !=null){
                   c.put("email", request.getParameter("email"));
                }
                
                if(request.getParameter("video") !=null){
                   c.put("video", request.getParameter("video"));
                }
                
                
                System.out.println(""+c.toString());
                
              /*  final String filename2 = filename;
                Runnable myRunnable = new Runnable() {

                    @Override
                    public void run() {
                        upLoad2Server(c);
                    }
                };
                Thread t = new Thread(myRunnable);
                t.start();*/
              
              
                json.put("Mensaje", "El documento se almaceno exitosamente");
                System.out.println("" + json.toString());
              //  datoSend("" + json.toString());
            } catch (Exception e) {
                json.put("Mensaje", "Error ");
               // datoSend("" + e.getMessage());
               // System.out.println("Error en Servlet Video " + e.getMessage());
                e.printStackTrace();
            }
            out.print(json);
        }
    }

    private String getFileName(Part part) {
        for (String token : part.getHeader("content-disposition").split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public static int upLoad2Server(JSONObject json) {

        //  String fileName = sourceFileUri;
        int serverResponseCode = 0;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String responseFromServer = "";

        // File sourceFile = new File(sourceFileUri);

        /*  if (!sourceFile.isFile()) {
            return 0;
        }*/
        try { // open a URL connection to the Servlet
            //    FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(HttpUrl.IP_LOGIN);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Encoding", "");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("video", json.getString(""));
          /*  conn.setRequestProperty("usuario", "" + casilla.getUsuario());
            conn.setRequestProperty("casilla", cas);
            conn.setRequestProperty("latitud", "" + casilla.getLatitud());
            conn.setRequestProperty("longitud", "" + casilla.getLongitud());
            conn.setRequestProperty("direccion", "" + direc);
            conn.setRequestProperty("descripcion", "" + casilla.getDesc());
            conn.setRequestProperty("nos", "" + casilla.getRc() + "");
            conn.setRequestProperty("tel", "" + casilla.getTel());
            conn.setRequestProperty("seccion", "" + sec);
            conn.setRequestProperty("distrito", "" + dis);
            conn.setRequestProperty("tipo", "" + casilla.getTipo());
            conn.setRequestProperty("imei", "" + casilla.getRg());
            conn.setRequestProperty("nombre", "" + casilla.getNombre());
            conn.setRequestProperty("contactto", "" + casilla.getContacto());

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"usuario\"" + lineEnd + lineEnd + casilla.getUsuario() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"casilla\"" + lineEnd + lineEnd + cas + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"latitud\"" + lineEnd + lineEnd + casilla.getLatitud() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"longitud\"" + lineEnd + lineEnd + casilla.getLongitud() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"direccion\"" + lineEnd + lineEnd + direc + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"tel\"" + lineEnd + lineEnd + casilla.getTel() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"descripcion\"" + lineEnd + lineEnd + casilla.getDesc() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"nos\"" + lineEnd + lineEnd + casilla.getRc() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"seccion\"" + lineEnd + lineEnd + sec + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"distrito\"" + lineEnd + lineEnd + dis + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"tipo\"" + lineEnd + lineEnd + casilla.getTipo() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"imei\"" + lineEnd + lineEnd + casilla.getRg() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"nombre\"" + lineEnd + lineEnd + casilla.getNombre() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"contacto\"" + lineEnd + lineEnd + casilla.getContacto() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"video\";filename=\"" + fileName.getBytes("UTF-8").toString() + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
            System.out.println("Response 1 " + serverResponseMessage + " " + serverResponseCode);
            new HttpUrl(null).datoSend("Response 1 " + serverResponseCode);
            fileInputStream.close();
            */
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            System.out.println("Upload file to server error: " + ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new HttpUrl(null).inputStreamToString(conn.getInputStream());
        } catch (IOException ioex) {
            ioex.printStackTrace();
            System.out.println(" error: " + ioex.getMessage());
        }
        conn.disconnect();
        return serverResponseCode;  // like 200 (Ok)

    } // end upLoad2Server 

    public void datoSend(String mensajes) throws IOException {
        String ruta =  "SendMEXA.txt";

        File archivo = new File(ruta);
        BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
        if (archivo.exists()) {

            String cadena = "";
            FileReader f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            while ((cadena = b.readLine()) != null) {
                //   System.out.println(cadena);
            }
            b.close();
            bw.write(cadena + " --- " + mensajes);
        } else {
            bw.write(mensajes);
        }
        bw.close();
    }

    public String sinAcentos(String cadena) {
        String normalized = Normalizer.normalize(cadena, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\P{ASCII}+");
        return pattern.matcher(normalized).replaceAll("");

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(ServletVideo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(ServletVideo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
