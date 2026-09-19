package com.yusuf.yusufmart.controller;

import com.yusuf.yusufmart.exception.AuthenticationException;
import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.Role;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.service.ServiceFactory;
import com.yusuf.yusufmart.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller handling user authentication: login, registration, and logout.
 * Complies with Section 4 Rule 3 (session regeneration) and thin controller rule.
 */
@WebServlet({"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {

    private final UserService userService = ServiceFactory.getUserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/login":
                request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
                break;
            case "/register":
                request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/login".equals(path)) {
            handleLogin(request, response);
        } else if ("/register".equals(path)) {
            handleRegister(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        try {
            User user = userService.authenticate(email, password);

            // Prevent session fixation (Section 4 Rule 3)
            request.changeSessionId();

            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(1800); // 30 minutes
            session.setAttribute("currentUser", user);

            if (redirect != null && !redirect.isBlank() && !redirect.contains("login") && !redirect.contains("register")) {
                response.sendRedirect(request.getContextPath() + redirect);
            } else if (user.isSeller()) {
                response.sendRedirect(request.getContextPath() + "/seller/products");
            } else if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/products");
            }
        } catch (AuthenticationException | ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String roleStr = request.getParameter("role");

        if (password != null && !password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
            return;
        }

        try {
            Role role = Role.fromString(roleStr);
            User user = userService.register(name, email, password, role);

            // Auto-login after successful registration
            request.changeSessionId();
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(1800);
            session.setAttribute("currentUser", user);

            if (user.isSeller()) {
                response.sendRedirect(request.getContextPath() + "/seller/products");
            } else {
                response.sendRedirect(request.getContextPath() + "/products?registered=true");
            }
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login?msg=logged_out");
    }
}
