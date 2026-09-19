package com.yusuf.yusufmart.controller;

import com.yusuf.yusufmart.config.DatabaseConfig;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Health check endpoint verifying service and database readiness.
 * Fulfills Section 18 Rule 1: GET /api/v1/health returns { "status": "UP", "db": "UP" }
 */
@WebServlet("/api/v1/health")
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String dbStatus = "DOWN";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {
            if (rs.next()) {
                dbStatus = "UP";
            }
        } catch (Exception e) {
            dbStatus = "DOWN";
        }

        if ("UP".equals(dbStatus)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\":\"UP\",\"db\":\"UP\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write("{\"status\":\"DEGRADED\",\"db\":\"DOWN\"}");
        }
    }
}
