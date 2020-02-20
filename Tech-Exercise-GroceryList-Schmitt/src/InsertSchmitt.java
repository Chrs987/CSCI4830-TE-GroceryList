
/**
 * @file InsertSchmitt.java
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/InsertSchmitt")
public class InsertSchmitt extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public InsertSchmitt() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String item = request.getParameter("item");
      String item_type = request.getParameter("item_type");
      String store = request.getParameter("store");
      String person = request.getParameter("person");

      Connection connection = null;
      String insertSql = " INSERT INTO List (id, ITEM, ITEM_TYPE, STORE, PERSON) values (default, ?, ?, ?, ?)";

      try {
	     DBConnectionSchmitt.getDBConnection(getServletContext());
	     connection = DBConnectionSchmitt.connection;
         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
         preparedStmt.setString(1, item);
         preparedStmt.setString(2, item_type);
         preparedStmt.setString(3, store);
         preparedStmt.setString(4, person);
         preparedStmt.execute();
         connection.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Insert Groceries into a Grocery List";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h2 align=\"center\">" + title + "</h2>\n" + //
            "<ul>\n" + //

            "  <li><b>Item</b>: " + item + "\n" + //
            "  <li><b>Item Type</b>: " + item_type + "\n" + //
            "  <li><b>Store</b>: " + store + "\n" + //
            "  <li><b>Person who added</b>: " + person + "\n" + //

            "</ul>\n");

      out.println("<a href=/Tech-Exercise-GroceryList-Schmitt/search_Schmitt.html>Search Groceries</a> <br>");
      out.println("</body></html>");
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
