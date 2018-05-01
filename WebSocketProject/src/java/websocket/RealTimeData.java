/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

/**
 *
 * @author Isa
 */
import java.io.*;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MySQLClient; 
import model.Sensor;

// Extend HttpServlet class
public class RealTimeData extends HttpServlet {
    MySQLClient client = new MySQLClient(); 
    
   // Method to handle GET method request.
   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      
      // Set refresh, autoload time as 5 seconds
      response.setIntHeader("Refresh", 5);
        
      Sensor s = new Sensor();
        try {
            s= client.getTempData("1");
        } catch (SQLException ex) {
            Logger.getLogger(RealTimeData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
      // Set response content type
      response.setContentType("text/html");
 
      // Get current time
      
      PrintWriter out = response.getWriter();
      /*String title = "Auto Page Refresh using Servlet";
      String docType =
         "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      
      out.println(docType +
         "<html>\n" +
         "<head><title>" + title + "</title></head>\n"+
         "<body bgcolor = \"#f0f0f0\">\n" +
         "<h1 align = \"center\">" + title + "</h1>\n" +
         "<p>Current temp is: " + s.getCurrentData()+ "</p>\n"
      );*/
      RequestDispatcher view = request.getRequestDispatcher("index.html");
      view.forward(request, response);
   }
   
   // Method to handle POST method request.
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      
      doGet(request, response);
   }
}