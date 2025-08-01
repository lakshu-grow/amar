package webLayer;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    String cadetName = request.getParameter("cadet_name");

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/corps", "root", "");

      PreparedStatement ps = con.prepareStatement(
        "SELECT * FROM cadet_details WHERE cadet_name = ?");
      ps.setString(1, cadetName);

      ResultSet rs = ps.executeQuery();

      out.println("<html><head><title>Cadet Profile</title>");
      out.println("<link rel='stylesheet' href='style.css'></head>");
      out.println("<body class='profile-page'>");
      out.println("<div class='profile-container'>");
      out.println("<h2>Your Profile</h2>");

      if(rs.next()) {
        out.println("<p><strong>Name:</strong> " + rs.getString("cadet_name") + "</p>");
        out.println("<p><strong>Register No:</strong> " + rs.getString("reg_no") + "</p>");
        out.println("<p><strong>Email:</strong> " + rs.getString("email") + "</p>");
        out.println("<p><strong>Attendance %:</strong> " + rs.getDouble("attendance_percent") + "</p>");
        out.println("<p><strong>Competition Marks:</strong> " + rs.getInt("competition_marks") + "</p>");
      } else {
        out.println("<p>No data found for this cadet.</p>");
      }

      out.println("</div></body></html>");
      con.close();
    } catch(Exception e) {
      out.println(e);
    }
  }
}
