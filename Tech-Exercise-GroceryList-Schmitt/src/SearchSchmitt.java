import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SearchSchmitt")
public class SearchSchmitt extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SearchSchmitt() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String keyword = request.getParameter("keyword");
      search(keyword, response);
   }

   void search(String keyword, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Grocery List Result";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnectionSchmitt.getDBConnection(getServletContext());
         connection = DBConnectionSchmitt.connection;

         if (keyword.isEmpty()) {
            String selectSQL = "SELECT * FROM List";
            preparedStatement = connection.prepareStatement(selectSQL);
         } else {
            String selectSQL = "SELECT * FROM List WHERE ITEM LIKE ?";
            String theListItem = keyword + "%";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, theListItem);
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            int id = rs.getInt("id");
            String item = rs.getString("item").trim();
            String item_type = rs.getString("item_type").trim();
            String store = rs.getString("store").trim();
            String person = rs.getString("person").trim();

            if (keyword.isEmpty() || item.contains(keyword)) {
               out.println("ID: " + id + ", ");
               out.println("Item: " + item + ", ");
               out.println("Item Type: " + item_type + ", ");
               out.println("Store: " + store + "<br>");
               out.println("Person who added: " + person + "<br>");
            }
         }
         out.println("<a href=/Tech-Exercise-GroceryList-Schmitt/search_Schmitt.html>Search Groceries on your list</a> <br>");
         out.println("<a href=/Tech-Exercise-GroceryList-Schmitt/insert_Schmitt.html>Add More Groceries</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
