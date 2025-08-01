package webLayer;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/AddNewsServlet")
public class AddNewsServlet extends HttpServlet {
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    String title = request.getParameter("title");
    String content = request.getParameter("content");

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/corps", "root", "");

      PreparedStatement ps = con.prepareStatement(
        "INSERT INTO defence_news (title, content) VALUES (?, ?)");
      ps.setString(1, title);
      ps.setString(2, content);

      int i = ps.executeUpdate();
      if(i > 0) {
        out.println("<h3>News Added Successfully!</h3>");
      } else {
        out.println("<h3>Failed to add news!</h3>");
      }

      con.close();
    } catch (Exception e) {
      out.println(e);
    }
  }
}
