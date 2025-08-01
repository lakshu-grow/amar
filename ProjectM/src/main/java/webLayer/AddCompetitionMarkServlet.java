package webLayer;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/AddCompetitionMarkServlet")
public class AddCompetitionMarkServlet extends HttpServlet {
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    String reg_no = request.getParameter("reg_no");
    String event = request.getParameter("event");
    int mark = Integer.parseInt(request.getParameter("mark"));

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    try {
    Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/corps", "root", "");

      // ✅ 1. Check if cadet row exists, else insert
      PreparedStatement ps1 = con.prepareStatement(
        "SELECT * FROM competition_marks WHERE reg_no=?");
      ps1.setString(1, reg_no);
      ResultSet rs = ps1.executeQuery();

      if (rs.next()) {
        // Exists → Update
        PreparedStatement ps2 = con.prepareStatement(
          "UPDATE competition_marks SET " + event + "=? WHERE reg_no=?");
        ps2.setInt(1, mark);
        ps2.setString(2, reg_no);
        ps2.executeUpdate();
      } else {
        // New → Insert
        PreparedStatement ps3 = con.prepareStatement(
          "INSERT INTO competition_marks (reg_no, " + event + ") VALUES (?, ?)");
        ps3.setString(1, reg_no);
        ps3.setInt(2, mark);
        ps3.executeUpdate();
      }

      // ✅ 2. Calculate AVG
      PreparedStatement ps4 = con.prepareStatement(
        "SELECT drill, swimming, semaphore, shooting FROM competition_marks WHERE reg_no=?");
      ps4.setString(1, reg_no);
      ResultSet rs2 = ps4.executeQuery();

      int total = 0, count = 0;
      if (rs2.next()) {
        int[] marks = {
          rs2.getInt("drill"),
          rs2.getInt("swimming"),
          rs2.getInt("semaphore"),
          rs2.getInt("shooting")
        };
        for (int m : marks) {
          if (m > 0) {
            total += m;
            count++;
          }
        }
      }
      double avg = (count > 0) ? (total / (double) count) : 0;

      // ✅ 3. Update cadet_details table
      PreparedStatement ps5 = con.prepareStatement(
        "UPDATE cadet_details SET competition_marks=? WHERE reg_no=?");
      ps5.setDouble(1, avg);
      ps5.setString(2, reg_no);
      ps5.executeUpdate();

      out.println("<h3>Mark added successfully! Current Avg: " + avg + "</h3>");
      con.close();

    } catch (Exception e) {
      out.println(e);
    }
  }
}
