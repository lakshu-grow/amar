package webLayer;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/CadetCompetitionRulesServlet")
public class CadetCompetitionRulesServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/corps", "root", "");

            PreparedStatement ps = con.prepareStatement(
                "SELECT comp_name, rule_text FROM competition_rules");
            ResultSet rs = ps.executeQuery();

            out.println("<html><head><title>Cadet Competition Rules</title>");
            out.println("<link rel='stylesheet' href='style.css'></head>");
            out.println("<body class='cadet-competition-page'>");
            out.println("<div class='competition-view-container'>");
            out.println("<h2>Competition Rules</h2>");
            out.println("<table class='competition-table'>");
            out.println("<tr><th>Competition</th><th>Rule Details</th></tr>");

            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("comp_name") + "</td>");
                out.println("<td>" + rs.getString("rule_text") + "</td></tr>");
            }

            out.println("</table>");
            out.println("</div></body></html>");

            con.close();
        } catch (Exception e) {
            out.println(e);
        }
    }
}
