// Save as ViewAttendanceServlet.java

package webLayer;

import java.io.*;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/ViewAttendanceServlet")
public class ViewAttendanceServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String uname = request.getParameter("username");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/corps", "root", "");

            PreparedStatement ps = con.prepareStatement(
                "SELECT date, status FROM attendance WHERE username=?");
            ps.setString(1, uname);

            ResultSet rs = ps.executeQuery();

            out.println("<html>");
            out.println("<head><title>View Attendance</title>");
            out.println("<link rel='stylesheet' href='style.css'>"); // LINK your style.css
            out.println("</head>");
            out.println("<body class='attendance-view-page'>"); // BODY class

            out.println("<div class='attendance-view-container'>");
            out.println("<h2>Attendance for " + uname + "</h2>");
            out.println("<table class='attendance-table'>");
            out.println("<tr><th>Date</th><th>Status</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("date") + "</td><td>"
                    + rs.getString("status") + "</td></tr>");
            }
            out.println("</table>");
            out.println("</div>");

            out.println("</body></html>");
            con.close();

        } catch (Exception e) {
            out.println(e);
        }
    }
}
