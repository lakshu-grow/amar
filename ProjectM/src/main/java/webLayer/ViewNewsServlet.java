package webLayer;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ViewNewsServlet")
public class ViewNewsServlet extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/corps", "root", "");

      Statement st = con.createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM defence_news ORDER BY date_posted DESC");

      out.println("<html><head><title>Defence News</title>");
      out.println("<link rel='stylesheet' href='style.css'></head>");
      out.println("<body class='news-view-page'>");
      out.println("<div class='news-list-container'>");
      out.println("<h2>Latest Defence News</h2>");

      while(rs.next()) {
        out.println("<div class='news-item'>");
        out.println("<h3>" + rs.getString("title") + "</h3>");
        out.println("<p>" + rs.getString("content") + "</p>");
        out.println("<p class='news-date'>Posted on: " + rs.getTimestamp("date_posted") + "</p>");
        out.println("</div>");
      }

      out.println("</div></body></html>");

      con.close();
    	
    } catch (Exception e) {
      out.println(e);
    }
  }
}
