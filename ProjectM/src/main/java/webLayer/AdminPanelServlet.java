package webLayer;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/AdminPanelServlet")
public class AdminPanelServlet extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    try {
     Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/corps", "root", "");

      // ✅ Fetch cadet_details table
      PreparedStatement ps1 = con.prepareStatement("SELECT * FROM cadet_details");
      ResultSet rs1 = ps1.executeQuery();

      // ✅ Fetch attendance summary using GROUP BY
      PreparedStatement ps2 = con.prepareStatement(
        "SELECT username, " +
        "COUNT(*) AS total_days, " +
        "SUM(CASE WHEN status='Present' THEN 1 ELSE 0 END) AS present_days, " +
        "SUM(CASE WHEN status='Absent' THEN 1 ELSE 0 END) AS absent_days, " +
        "ROUND(SUM(CASE WHEN status='Present' THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 2) AS attendance_percent " +
        "FROM attendance GROUP BY username"
      );
      ResultSet rs2 = ps2.executeQuery();

      // ✅ Fetch competition_marks table
      PreparedStatement ps3 = con.prepareStatement("SELECT * FROM competition_marks");
      ResultSet rs3 = ps3.executeQuery();

      // ✅ Start HTML
      out.println("<!DOCTYPE html><html><head><title>Admin Panel</title>");
      out.println("<link rel='stylesheet' href='style.css'></head><body>");
      out.println("<h1>Admin Panel - Full Data View</h1>");
      out.println("<a href='Admin.html'>Back to Dashboard</a>");

      // ✅ Cadet Details
      out.println("<h2>Cadet Details</h2>");
      out.println("<table><tr><th>Reg No</th><th>Name</th><th>Competition Avg</th></tr>");
      while (rs1.next()) {
        out.println("<tr>");
        out.println("<td>" + rs1.getString("reg_no") + "</td>");
        out.println("<td>" + rs1.getString("cadet_name") + "</td>");
        out.println("<td>" + rs1.getDouble("competition_marks") + "</td>");
        out.println("</tr>");
      }
      out.println("</table>");

      // ✅ Attendance Summary Table — GROUPED
      out.println("<h2>Attendance Summary</h2>");
      out.println("<table><tr><th>Reg No</th><th>Total Days</th><th>Present</th><th>Absent</th><th>Attendance %</th></tr>");
      while (rs2.next()) {
        out.println("<tr>");
        out.println("<td>" + rs2.getString("username") + "</td>");
        out.println("<td>" + rs2.getInt("total_days") + "</td>");
        out.println("<td>" + rs2.getInt("present_days") + "</td>");
        out.println("<td>" + rs2.getInt("absent_days") + "</td>");
        out.println("<td>" + rs2.getDouble("attendance_percent") + "</td>");
        out.println("</tr>");
      }
      out.println("</table>");

      // ✅ Competition Marks Table
      out.println("<h2>Competition Marks</h2>");
      out.println("<table><tr><th>Reg No</th><th>Drill</th><th>Swimming</th><th>Semaphore</th><th>Shooting</th></tr>");
      while (rs3.next()) {
        out.println("<tr>");
        out.println("<td>" + rs3.getString("reg_no") + "</td>");
        out.println("<td>" + rs3.getInt("drill") + "</td>");
        out.println("<td>" + rs3.getInt("swimming") + "</td>");
        out.println("<td>" + rs3.getInt("semaphore") + "</td>");
        out.println("<td>" + rs3.getInt("shooting") + "</td>");
        out.println("</tr>");
      }
      out.println("</table>");

      // ✅ End HTML
      out.println("</body></html>");

      con.close();

    } catch (Exception e) {
      out.println("<h3>Error: " + e + "</h3>");
    }
  }
}
