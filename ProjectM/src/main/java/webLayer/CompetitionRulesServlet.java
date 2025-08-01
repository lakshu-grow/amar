package webLayer;

import java.io.*;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/CompetitionRulesServlet")
public class CompetitionRulesServlet extends HttpServlet {
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    String compName = request.getParameter("comp_name");
    String ruleText = request.getParameter("rule_text");

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/corps", "root", "");

      PreparedStatement ps = con.prepareStatement(
        "INSERT INTO competition_rules (comp_name, rule_text) VALUES (?, ?)"
      );
      ps.setString(1, compName);
      ps.setString(2, ruleText);

      int i = ps.executeUpdate();
      if (i > 0) {
        out.println("<h3>Rule Added Successfully!</h3>");
      } else {
        out.println("<h3>Error Adding Rule.</h3>");
      }
      con.close();

    } catch (Exception e) {
      out.println(e);
    }
  }
}
