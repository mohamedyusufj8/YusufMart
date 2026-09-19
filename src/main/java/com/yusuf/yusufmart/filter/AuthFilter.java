package com.yusuf.yusufmart.filter;

import com.yusuf.yusufmart.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication and Authorization filter.
 * Protects buyer, seller, and admin routes according to user session role.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        // Check if route requires authentication or authorization
        boolean isSellerRoute = path.startsWith("/seller");
        boolean isAdminRoute = path.startsWith("/admin");
        boolean isBuyerProtected = path.startsWith("/cart") || path.startsWith("/checkout") 
                || path.startsWith("/orders") || path.startsWith("/reviews");

        if (isSellerRoute || isAdminRoute || isBuyerProtected) {
            HttpSession session = request.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

            if (user == null) {
                // Not authenticated
                response.sendRedirect(contextPath + "/login?error=Please+login+to+continue&redirect=" + path);
                return;
            }

            // Role authorizations
            if (isAdminRoute && !user.isAdmin()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Admin role required.");
                return;
            }

            if (isSellerRoute && !(user.isSeller() || user.isAdmin())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Seller role required.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
