package webLayer;

import java.io.*;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uname = request.getParameter("username");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/corps", "root", "");

            // ✅ Step 1: Insert Attendance
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO attendance(username, date, status) VALUES(?, ?, ?)");
            ps.setString(1, uname);
            ps.setString(2, date);
            ps.setString(3, status);

            int i = ps.executeUpdate();

            if (i > 0) {
                // ✅ Step 2: Calculate Total & Present days
                PreparedStatement ps2 = con.prepareStatement(
                    "SELECT COUNT(*) FROM attendance WHERE username = ?");
                ps2.setString(1, uname);
                ResultSet rs1 = ps2.executeQuery();
                rs1.next();
                int total_days = rs1.getInt(1);

                PreparedStatement ps3 = con.prepareStatement(
                    "SELECT COUNT(*) FROM attendance WHERE username = ? AND status = 'Present'");
                ps3.setString(1, uname);
                ResultSet rs2 = ps3.executeQuery();
                rs2.next();
                int present_days = rs2.getInt(1);

                double percentage = (present_days * 100.0) / total_days;

                // ✅ Step 3: Update cadet_details — use reg_no instead of username
                PreparedStatement ps4 = con.prepareStatement(
                    "UPDATE cadet_details SET attendance_percent = ? WHERE cadet_name = ?");
                ps4.setDouble(1, percentage);
                ps4.setString(2, uname);  // uname is actually reg_no
                ps4.executeUpdate();

                out.println("<h3>Attendance marked successfully!<br>");
                out.println("Updated Attendance Percentage: " + String.format("%.2f", percentage) + "%</h3>");
            } else {
                out.println("<h3>Error marking attendance.</h3>");
            }

            con.close();

        } catch (Exception e) {
            out.println(e);
        }
    }
}
