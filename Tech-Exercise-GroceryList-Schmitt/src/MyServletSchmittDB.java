import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MyServletSchmittDB")
public class MyServletSchmittDB extends HttpServlet {
   private static final long serialVersionUID = 1L;
   static String url = "jdbc:mysql://ec2-52-15-186-240.us-east-2.compute.amazonaws.com:3306/GroceryList";
   static String user = "newmysqlremoteuser";
   static String password = "Dkhpgh38s2sDK";
   static Connection connection = null;

   public MyServletSchmittDB() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();
      String title = "Welcome to the Grocery List Application!";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");
      try {
         Class.forName("com.mysql.jdbc.Driver");
      } catch (ClassNotFoundException e) {
         System.out.println("Where is your MySQL JDBC Driver?");
         e.printStackTrace();
         return;
      }
      connection = null;
      try {
         connection = DriverManager.getConnection(url, user, password);
      } catch (SQLException e) {
         System.out.println("Connection Failed! Check output console");
         e.printStackTrace();
         return;
      }
      if (connection != null) {
      } else {
         System.out.println("Failed to make connection!");
      }
      try {
         String selectSQL = "SELECT * FROM List";
         response.getWriter().println("------------------------------------------<br>");
         response.getWriter().println("<b>Here are your current Groceries</b><br>");
         response.getWriter().println("------------------------------------------<br>");
         PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
         ResultSet rs = preparedStatement.executeQuery();
         while (rs.next()) {
            String id = rs.getString("ID");
            String item = rs.getString("ITEM");
            String item_type = rs.getString("ITEM_TYPE");
            String store = rs.getString("STORE");
            String person = rs.getString("PERSON");
            response.getWriter().append("ID: " + id + ", ");
            response.getWriter().append("Item: " + item + ", ");
            response.getWriter().append("Item Type: " + item_type + ", ");
            response.getWriter().append("Store: " + store + ", ");
            response.getWriter().append("Person: " + person + "<br>");
         }
         out.println("<a href=/Tech-Exercise-GroceryList-Schmitt/search_Schmitt.html>Search Grocery List</a> <br>");
         out.println("<a href=/Tech-Exercise-GroceryList-Schmitt/insert_Schmitt.html>Add More Groceries</a> <br>");
         out.println("</body></html>");
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }
}
